package me.szumielxd.bungeechatcontrol.command;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.szumielxd.bungeechatcontrol.Config;
import me.szumielxd.bungeechatcontrol.MiscUtil;
import me.szumielxd.bungeechatcontrol.Pair;
import me.szumielxd.bungeechatcontrol.command.subcommands.AdmReportCommand;
import me.szumielxd.bungeechatcontrol.command.subcommands.ClearCommand;
import me.szumielxd.bungeechatcontrol.command.subcommands.HelpCommand;
import me.szumielxd.bungeechatcontrol.command.subcommands.ListCommand;
import me.szumielxd.bungeechatcontrol.command.subcommands.ReloadCommand;
import me.szumielxd.bungeechatcontrol.command.subcommands.ToggleChatCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MainCommand extends Command {

	public MainCommand(String name, String[] aliases) {
		super(name, "bungeechatcontrol.command.help", aliases);
		new ClearCommand("clear", Config.COMMAND_CLEAR_DESCRIPTION, Config.COMMAND_CLEAR_ALIASES);
		new ToggleChatCommand("toggle", Config.COMMAND_TOGGLE_DESCRIPTION, Config.COMMAND_TOGGLE_ALIASES);
		new HelpCommand("", Config.COMMAND_HELP_DESCRIPTION, Config.COMMAND_HELP_ALIASES);
		new ListCommand("list", Config.COMMAND_LIST_DESCRIPTION, Config.COMMAND_LIST_ALIASES);
		new ReloadCommand("reload", Config.COMMAND_RELOAD_DESCRIPTION, Config.COMMAND_RELOAD_ALIASES);
		new AdmReportCommand("report", Config.COMMAND_REPORT_DESCRIPTION, Config.COMMAND_REPORT_ALIASES);
	}

	@Override
	public void execute(CommandSender s, String[] args) {
		
		if(args.length == 0) args = new String[] {""};
		String[] subargs = Arrays.copyOfRange(args, 1, args.length);
		Map<String, SubCommand> subcommands = SubCommand.getSubCommands();
		if(subcommands == null || subcommands.size() == 0) {
			MiscUtil.sendMessage(s, Config.COMMAND_ERROR);
			return;
		}
		SubCommand last = null;
		for(SubCommand subcmd : subcommands.values()) {
			if(subcmd.match("")) last = subcmd;
			if(subcmd.match(args[0])) {
				tryRun(subcmd, s, subargs);
				return;
			}
		}
		if(last == null) return;
		tryRun(last, s, subargs);
		return;
	}
	
	
	private void tryRun(SubCommand subcmd, CommandSender s, String[] args) {
		if(subcmd.hasPermission(s)) {
			try {
				List<Pair<ArgumentType, Object>> subargs = subcmd.parseValues(args);
				try {
					if(!subcmd.execute(s, subargs)) MiscUtil.sendMessage(s, Config.COMMAND_ERROR);
				} catch (Throwable e) {
					e.printStackTrace();
					MiscUtil.sendMessage(s, Config.COMMAND_ERROR);
				}
			} catch (IllegalArgumentException e) {
				String[] arr = e.getMessage().split(",");
				if(arr.length != 2) MiscUtil.sendMessage(s, Config.COMMAND_ERROR);
				Integer i = Integer.valueOf(arr[0]);
				if(i == null) MiscUtil.sendMessage(s, Config.COMMAND_ERROR);
				ArgumentType type = ArgumentType.valueOf(arr[1]);
				String msg;
				if(type.equals(ArgumentType.STATIC)) msg = Config.COMMAND_PARSE_ERROR.replace("%arg%", args.length>i? args[i] : "").replace("%type%", subcmd.getArgsString().split(" ")[i]);
				else msg = Config.COMMAND_PARSE_ERROR.replace("%arg%", args.length>i? args[i] : "").replace("%type%", type.getDisplay());
				MiscUtil.sendMessage(s, msg);
			}
		} else {
			MiscUtil.sendMessage(s, Config.COMMAND_PERMISSION_ERROR);
		}
	}
		

}
