package com.nsl.automation.utility;

import java.util.List;

public class LiveChatMessage {
	public String question;
	public Ans ans;
	public String document;
	public String score;
	public String topic;
	public Alternate alternate;
	public Media media;
	public String response_type;
	public Trace trace;
	public boolean probe_mode;
	public String ans_format;
	public double timestamp;
	public TenantInfo tenant_info;
	public String roomId;
	public String user;

	public static class Ans {
		public String text;
		public List<Object> data;
	}

	public static class Alternate {
	}

	public static class Media {
		public List<String> image;
		public List<Object> video;
		public List<Object> audio;
	}

	public static class Trace {
	}

	public static class TenantInfo {
	}
}