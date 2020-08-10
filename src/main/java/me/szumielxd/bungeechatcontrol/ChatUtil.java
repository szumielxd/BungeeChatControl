package me.szumielxd.bungeechatcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PrimitiveIterator.OfInt;
import java.util.Random;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatUtil {
	
	private static List<String> disabled = new ArrayList<String>();
	
	
	// CHECK IF SERVER HAS ENABLED CHAT
	public static boolean isAllowed(String server) {
		if(disabled.contains("*")) return false;
		if(disabled.contains(server.toLowerCase())) return false;
		return true;
	}
	
	
	public static boolean isExactlyAllowed(String server) {
		if(disabled.contains(server.toLowerCase())) return false;
		return true;
	}
	
	
	// ENABLE CHAT ON SERVER. USE * FOR GLOBAL
	public static boolean allow(String server) {
		return disabled.remove(server.toLowerCase());
	}
	
	
	// DISABLE CHAT ON SERVER. USE * FOR GLOBAL
	public static boolean disallow(String server) {
		if(disabled.contains(server)) return false;
		return disabled.add(server.toLowerCase());
	}
	
	
	/*
	 * List of methods used to clear chat
	 * Default values:
	 * - server = "*"
	 * - ignorePermissions = false
	 */
	public static void chatClear() {
		clearChat("*", false);
	}
	public static void chatClear(String server) {
		clearChat(server, false);
	}
	public static void chatClear(boolean ignorePermissions) {
		clearChat("*", ignorePermissions);
	}
	public static void clearChat(String server, boolean ignorePermissions) {
		ServerInfo srv = ProxyServer.getInstance().getServerInfo(server);
		if(srv == null) return;
		Collection<ProxiedPlayer> list = MiscUtil.getPlayers(server);
		int lines = 300;
		String[] texts = new String[]{"   ", "          ", "      ", "               ", " ", "                     "};
		OfInt ints = new Random().ints(lines, 0, Integer.MAX_VALUE).iterator();
		BaseComponent[] clear = new BaseComponent[lines];
		for(int i=0; i<lines; i++) {
			clear[i] = new TextComponent(texts[ints.next()%texts.length]);
		}
		if(list == null || list.isEmpty()) return;
		if(ignorePermissions) {
			for(ProxiedPlayer p : list) {
				for(int i=0; i<lines; i++) {
					p.sendMessage(clear[i]);
				}
			}
		} else {
			for(ProxiedPlayer p : list) {
				if(!p.hasPermission("bungeechatcontrol.bypass.clear")) for(int i=0; i<lines; i++) {
					p.sendMessage(clear[i]);
				}
			}
		}
	}
	
	
	public static void sendClearMessage(String server, String cleaner) {
		Collection<ProxiedPlayer> players = MiscUtil.getPlayers(server);
		String msg;
		if(cleaner == null) msg = Config.CHAT_CLEAR_INFO_INCOGNITO;
		else msg = Config.CHAT_CLEAR_INFO.replace("%sender%", cleaner);
		if(msg == null || msg.isEmpty()) return;
		TextComponent text = MiscUtil.fixedFormat(msg);
		ProxyServer.getInstance().getConsole().sendMessage(text);
		if(players == null || players.isEmpty()) return;
		players.forEach(p -> {
			p.sendMessage(text);
		});
	}
	

}
