package me.szumielxd.bungeechatcontrol;

import me.szumielxd.bungeechatcontrol.command.MainCommand;
import me.szumielxd.bungeechatcontrol.command.ReportCommand;
import me.szumielxd.bungeechatcontrol.command.SubCommand;
import me.szumielxd.bungeechatcontrol.data.ChatPlayer;
import me.szumielxd.bungeechatcontrol.listener.ChatListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeChatControl extends Plugin {
	
	
	private static BungeeChatControl instance;
	private static MainCommand command;
	private static ReportCommand reportCommand;
	public static BungeeChatControl getinst() {
		return instance;
	}
	
	
	@Override
	public void onEnable() {
		
		if(instance == null) instance = this;
		Config.load();
		command = new MainCommand(Config.COMMAND, Config.ALIASES.toArray(new String[] {}));
		reportCommand = new ReportCommand(Config.REPORT_COMMAND, Config.REPORT_ALIASES.toArray(new String[] {}));
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatListener());
		ProxyServer.getInstance().getPluginManager().registerCommand(this, command);
		ProxyServer.getInstance().getPluginManager().registerCommand(this, reportCommand);
		ChatPlayer.load();
		
	}
	
	
	@Override
	public void onDisable() {
		ChatPlayer.unload();
		SubCommand.onDisable();
		ProxyServer.getInstance().getPluginManager().unregisterCommand(command);
		ProxyServer.getInstance().getPluginManager().unregisterCommand(reportCommand);
		ProxyServer.getInstance().getPluginManager().unregisterListeners(instance);
	}
	

}
