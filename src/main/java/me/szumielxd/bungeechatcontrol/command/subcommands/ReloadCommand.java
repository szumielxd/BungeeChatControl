package me.szumielxd.bungeechatcontrol.command.subcommands;

import java.util.ArrayList;
import java.util.List;

import me.szumielxd.bungeechatcontrol.BungeeChatControl;
import me.szumielxd.bungeechatcontrol.Config;
import me.szumielxd.bungeechatcontrol.MiscUtil;
import me.szumielxd.bungeechatcontrol.Pair;
import me.szumielxd.bungeechatcontrol.command.ArgumentType;
import me.szumielxd.bungeechatcontrol.command.SubCommand;
import net.md_5.bungee.api.CommandSender;

public class ReloadCommand extends SubCommand {

	public ReloadCommand(String name, String descr, String... aliases) {
		super(name, descr, aliases);
	}

	@Override
	public boolean execute(CommandSender s, List<Pair<ArgumentType, Object>> args) {
		
		try {
			BungeeChatControl inst = BungeeChatControl.getinst();
			inst.onDisable();
			inst.onEnable();
			MiscUtil.sendMessage(s, Config.COMMAND_RELOAD_COMPLETED);
		} catch (Exception e) {
			e.printStackTrace();
			MiscUtil.sendMessage(s, Config.COMMAND_RELOAD_FAILED);
		}
		
		return true;
	}
	
	public List<Pair<ArgumentType, Boolean>> getArguments(){
		return new ArrayList<Pair<ArgumentType, Boolean>>();
	}

}
