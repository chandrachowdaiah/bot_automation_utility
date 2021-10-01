package com.nsl.automation.utility;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class BotAutomationUtility {
    private SockJsClient sockJsClient;
    private WebSocketStompClient stompClient;
    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    protected static final Gson GSON = new GsonBuilder()
            // parse dates from long:
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
            .create();
    private static List<String> questions = new ArrayList<String>();
    private static String currentQuestion = null;
    private static int i=0;
    
    public BotAutomationUtility() {
    	 List<Transport> transports = new ArrayList<>();
         transports.add(new WebSocketTransport(new StandardWebSocketClient()));
         this.sockJsClient = new SockJsClient(transports);

         this.stompClient = new WebSocketStompClient(sockJsClient);
         this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//         questions.add("What is Agent");
//         questions.add("What is CU");
//         questions.add("What is entity");
	}

    private void connect(MessageListener listener, String token, String roomId, String targetPath) throws Exception {
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        BufferedWriter writer = new BufferedWriter(new FileWriter(targetPath));

        StompSessionHandler handler = new TestSessionHandler(failure) {
        	@Override
        	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        		session.subscribe("/topic/connectResponse", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return ConnectionResponse.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        try {
                        	  session.send("/chat/register", new RegisterUserRequest("usercch", "usercch@nslhub.com", null, token));
                        } catch (Throwable t) {
                            failure.set(t);
                        }
                    }
                });
        		
        		session.subscribe("/topic/registerResponse", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return RegisterGuestResponse.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        try {
                        	 session.send("/chat/message", new MessageRequest(roomId, "Hi", token));
                        } catch (Throwable t) {
                            failure.set(t);
                        }
                    }
                });
        		
        		session.subscribe("/topic/messageResponse", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return LiveChatMessage.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                    	LiveChatMessage messageResponse = (LiveChatMessage) payload;
                        try {
                            writer.write("\n"+messageResponse.ans.text);
                            
                            if (messageResponse.ans.text.equalsIgnoreCase(currentQuestion)) {
                            	currentQuestion = null;
                            }
                            
                            if (questions.contains(messageResponse.ans.text) ||
                            		messageResponse.ans.text.equalsIgnoreCase("hi")) {
                            	return;
                            }
                            writer.write("\n========================================================");
                            writer.flush();
                            if (currentQuestion == null) {
                            if (i<=questions.size()-1) {
                            	Thread.sleep(1000);
                            	currentQuestion = questions.get(i++);
                            	session.send("/chat/message", new MessageRequest(roomId, currentQuestion, token));
                            	System.out.println("Sent question : " + i);
                            }
                            else {
                                session.send("/chat/disconnect", new ConnectionRequest(token, roomId));
                                Thread.sleep(1000);
                            	session.disconnect();
                            	System.exit(1);
                            	writer.close();
                            }
                            }
                        } catch (Throwable t) {
                            failure.set(t);
                            try {
								writer.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                    }
                });
        		session.send("/chat/connect", new ConnectionRequest(token, roomId));
        	}
        };  
        this.stompClient.connect("ws://a3c9d66a07b1d469abcf5d105aa3907a-1710918126.us-west-2.elb.amazonaws.com:{port}/livechat", this.headers, handler, 8081);
        
        if (latch.await(10000, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        }
        else {
            throw new Exception("Response not received");
        }
    }
	
	public static void main(String[] args) throws Exception {
		String roomId = UUID.randomUUID().toString();
    	String token = UUID.randomUUID().toString();
    	String filePath = args[0];
    	String targetPath = args[1];
    	BotAutomationUtility utility = new BotAutomationUtility();
    	utility.parseCSVFile(filePath);
    	MessageListener listener = new MessageListenerImpl();
    	utility.connect(listener, token, roomId, targetPath);
	}
	
	private void parseCSVFile(String fileName) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> r = reader.readAll();
            for (String[] questionRow : r) {
            	if (questionRow[3] == null || questionRow[3].isEmpty() || questionRow[3].equalsIgnoreCase("Questions")) {
            		continue;
            	}
            	questions.add(questionRow[3]);
            }
        }
	}
	
    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;

        public TestSessionHandler(AtomicReference<Throwable> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(ex);
        }
    }
}
