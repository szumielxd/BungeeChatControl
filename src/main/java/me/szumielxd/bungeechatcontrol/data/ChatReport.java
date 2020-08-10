package me.szumielxd.bungeechatcontrol.data;

import java.util.ArrayList;

import me.szumielxd.bungeechatcontrol.Pair;

public class ChatReport {
	
	
	private Long timestamp;
	private String name;
	private String reporter;
	private String reason;
	private ArrayList<Pair<Long, String>> messages;
	
	
	public ChatReport(ChatPlayer cp, String reporter, String reason) {
		if(reporter == null) throw new NullPointerException("reporter cannot be null");
		if(cp == null) throw new NullPointerException("cp cannot be null");
		if(reason == null) throw new NullPointerException("reason cannot be null");
		this.name = cp.getName();
		this.reporter = reporter;
		this.reason = reason;
		this.timestamp = System.currentTimeMillis();
		this.messages = cp.getLastMessages();
	}
	
	
	public String getName() {
		return this.name;
	}
	
	public Long getTimestamp() {
		return this.timestamp;
	}
	
	public ArrayList<Pair<Long, String>> getMessages(){
		return new ArrayList<Pair<Long, String>>(this.messages);
	}
	
	public String getReporter() {
		return new String(this.reporter);
	}
	
	public String getReason() {
		return new String(this.reason);
	}
	

}
