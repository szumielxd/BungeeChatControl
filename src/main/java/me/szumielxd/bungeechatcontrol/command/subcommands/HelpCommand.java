package me.szumielxd.bungeechatcontrol.command.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.szumielxd.bungeechatcontrol.Config;
import me.szumielxd.bungeechatcontrol.MiscUtil;
import me.szumielxd.bungeechatcontrol.Pair;
import me.szumielxd.bungeechatcontrol.command.ArgumentType;
import me.szumielxd.bungeechatcontrol.command.SubCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class HelpCommand extends SubCommand {

	public HelpCommand(String name, String descr, String... aliases) {
		super(name, descr, aliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender s, List<Pair<ArgumentType, Object>> args) {
		
		if(Config.COMMAND_HELP_HEADER != null && !Config.COMMAND_HELP_HEADER.isEmpty()) s.sendMessage(MiscUtil.fixedFormat(Config.COMMAND_HELP_HEADER));
		
		Map<String, SubCommand> map = SubCommand.getSubCommands();
		if(map != null && !map.isEmpty())for(String cmd : map.keySet()) {
			String argstr = map.get(cmd).getArgsString();
			String cmdName = Config.COMMAND+" "+cmd;
			String description = map.get(cmd).getDescription();
			TextComponent message = MiscUtil.fixedFormat(Config.COMMAND_HELP_FORMAT.replace("%command%", cmdName).replace("%aliases%", String.join(", ", map.get(cmd).getAliases())).replace("%args%", argstr).replace("%description%", description).replace("%sender%", MiscUtil.getName(s)));
			if(Config.COMMAND_HELP_HOVER != null && !Config.COMMAND_HELP_HOVER.isEmpty()) {
				TextComponent hover = MiscUtil.fixedFormat(String.join("\n", Config.COMMAND_HELP_HOVER).replace("%command%", cmdName).replace("%aliases%", String.join(", ", map.get(cmd).getAliases())).replace("%args%", argstr).replace("%description%", description).replace("%sender%", MiscUtil.getName(s)));
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
			}
			if(Config.COMMAND_HELP_CLICK != null && !Config.COMMAND_HELP_CLICK.isEmpty()) {
				String click = String.join("\n", Config.COMMAND_HELP_CLICK).replace("%command%", cmdName).replace("%aliases%", String.join(", ", map.get(cmd).getAliases())).replace("%args%", argstr).replace("%description%", description).replace("%sender%", MiscUtil.getName(s));
				message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, click));
			}
			s.sendMessage(message);
		}
		
		if(Config.COMMAND_HELP_FOOTER != null && !Config.COMMAND_HELP_FOOTER.isEmpty()) s.sendMessage(MiscUtil.fixedFormat(Config.COMMAND_HELP_FOOTER));
		
		return true;
	}
	
	@Override
	public List<Pair<ArgumentType, Boolean>> getArguments() {
		return new ArrayList<Pair<ArgumentType, Boolean>>();
	}

}
