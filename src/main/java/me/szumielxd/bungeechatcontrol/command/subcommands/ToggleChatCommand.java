package me.szumielxd.bungeechatcontrol.command.subcommands;

import java.util.ArrayList;
import java.util.List;

import me.szumielxd.bungeechatcontrol.ChatUtil;
import me.szumielxd.bungeechatcontrol.Config;
import me.szumielxd.bungeechatcontrol.MiscUtil;
import me.szumielxd.bungeechatcontrol.Pair;
import me.szumielxd.bungeechatcontrol.command.ArgumentType;
import me.szumielxd.bungeechatcontrol.command.SubCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ToggleChatCommand extends SubCommand {

	public ToggleChatCommand(String name, String descr, String... aliases) {
		super(name, descr, aliases);
		this.staticArgs = new String[] {"-incognito -inc -i"};
	}

	@Override
	public boolean execute(CommandSender s, List<Pair<ArgumentType, Object>> args) {
		String server;
		if(s instanceof ProxiedPlayer) server = ((ProxiedPlayer)s).getServer().getInfo().getName();
		else server  = "*";
		boolean incognito = false;
		Object arg1 = args.get(0).getRight();
		Object arg2 = args.get(1).getRight();
		Object arg3 = args.get(2).getRight();
		if(arg2 != null && arg2 instanceof ServerInfo) server = ((ServerInfo) arg2).getName();
		if(arg3 != null) incognito = true;
		boolean on = !ChatUtil.isExactlyAllowed(server);
		if(arg1 != null && arg1 instanceof Boolean) {
			boolean bool = (Boolean)arg1;
			if(bool != on) {
				s.sendMessage(MiscUtil.fixedFormat(Config.PREFIX+(bool? Config.COMMAND_TOGGLE_ON_ALREADY : Config.COMMAND_TOGGLE_OFF_ALREADY)));
				return true;
			}
		}
		if(on) ChatUtil.allow(server);
		else ChatUtil.disallow(server);
		List<ProxiedPlayer> players = new ArrayList<ProxiedPlayer>(MiscUtil.getPlayers(server));
		TextComponent text;
		if(on) text = MiscUtil.fixedFormat(incognito? Config.COMMAND_TOGGLE_ON_INFO_INCOGNITO : Config.COMMAND_TOGGLE_ON_INFO.replace("%sender%", MiscUtil.getName(s)));
		else text = MiscUtil.fixedFormat(incognito? Config.COMMAND_TOGGLE_OFF_INFO_INCOGNITO : Config.COMMAND_TOGGLE_OFF_INFO.replace("%sender%", MiscUtil.getName(s)));
		if(players != null && !players.isEmpty()) players.forEach(p -> p.sendMessage(text));
		if(server.equals("*")) s.sendMessage(MiscUtil.fixedFormat(Config.PREFIX+(on? Config.COMMAND_TOGGLE_ON_DONE_GLOBAL : Config.COMMAND_TOGGLE_OFF_DONE_GLOBAL)));
		else s.sendMessage(MiscUtil.fixedFormat(Config.PREFIX+(on? Config.COMMAND_TOGGLE_ON_DONE : Config.COMMAND_TOGGLE_OFF_DONE).replace("%server%", server)));
		
		return true;
	}
	
	@Override
	public List<Pair<ArgumentType, Boolean>> getArguments() {
		List<Pair<ArgumentType, Boolean>> list = new ArrayList<Pair<ArgumentType, Boolean>>();
		list.add(new Pair<>(ArgumentType.BOOLEAN, false)); //ON-OFF
		list.add(new Pair<>(ArgumentType.SERVER, false)); //SERVER
		list.add(new Pair<>(ArgumentType.STATIC, false)); //-incognito -inc -i
		return list;
	}

}
