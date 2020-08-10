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
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ClearCommand extends SubCommand {

	public ClearCommand(String name, String descr, String... aliases) {
		super(name, descr, aliases);
		this.staticArgs = new String[] {"-incognito -inc -i", "-perm -ignore"};
	}

	@Override
	public boolean execute(CommandSender s, List<Pair<ArgumentType, Object>> args) {
		String server;
		if(s instanceof ProxiedPlayer) server = ((ProxiedPlayer)s).getServer().getInfo().getName();
		else server  = "*";
		boolean ignoreperm = false;
		Object arg1 = args.get(0).getRight();
		Object arg2 = args.get(1).getRight();
		Object arg3 = args.get(2).getRight();
		if(arg1 != null && arg1 instanceof ServerInfo) server = ((ServerInfo) arg1).getName();
		String nick = arg2!=null ? null : MiscUtil.getName(s);
		if(arg3 != null) ignoreperm = true;
		ChatUtil.clearChat(server, ignoreperm);
		ChatUtil.sendClearMessage(server, nick);
		s.sendMessage(MiscUtil.fixedFormat(Config.PREFIX+(server.equals("*")? Config.COMMAND_CLEAR_DONE_GLOBAL : Config.COMMAND_CLEAR_DONE.replace("%server%", server))));
		return true;
	}
	
	@Override
	public List<Pair<ArgumentType, Boolean>> getArguments() {
		List<Pair<ArgumentType, Boolean>> list = new ArrayList<Pair<ArgumentType, Boolean>>();
		list.add(new Pair<>(ArgumentType.SERVER, false)); //SERVER
		list.add(new Pair<>(ArgumentType.STATIC, false)); //-permignore -ignore
		list.add(new Pair<>(ArgumentType.STATIC, false)); //-incognito -inc -i
		return list;
	}

}
