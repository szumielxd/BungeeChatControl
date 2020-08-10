package me.szumielxd.bungeechatcontrol.command.subcommands;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.szumielxd.bungeechatcontrol.ChatUtil;
import me.szumielxd.bungeechatcontrol.Config;
import me.szumielxd.bungeechatcontrol.MiscUtil;
import me.szumielxd.bungeechatcontrol.Pair;
import me.szumielxd.bungeechatcontrol.command.ArgumentType;
import me.szumielxd.bungeechatcontrol.command.SubCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

public class ListCommand extends SubCommand {

	public ListCommand(String name, String descr, String... aliases) {
		super(name, descr, aliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender s, List<Pair<ArgumentType, Object>> args) {
		Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
		TextComponent message = MiscUtil.fixedFormat(Config.COMMAND_LIST_START);
		boolean first = false;
		if(servers != null && !servers.isEmpty()) {
			for(String srv : servers.keySet()) {
				String status = ChatUtil.isAllowed(srv)? Config.COMMAND_LIST_ENABLED : Config.COMMAND_LIST_DISABLED;
				TextComponent name = MiscUtil.fixedFormat(status+srv);
				ServerInfo info = servers.get(srv);
				String online = info.getPlayers().size()+"";
				String restricted = MiscUtil.parseStatus(info.isRestricted());
				String stat = MiscUtil.parseBool(ChatUtil.isExactlyAllowed(srv));
				String perm = info.getPermission();
				String ip = "localhost";
				String port = "-1";
				if(info.getSocketAddress() instanceof InetSocketAddress) {
					InetSocketAddress socket = (InetSocketAddress) info.getSocketAddress();
					port = socket.getPort()+"";
					ip = socket.getAddress().getHostAddress();
				}
				if(Config.COMMAND_LIST_HOVER != null && !Config.COMMAND_LIST_HOVER.isEmpty()) {
					String hover = String.join("\n", Config.COMMAND_LIST_HOVER);
					hover = hover.replace("%server%", srv);
					hover = hover.replace("%online%", online);
					hover = hover.replace("%restricted%", restricted);
					hover = hover.replace("%status%", stat);
					hover = hover.replace("%permission%", perm);
					hover = hover.replace("%ip%", ip);
					hover = hover.replace("%port%", port);
					name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
				}
				if(Config.COMMAND_LIST_CLICK != null && !Config.COMMAND_LIST_CLICK.isEmpty()) {
					String[] arr = Config.COMMAND_LIST_CLICK.split(": ", 2);
					ClickEvent.Action act = null;
					String cmd= "";
					if(arr.length == 2) {
						act = ClickEvent.Action.valueOf(arr[0].toUpperCase());
						cmd = arr[1];
					}
					if(act == null) {
						act = ClickEvent.Action.RUN_COMMAND;
						cmd = Config.COMMAND_LIST_CLICK;
					}
					cmd = cmd.replace("%server%", srv);
					cmd = cmd.replace("%online%", online);
					cmd = cmd.replace("%restricted%", restricted);
					cmd = cmd.replace("%status%", stat);
					cmd = cmd.replace("%permission%", perm);
					cmd = cmd.replace("%ip%", ip);
					cmd = cmd.replace("%port%", port);
					name.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
				}
				message.addExtra(name);
				if(first) first = true;
				else message.addExtra(MiscUtil.fixedFormat(Config.COMMAND_LIST_SEPARATOR));
			}
		}
		s.sendMessage(message);
		return true;
	}

	@Override
	public List<Pair<ArgumentType, Boolean>> getArguments() {
		return new ArrayList<Pair<ArgumentType, Boolean>>();
	}

}
