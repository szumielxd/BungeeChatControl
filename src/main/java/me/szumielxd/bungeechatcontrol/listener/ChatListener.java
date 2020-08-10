package me.szumielxd.bungeechatcontrol.listener;

import me.szumielxd.bungeechatcontrol.ChatUtil;
import me.szumielxd.bungeechatcontrol.Config;
import me.szumielxd.bungeechatcontrol.data.ChatPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener implements Listener {
	
	
	// MUTE CHAT
	@EventHandler(priority = EventPriority.HIGH)
	public void checkMutedChat(ChatEvent e) {
		if(e.isCancelled()) return;
		if (!e.isCommand()) {
			if(e.getSender() instanceof ProxiedPlayer) {
				ProxiedPlayer p = (ProxiedPlayer) e.getSender();
				if(!p.hasPermission("bungeechatcontrol.bypass.chat")) {
					if(!ChatUtil.isAllowed(p.getServer().getInfo().getName())) {
						e.setCancelled(true);
						p.sendMessage(new TextComponent(Config.CHAT_DENY_MESSAGE));
					}
				}
			}
		}	
	}
	
	
	// SAVE CHAT TO CACHE
	@EventHandler(priority = EventPriority.LOWEST)
	public void chatSaver(ChatEvent e) {
		if(e.isCancelled()) return;
		if(e.isCommand()) return;
		if(e.getSender() instanceof ProxiedPlayer) {
			if(e.getMessage() == null) return;
			ChatPlayer cp = ChatPlayer.getChatPlayer(((ProxiedPlayer)e.getSender()).getUniqueId());
			//if(cp == null) return;
			cp.append(System.currentTimeMillis(), e.getMessage());
		}
	}
	

}
