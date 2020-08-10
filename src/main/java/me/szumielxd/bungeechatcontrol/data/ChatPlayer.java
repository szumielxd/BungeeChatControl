package me.szumielxd.bungeechatcontrol.data;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.szumielxd.bungeechatcontrol.BungeeChatControl;
import me.szumielxd.bungeechatcontrol.Pair;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class ChatPlayer {

	
	private UUID uuid;
	private String name;
	private ArrayList<Pair<Long, String>> chat = new ArrayList<Pair<Long, String>>();
	
	
	private ChatPlayer(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
		chatPlayers.add(this);
	}
	
	
	public void append(Long timestamp, String message) {
		if(timestamp == null) throw new NullPointerException("date cannot be null");
		if(message == null) throw new NullPointerException("message cannot be null");
		chat.add(new Pair<>(timestamp, message));
		if(chat.size() > 5) chat.remove(0);
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public String getName() {
		return new String(this.name);
	}
	
	public ArrayList<Pair<Long, String>> getLastMessages() {
		return new ArrayList<Pair<Long, String>>(this.chat);
	}
	
	
	private static ArrayList<ChatPlayer> chatPlayers = new ArrayList<ChatPlayer>();
	private static ScheduledTask task;
	
	public static void load() {
		if(task != null) task.cancel();
		task = ProxyServer.getInstance().getScheduler().schedule(BungeeChatControl.getinst(), new Runnable() {
			@Override
			public void run() {
				if(chatPlayers.isEmpty()) return;
				for(ChatPlayer cp : new ArrayList<ChatPlayer>()) {
					if(ProxyServer.getInstance().getPlayer(cp.getUUID()) == null) {
						chatPlayers.remove(cp);
					}
				}
				
			}
		}, 20L, 20L, TimeUnit.SECONDS);
	}
	
	public static void unload() {
		if(task == null) return;
		task.getTask().run();
		task.cancel();
		task = null;
	}
	
	public static ChatPlayer getChatPlayer(UUID uuid) {
		ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(uuid);
		if(pp == null) return null;
		String name = pp.getName();
		if(chatPlayers.size() == 0) return new ChatPlayer(uuid, name);
		for(ChatPlayer cp : new ArrayList<ChatPlayer>(chatPlayers)) {
			if(cp.getUUID().equals(uuid)) return cp;
		}
		return new ChatPlayer(uuid, name);
	}
	
	public static ChatPlayer getChatPlayer(String name) {
		ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(name);
		if(pp == null) return null;
		UUID uuid = pp.getUniqueId();
		if(chatPlayers.size() == 0) return new ChatPlayer(uuid, name);
		for(ChatPlayer cp : new ArrayList<ChatPlayer>(chatPlayers)) {
			if(cp.getName().equals(name)) return cp;
		}
		return new ChatPlayer(uuid, name);
	}
	
	
}
