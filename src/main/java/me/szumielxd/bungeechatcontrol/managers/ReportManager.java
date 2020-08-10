package me.szumielxd.bungeechatcontrol.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import me.szumielxd.bungeechatcontrol.BungeeChatControl;
import me.szumielxd.bungeechatcontrol.Config;
import me.szumielxd.bungeechatcontrol.MiscUtil;
import me.szumielxd.bungeechatcontrol.Pair;
import me.szumielxd.bungeechatcontrol.data.ChatReport;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class ReportManager {
	
	
	private static HashMap<String, Pair<ChatReport, ScheduledTask>> reports = new HashMap<String, Pair<ChatReport, ScheduledTask>>();
	public static HashMap<String, Pair<ChatReport, ScheduledTask>> getActiveReports(){
		return new HashMap<String, Pair<ChatReport, ScheduledTask>>(reports);
	}
	
	
	public static boolean registerReport(final ChatReport report) {
		
		ArrayList<String> keys = new ArrayList<String>(reports.keySet());
		HashMap<String, Pair<ChatReport, ScheduledTask>> map = new HashMap<String, Pair<ChatReport, ScheduledTask>>(reports);
		if(!keys.isEmpty()) for(String code : keys) {
			ChatReport cr = map.get(code).getLeft();
			if(cr.getMessages().equals(report.getMessages())) return false;
		}
		String reportCode;
		do {
			reportCode = MiscUtil.randomString(7);
		} while (reports.containsKey(reportCode));
		final String code = reportCode;
		ScheduledTask task = ProxyServer.getInstance().getScheduler().schedule(BungeeChatControl.getinst(), new Runnable() {
			@Override
			public void run() {
				reports.remove(code);
				if(!Config.REPORT_NOTIFICATION_ADMIN_TIMEOUT.isEmpty()) {
					TextComponent msg = MiscUtil.fixedFormat(Config.REPORT_NOTIFICATION_ADMIN_TIMEOUT.replace("%accused%", report.getName()).replace("%reporter%", report.getReporter()));
					ProxyServer.getInstance().getPlayers().forEach(p -> {
						if(p.hasPermission("bungeechatcontrol.admin.notify")) p.sendMessage(msg);
					});
				}
				ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(report.getReporter());
				if(!Config.REPORT_NOTIFICATION_TIMEOUT.isEmpty()) pp.sendMessage(MiscUtil.fixedFormat(Config.REPORT_NOTIFICATION_TIMEOUT.replace("%accused%", report.getName()).replace("%reporter%", report.getReporter())));
			}
		}, Config.REPORT_TIMEOUT.longValue(), TimeUnit.SECONDS);
		reports.put(code, new Pair<>(report, task));
		sendNewReport(code);
		return true;
		
	}
	
	
	public static boolean unregisterReport(final String code, String admin, Boolean accepted) {
		if(code == null) return false;
		if(!reports.containsKey(code)) return false;
		ChatReport report = reports.get(code).getLeft();
		ScheduledTask task = reports.get(code).getRight();
		if(task == null) return false;
		task.cancel();
		reports.remove(code);
		String admmsg = "";
		if(accepted) admmsg = Config.REPORT_NOTIFICATION_ADMIN_ACCEPTED;
		else if(!accepted) admmsg = Config.REPORT_NOTIFICATION_ADMIN_REJECTED;
		if(!admmsg.isEmpty()) {
			TextComponent msg = MiscUtil.fixedFormat(admmsg.replace("%accused%", report.getName()).replace("%reporter%", report.getReporter()).replace("%admin%", admin));
			ProxyServer.getInstance().getPlayers().forEach(p -> {
				if(p.hasPermission("bungeechatcontrol.admin.notify")) p.sendMessage(msg);
			});
		}
		ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(report.getReporter());
		if(pp == null) return true;
		String message = "";
		if(accepted) message = Config.REPORT_NOTIFICATION_ACCEPTED;
		else if(!accepted) message = Config.REPORT_NOTIFICATION_REJECTED;
		if(!message.isEmpty()) pp.sendMessage(MiscUtil.fixedFormat(message.replace("%accused%", report.getName()).replace("%reporter%", report.getReporter()).replace("%admin%", admin)));
		return true;
	}
	
	
	@SuppressWarnings("deprecation")
	private static void sendNewReport(String code) {
		if(code == null) return;
		if(!reports.containsKey(code)) return;
		ChatReport cr = reports.get(code).getLeft();
		
		TextComponent text = new TextComponent();
		
		TextComponent main = MiscUtil.fixedFormat(Config.COMMAND_REPORT_NEW_TEXT.replace("%accused%", cr.getName()).replace("%reporter%", cr.getReporter()).replace("%reason%", cr.getReason()).replace("%id%", code)
				.replace("%time%", MiscUtil.parseOnlyTime(cr.getTimestamp())).replace("%date%", MiscUtil.parseOnlyDate(cr.getTimestamp())));
		String header = Config.COMMAND_REPORT_NEW_HOVER_HEADER.replace("%accused%", cr.getName()).replace("%reporter%", cr.getReporter()).replace("%reason%", cr.getReason()).replace("%id%", code)
				.replace("%time%", MiscUtil.parseOnlyTime(cr.getTimestamp())).replace("%date%", MiscUtil.parseOnlyDate(cr.getTimestamp()));
		String footer = Config.COMMAND_REPORT_NEW_HOVER_FOOTER.replace("%accused%", cr.getName()).replace("%reporter%", cr.getReporter()).replace("%reason%", cr.getReason()).replace("%id%", code)
				.replace("%time%", MiscUtil.parseOnlyTime(cr.getTimestamp())).replace("%date%", MiscUtil.parseOnlyDate(cr.getTimestamp()));
		
		if(!Config.COMMAND_REPORT_NEW_HOVER_FORMAT.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			if(!header.isEmpty())sb.append(header.replace("\\n", "\n")+"\n");
			cr.getMessages().forEach(pair -> {
				sb.append(Config.COMMAND_REPORT_NEW_HOVER_FORMAT.replace("%date%", MiscUtil.parseOnlyDate(pair.getLeft())).replace("%time%", MiscUtil.parseOnlyTime(pair.getLeft())).replace("%message%", pair.getRight()));
				sb.append('\n');
			});
			if(!footer.isEmpty()) sb.append(footer.replace("\\n", "\n"));
			main.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(sb.toString()).create()));
		}
		if(!Config.COMMAND_REPORT_NEW_CLICK.isEmpty()) main.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Config.COMMAND_REPORT_NEW_CLICK.replace("%accused%", cr.getName()).replace("%reporter%", cr.getReporter()).replace("%reason%", cr.getReason())));
		TextComponent accept = MiscUtil.fixedFormat(Config.COMMAND_REPORT_NEW_ACCEPT);
		accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Config.COMMAND_REPORT_NEW_ACCEPT_HOVER.replace("\\n", "\n")).create()));
		accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"+Config.COMMAND+" report accept "+code));
		TextComponent reject = MiscUtil.fixedFormat(Config.COMMAND_REPORT_NEW_REJECT);
		reject.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Config.COMMAND_REPORT_NEW_REJECT_HOVER.replace("\\n", "\n")).create()));
		reject.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"+Config.COMMAND+" report reject "+code));
		text.addExtra(main);
		text.addExtra(" ");
		text.addExtra(accept);
		text.addExtra(" ");
		text.addExtra(reject);
		ProxyServer.getInstance().getPlayers().forEach(p -> {
			if(p.hasPermission("bungeechatcontrol.admin.notify")) p.sendMessage(text);
		});
	}
	

}
