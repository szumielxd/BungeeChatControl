package me.szumielxd.bungeechatcontrol.command;

import java.util.Arrays;

import me.szumielxd.bungeechatcontrol.Config;
import me.szumielxd.bungeechatcontrol.MiscUtil;
import me.szumielxd.bungeechatcontrol.data.ChatPlayer;
import me.szumielxd.bungeechatcontrol.data.ChatReport;
import me.szumielxd.bungeechatcontrol.managers.ReportManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand extends Command {

	public ReportCommand(String name, String[] aliases) {
		super(name, "bungeechatcontrol.report", aliases);
	}

	@Override
	public void execute(CommandSender s, String[] args) {
		
		if(!(s instanceof ProxiedPlayer)) {
			s.sendMessage(new TextComponent("§cNot for console!"));
			return;
		}
		
		if(args.length < 2) {
			s.sendMessage(MiscUtil.fixedFormat(Config.REPORT_PREFIX+Config.REPORT_USAGE));
			return;
		}
		ProxiedPlayer accused = ProxyServer.getInstance().getPlayer(args[0]);
		if(accused == null) {
			s.sendMessage(MiscUtil.fixedFormat(Config.REPORT_PREFIX+Config.REPORT_USAGE));
			return;
		}
		ChatPlayer cp = ChatPlayer.getChatPlayer(accused.getUniqueId());
		if(cp == null) {
			s.sendMessage(MiscUtil.fixedFormat(Config.REPORT_PREFIX+Config.COMMAND_ERROR));
			return;
		}
		ChatReport cr = new ChatReport(cp, s.getName(), String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
		if(ReportManager.registerReport(cr)) {
			s.sendMessage(MiscUtil.fixedFormat(Config.REPORT_PREFIX+Config.REPORT_SUCCESSFULLY_REPORTED.replace("%accused%", cp.getName())));
		} else {
			s.sendMessage(MiscUtil.fixedFormat(Config.REPORT_PREFIX+Config.REPORT_ALREADY_REPORTED.replace("%accused%", cp.getName())));
		}
		
	}

}
