package me.szumielxd.bungeechatcontrol;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MiscUtil {
	
	
	private static char[] randomCharset = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	
	public static Collection<ProxiedPlayer> getPlayers(String server) {
		if(server.equals("*")) {
			return ProxyServer.getInstance().getPlayers();
		} else {
			ServerInfo srv = ProxyServer.getInstance().getServerInfo(server);
			if(srv == null) return Collections.emptyList();
			return srv.getPlayers();
		}
	}
	
	
	public static void sendMessage(CommandSender s, String message) {
		if(message.isEmpty()) return;
		String name = (s instanceof ProxiedPlayer) ? s.getName() : Config.CONSOLE;
		s.sendMessage(fixedFormat(Config.PREFIX + message.replace("%sender%", name)));
	}
	
	
	public static String getName(CommandSender s) {
		String name;
		if(s instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) s;
			name = p.getDisplayName();
			if(Config.SETTINGS_USE_LUCKPERMS_DISPLAYNAME) {
				try {
					Class.forName("");
					LuckPerms api = LuckPermsProvider.get();
					User user = api.getUserManager().getUser(p.getUniqueId());
					ContextManager cm = api.getContextManager();
					QueryOptions queryOptions = cm.getQueryOptions(user).orElse(cm.getStaticQueryOptions());
					CachedMetaData meta = user.getCachedData().getMetaData(queryOptions);
					name = meta.getPrefix()+name+(meta.getSuffix());
				} catch(Exception e) {
				}
			}
		} else {
			name = Config.CONSOLE;
		}
		return name;
	}
	
	
	public static String parseStatus(boolean status) {
		return status? Config.MISC_ON : Config.MISC_OFF;
	}
	
	
	public static String parseBool(boolean status) {
		return status? Config.MISC_TRUE : Config.MISC_FALSE;
	}
	
	
	public static TextComponent fixedFormat(String str) {
		String[] arr = str.split("§");
		boolean bold = false;
		boolean italic = false;
		boolean underlined = false;
		boolean strikethrough = false;
		boolean obfuscated = false;
		ChatColor color = ChatColor.RESET;
		TextComponent result = new TextComponent(arr[0]!=null? arr[0] : "");
		for(int i=1; i < arr.length; i++) {
			TextComponent text = new TextComponent(arr[i].substring(1));
			switch(arr[i].substring(0, 1)) {
				case "r": {
					bold = false;
					italic = false;
					underlined = false;
					strikethrough = false;
					obfuscated = false;
					color = ChatColor.RESET;
					break;
				}
				case "l": {
					bold = true;
					break;
				}
				case "o": {
					italic = true;
					break;
				}
				case "n": {
					underlined = true;
					break;
				}
				case "m": {
					strikethrough = true;
					break;
				}
				case "k": {
					obfuscated = true;
					break;
				}
				default: {
					try{
						Integer.parseInt(arr[i].substring(0, 1), 16);
						color = ChatColor.getByChar(arr[i].charAt(0));
					} catch(NumberFormatException e) {
						text.setText(arr[i]);
					}
				}
			}
			text.setBold(bold);
			text.setItalic(italic);
			text.setUnderlined(underlined);
			text.setStrikethrough(strikethrough);
			text.setObfuscated(obfuscated);
			text.setColor(color);
			result.addExtra(text);
		}
		return result;
	}
	
	
	public static String randomString(int length) {
		StringBuilder sb = new StringBuilder();
		new Random().ints(length, 0, randomCharset.length-1).forEach(cons ->{
			sb.append(randomCharset[cons]);
		});
		return sb.toString();
	}
	
	
	public static String parseOnlyDate(long timestamp) {
		return new SimpleDateFormat("dd-MM-yyyy").format(new Date(timestamp));
	}
	
	
	public static String parseOnlyTime(long timestamp) {
		return new SimpleDateFormat("HH:mm:ss").format(new Date(timestamp));
	}
	

}
