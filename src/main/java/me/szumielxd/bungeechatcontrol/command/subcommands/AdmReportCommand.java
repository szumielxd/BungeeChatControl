package me.szumielxd.bungeechatcontrol.command.subcommands;

import java.util.ArrayList;
import java.util.List;

import me.szumielxd.bungeechatcontrol.Config;
import me.szumielxd.bungeechatcontrol.MiscUtil;
import me.szumielxd.bungeechatcontrol.Pair;
import me.szumielxd.bungeechatcontrol.command.ArgumentType;
import me.szumielxd.bungeechatcontrol.command.SubCommand;
import me.szumielxd.bungeechatcontrol.data.ChatReport;
import me.szumielxd.bungeechatcontrol.managers.ReportManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class AdmReportCommand extends SubCommand {

	public AdmReportCommand(String name, String descr, String... aliases) {
		super(name, descr, aliases);
		this.staticArgs = new String[] {"accept reject list"};
	}

	@Override
	public boolean execute(CommandSender s, List<Pair<ArgumentType, Object>> args) {
		Object arg1 = args.get(0).getRight();
		Object arg2 = args.get(1).getRight();
		String action = arg1.toString();
		switch(action) {
			case "accept":{
				accept(s, arg2==null? null : arg2.toString());
				return true;
			}
			case "reject":{
				reject(s, arg2==null? null : arg2.toString());
				return true;
			}
			case "list":{
				list(s);
				return true;
			}
			default:{
				return false;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void list(CommandSender s) {
		if(!Config.COMMAND_REPORT_LIST_HEADER.isEmpty()) s.sendMessage(MiscUtil.fixedFormat(Config.COMMAND_REPORT_LIST_HEADER));
		
		if(!ReportManager.getActiveReports().isEmpty()){
			for(String code : ReportManager.getActiveReports().keySet()) {
				ChatReport cr = ReportManager.getActiveReports().get(code).getLeft();
				TextComponent text = new TextComponent();
				TextComponent main = MiscUtil.fixedFormat(Config.COMMAND_REPORT_LIST_FORMAT.replace("%accused%", cr.getName()).replace("%reporter%", cr.getReporter()).replace("%reason%", cr.getReason()).replace("%id%", code)
						.replace("%time%", MiscUtil.parseOnlyTime(cr.getTimestamp())).replace("%date%", MiscUtil.parseOnlyDate(cr.getTimestamp())));
				String header = Config.COMMAND_REPORT_LIST_HOVER_HEADER.replace("%accused%", cr.getName()).replace("%reporter%", cr.getReporter()).replace("%reason%", cr.getReason()).replace("%id%", code)
						.replace("%time%", MiscUtil.parseOnlyTime(cr.getTimestamp())).replace("%date%", MiscUtil.parseOnlyDate(cr.getTimestamp()));
				String footer = Config.COMMAND_REPORT_LIST_HOVER_FOOTER.replace("%accused%", cr.getName()).replace("%reporter%", cr.getReporter()).replace("%reason%", cr.getReason()).replace("%id%", code)
						.replace("%time%", MiscUtil.parseOnlyTime(cr.getTimestamp())).replace("%date%", MiscUtil.parseOnlyDate(cr.getTimestamp()));
				if(!Config.COMMAND_REPORT_LIST_HOVER_FORMAT.isEmpty()) {
					StringBuilder sb = new StringBuilder();
					if(!header.isEmpty())sb.append(header.replace("\\n", "\n")+"\n");
					cr.getMessages().forEach(pair -> {
						sb.append(Config.COMMAND_REPORT_LIST_HOVER_FORMAT.replace("%date%", MiscUtil.parseOnlyDate(pair.getLeft())).replace("%time%", MiscUtil.parseOnlyTime(pair.getLeft())).replace("%message%", pair.getRight()));
						sb.append('\n');
					});
					if(!footer.isEmpty()) sb.append(footer.replace("\\n", "\n"));
					main.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(sb.toString()).create()));
				}
				if(!Config.COMMAND_REPORT_LIST_CLICK.isEmpty()) main.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Config.COMMAND_REPORT_LIST_CLICK.replace("%accused%", cr.getName()).replace("%reporter%", cr.getReporter()).replace("%reason%", cr.getReason())));
				TextComponent accept = MiscUtil.fixedFormat(Config.COMMAND_REPORT_NEW_ACCEPT);
				accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Config.COMMAND_REPORT_LIST_ACCEPT_HOVER.replace("\\n", "\n")).create()));
				accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"+Config.COMMAND+" report accept "+code));
				TextComponent reject = MiscUtil.fixedFormat(Config.COMMAND_REPORT_NEW_REJECT);
				reject.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Config.COMMAND_REPORT_LIST_REJECT_HOVER.replace("\\n", "\n")).create()));
				reject.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"+Config.COMMAND+" report reject "+code));
				text.addExtra(main);
				text.addExtra(" ");
				text.addExtra(accept);
				text.addExtra(" ");
				text.addExtra(reject);
				s.sendMessage(text);
			}
		}
		
		if(!Config.COMMAND_REPORT_LIST_FOOTER.isEmpty()) s.sendMessage(MiscUtil.fixedFormat(Config.COMMAND_REPORT_LIST_FOOTER));
	}
	
	private void accept(CommandSender s, String code) {
		if(!ReportManager.getActiveReports().containsKey(code)) {
			MiscUtil.sendMessage(s, Config.COMMAND_REPORT_INVALID_ID);
			return;
		}
		ReportManager.unregisterReport(code, MiscUtil.getName(s), true);
	}
	
	private void reject(CommandSender s, String code) {
		if(!ReportManager.getActiveReports().containsKey(code)) {
			MiscUtil.sendMessage(s, Config.COMMAND_REPORT_INVALID_ID);
			return;
		}
		ReportManager.unregisterReport(code, MiscUtil.getName(s), false);
	}
	
	@Override
	public List<Pair<ArgumentType, Boolean>> getArguments() {
		List<Pair<ArgumentType, Boolean>> list = new ArrayList<Pair<ArgumentType, Boolean>>();
		list.add(new Pair<>(ArgumentType.STATIC, true)); //accept reject list
		list.add(new Pair<>(ArgumentType.TEXT, false)); //CODE
		return list;
	}

}
