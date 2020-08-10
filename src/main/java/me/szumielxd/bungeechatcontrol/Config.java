package me.szumielxd.bungeechatcontrol;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import me.szumielxd.bungeechatcontrol.command.ArgumentType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config {
	
	
	public static String PREFIX;
	public static String COMMAND = "bungeechatcontrol";
	public static String CONSOLE = "CONSOLE";
	public static List<String> ALIASES = new ArrayList<String>();
	////////////////////////
	public static String REPORT_PREFIX;
	public static String REPORT_COMMAND = "bungeechatreport";
	public static List<String> REPORT_ALIASES = new ArrayList<String>();
	
	/*LANGUAGE*/
	public static String MISC_ON;
	public static String MISC_OFF;
	public static String MISC_TRUE;
	public static String MISC_FALSE;
	////////////////////////
	public static String CHAT_DENY_MESSAGE;
	public static String CHAT_CLEAR_INFO;
	public static String CHAT_CLEAR_INFO_INCOGNITO;
	////////////////////////
	public static String REPORT_USAGE;
	public static String REPORT_ALREADY_REPORTED;
	public static String REPORT_SUCCESSFULLY_REPORTED;
	public static String REPORT_NOTIFICATION_TIMEOUT;
	public static String REPORT_NOTIFICATION_ACCEPTED;
	public static String REPORT_NOTIFICATION_REJECTED;
	public static String REPORT_NOTIFICATION_ADMIN_TIMEOUT;
	public static String REPORT_NOTIFICATION_ADMIN_ACCEPTED;
	public static String REPORT_NOTIFICATION_ADMIN_REJECTED;
	
	/*COMMANDS*/
	public static String COMMAND_ERROR;
	public static String COMMAND_PERMISSION_ERROR;
	public static String COMMAND_PARSE_ERROR;
	////////////////////////
	public static String COMMAND_LIST_DESCRIPTION;
	public static String[] COMMAND_LIST_ALIASES = new String[] {};
	public static String COMMAND_LIST_START;
	public static String COMMAND_LIST_SEPARATOR;
	public static String COMMAND_LIST_ENABLED;
	public static String COMMAND_LIST_DISABLED;
	public static String COMMAND_LIST_CLICK;
	public static List<String> COMMAND_LIST_HOVER = new ArrayList<String>();
	////////////////////////
	public static String COMMAND_RELOAD_DESCRIPTION;
	public static String[] COMMAND_RELOAD_ALIASES = new String[] {};
	public static String COMMAND_RELOAD_COMPLETED;
	public static String COMMAND_RELOAD_FAILED;
	////////////////////////
	public static String COMMAND_HELP_DESCRIPTION;
	public static String[] COMMAND_HELP_ALIASES = new String[] {};
	public static String COMMAND_HELP_HEADER;
	public static String COMMAND_HELP_FOOTER;
	public static String COMMAND_HELP_FORMAT;
	public static String COMMAND_HELP_CLICK;
	public static List<String> COMMAND_HELP_HOVER = new ArrayList<String>();
	////////////////////////
	public static String COMMAND_CLEAR_DESCRIPTION;
	public static String[] COMMAND_CLEAR_ALIASES = new String[] {};
	public static String COMMAND_CLEAR_DONE;
	public static String COMMAND_CLEAR_DONE_GLOBAL;
	////////////////////////
	public static String COMMAND_TOGGLE_DESCRIPTION;
	public static String[] COMMAND_TOGGLE_ALIASES = new String[] {};
	public static String COMMAND_TOGGLE_ON_INFO;
	public static String COMMAND_TOGGLE_ON_INFO_INCOGNITO;
	public static String COMMAND_TOGGLE_OFF_INFO;
	public static String COMMAND_TOGGLE_OFF_INFO_INCOGNITO;
	public static String COMMAND_TOGGLE_ON_DONE;
	public static String COMMAND_TOGGLE_ON_DONE_GLOBAL;
	public static String COMMAND_TOGGLE_OFF_DONE;
	public static String COMMAND_TOGGLE_OFF_DONE_GLOBAL;
	public static String COMMAND_TOGGLE_ON_ALREADY;
	public static String COMMAND_TOGGLE_OFF_ALREADY;
	////////////////////////
	public static String COMMAND_REPORT_DESCRIPTION;
	public static String[] COMMAND_REPORT_ALIASES = new String[] {};
	public static String COMMAND_REPORT_NEW_TEXT;
	public static String COMMAND_REPORT_NEW_HOVER_HEADER;
	public static String COMMAND_REPORT_NEW_HOVER_FORMAT;
	public static String COMMAND_REPORT_NEW_HOVER_FOOTER;
	public static String COMMAND_REPORT_NEW_CLICK;
	public static String COMMAND_REPORT_NEW_ACCEPT;
	public static String COMMAND_REPORT_NEW_ACCEPT_HOVER;
	public static String COMMAND_REPORT_NEW_REJECT;
	public static String COMMAND_REPORT_NEW_REJECT_HOVER;
	public static String COMMAND_REPORT_INVALID_ID;
	
	public static String COMMAND_REPORT_LIST_HEADER;
	public static String COMMAND_REPORT_LIST_FOOTER;
	public static String COMMAND_REPORT_LIST_FORMAT;
	public static String COMMAND_REPORT_LIST_HOVER_HEADER;
	public static String COMMAND_REPORT_LIST_HOVER_FORMAT;
	public static String COMMAND_REPORT_LIST_HOVER_FOOTER;
	public static String COMMAND_REPORT_LIST_CLICK;
	public static String COMMAND_REPORT_LIST_ACCEPT;
	public static String COMMAND_REPORT_LIST_ACCEPT_HOVER;
	public static String COMMAND_REPORT_LIST_REJECT;
	public static String COMMAND_REPORT_LIST_REJECT_HOVER;
	
	/*SETTINGS*/
	public static Boolean SETTINGS_USE_LUCKPERMS_DISPLAYNAME;
	public static Integer REPORT_TIMEOUT;
	
	
	public static boolean load() {
		
		File dir = BungeeChatControl.getinst().getDataFolder();
		if(!dir.exists()) dir.mkdirs();
		try {
			
			File f = new File(dir, "config.yml");
			if(!f.exists()) {
				InputStream is = BungeeChatControl.getinst().getResourceAsStream("config.yml");
				Files.copy(is, f.toPath());
				is.close();
			}
			ConfigurationProvider prov = ConfigurationProvider.getProvider(YamlConfiguration.class);
			Configuration cfg = prov.load(f);
			InputStream is = BungeeChatControl.getinst().getResourceAsStream("config.yml");
			Configuration alt = prov.load(is);
			is.close();
			
			
			/*MAIN*/
			PREFIX = color(cfg, alt, "prefix");
			COMMAND = safeString(cfg, alt, "command");
			CONSOLE = safeString(cfg, alt, "console");
			ALIASES = safeStringList(cfg, alt, "aliases");
			////////////////////////
			REPORT_PREFIX = color(cfg, alt, "report-prefix");
			REPORT_COMMAND = safeString(cfg, alt, "report-command");
			REPORT_ALIASES = safeStringList(cfg, alt, "report-aliases");
			
			
			/*LANGUAGE*/
			{
				Configuration sec = cfg.getSection("language");
				Configuration altsec = alt.getSection("language");
				MISC_ON = color(sec, altsec, "misc-on");
				MISC_OFF = color(sec, altsec, "misc-off");
				MISC_TRUE = color(sec, altsec, "misc-true");
				MISC_FALSE = color(sec, altsec, "misc-false");
				////////////////////////
				CHAT_DENY_MESSAGE = color(sec, altsec, "chat-deny-message");
				CHAT_CLEAR_INFO = color(sec, altsec, "chat-clear-info");
				CHAT_CLEAR_INFO_INCOGNITO = color(sec, altsec, "chat-clear-info-incognito");
				////////////////////////
				REPORT_USAGE = color(sec, altsec, "report.usage");
				REPORT_ALREADY_REPORTED = color(sec, altsec, "report.already-reported");
				REPORT_SUCCESSFULLY_REPORTED = color(sec, altsec, "report.successfully-reported");
				REPORT_NOTIFICATION_TIMEOUT = color(sec, altsec, "report.notification-timeout");
				REPORT_NOTIFICATION_ACCEPTED = color(sec, altsec, "report.notification-accepted");
				REPORT_NOTIFICATION_REJECTED = color(sec, altsec, "report.notification-rejected");
				REPORT_NOTIFICATION_ADMIN_TIMEOUT = color(sec, altsec, "report.notification-admin-timeout");
				REPORT_NOTIFICATION_ADMIN_ACCEPTED = color(sec, altsec, "report.notification-admin-accepted");
				REPORT_NOTIFICATION_ADMIN_REJECTED = color(sec, altsec, "report.notification-admin-rejected");
			}
			
			
			/*COMMANDS*/
			{
				Configuration sec = cfg.getSection("commands");
				Configuration altsec = alt.getSection("commands");
				for(ArgumentType arg : ArgumentType.values()) {
					arg.setDisplay(safeString(sec, altsec, "arg-types."+arg.toString().toLowerCase()));
				}
				COMMAND_ERROR = color(sec, altsec, "error");
				COMMAND_PERMISSION_ERROR = color(sec, altsec, "permission-error");
				COMMAND_PARSE_ERROR = color(sec, altsec, "parse-error");
				////////////////////////
				COMMAND_LIST_DESCRIPTION = color(sec, altsec, "list.description");
				COMMAND_LIST_ALIASES = safeStringList(sec, altsec, "list.aliases").toArray(new String[] {});
				COMMAND_LIST_START = color(sec, altsec, "list.start");
				COMMAND_LIST_SEPARATOR = color(sec, altsec, "list.separator");
				COMMAND_LIST_ENABLED = color(sec, altsec, "list.enabled");
				COMMAND_LIST_DISABLED = color(sec, altsec, "list.disabled");
				COMMAND_LIST_CLICK = color(sec, altsec, "list.click");
				COMMAND_LIST_HOVER = colorList(sec, altsec, "list.hover");
				////////////////////////
				COMMAND_RELOAD_DESCRIPTION = color(sec, altsec, "reload.description");
				COMMAND_RELOAD_ALIASES = safeStringList(sec, altsec, "reload.aliases").toArray(new String[] {});
				COMMAND_RELOAD_COMPLETED = color(sec, altsec, "reload.completed");
				COMMAND_RELOAD_FAILED = color(sec, altsec, "reload.failed");
				////////////////////////
				COMMAND_HELP_DESCRIPTION = color(sec, altsec, "help.description");
				COMMAND_HELP_ALIASES = safeStringList(sec, altsec, "help.aliases").toArray(new String[] {});
				COMMAND_HELP_HEADER = color(sec, altsec, "help.header");
				COMMAND_HELP_FOOTER = color(sec, altsec, "help.footer");
				COMMAND_HELP_FORMAT = color(sec, altsec, "help.format");
				COMMAND_HELP_CLICK = color(sec, altsec, "help.click");
				COMMAND_HELP_HOVER = colorList(sec, altsec, "help.hover");
				////////////////////////
				COMMAND_CLEAR_DESCRIPTION = color(sec, altsec, "clear.description");
				COMMAND_CLEAR_ALIASES = safeStringList(sec, altsec, "clear.aliases").toArray(new String[] {});
				COMMAND_CLEAR_DONE = color(sec, altsec, "clear.done");
				COMMAND_CLEAR_DONE_GLOBAL = color(sec, altsec, "clear.done-global");
				////////////////////////
				COMMAND_TOGGLE_DESCRIPTION = color(sec, altsec, "toggle.description");
				COMMAND_TOGGLE_ALIASES = safeStringList(sec, altsec, "toggle.aliases").toArray(new String[] {});
				COMMAND_TOGGLE_ON_INFO = color(sec, altsec, "toggle.info-on");
				COMMAND_TOGGLE_ON_INFO_INCOGNITO = color(sec, altsec, "toggle.info-on-incognito");
				COMMAND_TOGGLE_OFF_INFO = color(sec, altsec, "toggle.info-off");
				COMMAND_TOGGLE_OFF_INFO_INCOGNITO = color(sec, altsec, "toggle.info-off-incognito");
				COMMAND_TOGGLE_ON_DONE = color(sec, altsec, "toggle.done-on");
				COMMAND_TOGGLE_ON_DONE_GLOBAL = color(sec, altsec, "toggle.done-on-global");
				COMMAND_TOGGLE_OFF_DONE = color(sec, altsec, "toggle.done-off");
				COMMAND_TOGGLE_OFF_DONE_GLOBAL = color(sec, altsec, "toggle.done-off-global");
				COMMAND_TOGGLE_ON_ALREADY = color(sec, altsec, "toggle.already-on");
				COMMAND_TOGGLE_OFF_ALREADY = color(sec, altsec, "toggle.already-off");
				////////////////////////
				COMMAND_REPORT_DESCRIPTION = color(sec, altsec, "report.description");
				COMMAND_REPORT_ALIASES = safeStringList(sec, altsec, "report.aliases").toArray(new String[] {});
				COMMAND_REPORT_NEW_TEXT = color(sec, altsec, "report.notify-new.text");
				COMMAND_REPORT_NEW_HOVER_HEADER = color(sec, altsec, "report.notify-new.hover-header");
				COMMAND_REPORT_NEW_HOVER_FORMAT = color(sec, altsec, "report.notify-new.hover-message-format");
				COMMAND_REPORT_NEW_HOVER_FOOTER = color(sec, altsec, "report.notify-new.hover-footer");
				COMMAND_REPORT_NEW_CLICK = safeString(sec, altsec, "report.notify-new.click");
				COMMAND_REPORT_NEW_ACCEPT = color(sec, altsec, "report.notify-new.accept");
				COMMAND_REPORT_NEW_ACCEPT_HOVER = color(sec, altsec, "report.notify-new.accept-hover");
				COMMAND_REPORT_NEW_REJECT = color(sec, altsec, "report.notify-new.reject");
				COMMAND_REPORT_NEW_REJECT_HOVER = color(sec, altsec, "report.notify-new.reject-hover");
				COMMAND_REPORT_INVALID_ID = color(sec, altsec, "report.invalid-id");
				
				COMMAND_REPORT_LIST_HEADER = color(sec, altsec, "report.list.header");
				COMMAND_REPORT_LIST_FOOTER = color(sec, altsec, "report.list.footer");
				COMMAND_REPORT_LIST_FORMAT = color(sec, altsec, "report.list.format");
				COMMAND_REPORT_LIST_HOVER_HEADER = color(sec, altsec, "report.list.hover-header");
				COMMAND_REPORT_LIST_HOVER_FORMAT = color(sec, altsec, "report.list.hover-message-format");
				COMMAND_REPORT_LIST_HOVER_FOOTER = color(sec, altsec, "report.list.hover-footer");
				COMMAND_REPORT_LIST_CLICK = safeString(sec, altsec, "report.list.click");
				COMMAND_REPORT_LIST_ACCEPT = color(sec, altsec, "report.list.accept");
				COMMAND_REPORT_LIST_ACCEPT_HOVER = color(sec, altsec, "report.list.accept-hover");
				COMMAND_REPORT_LIST_REJECT = color(sec, altsec, "report.list.reject");
				COMMAND_REPORT_LIST_REJECT_HOVER = color(sec, altsec, "report.list.reject-hover");
				
			}
			
			
			/*SETTINGS*/
			{
				Configuration sec = cfg.getSection("settings");
				Configuration altsec = alt.getSection("settings");
				SETTINGS_USE_LUCKPERMS_DISPLAYNAME = safeBoolean(sec, altsec, "use-luckperms-displayname");
				REPORT_TIMEOUT = safeInt(sec, altsec, "report-timeout", 120);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	
	private static Boolean safeBoolean(Configuration cfg, Configuration alt, String str) {
		return cfg.getBoolean(str, alt.getBoolean(str, false));
	}
	private static String safeString(Configuration cfg, Configuration alt, String str) {
		return cfg.getString(str, alt.getString(str, ""));
	}
	@SuppressWarnings("unused")
	private static Integer safeInt(Configuration cfg, Configuration alt, String str) {
		return cfg.getInt(str, alt.getInt(str, 0));
	}
	private static Integer safeInt(Configuration cfg, Configuration alt, String str, Integer defaultVal) {
		return cfg.getInt(str, alt.getInt(str, defaultVal));
	}
	private static List<String> safeStringList(Configuration cfg, Configuration alt, String str) {
		List<String> list = cfg.getStringList(str);
		if(list != null) return list;
		list = alt.getStringList(str);
		if(list != null) return list;
		return new ArrayList<String>();
	}
	
	
	private static String color(Configuration cfg, Configuration alt, String str) {
		return ChatColor.translateAlternateColorCodes('&', safeString(cfg, alt, str));
	}
	private static List<String> colorList(Configuration cfg, Configuration alt, String str) {
		List<String> list = new ArrayList<String>();
		List<String> uncolored = safeStringList(cfg, alt, str);
		if(!uncolored.isEmpty()) uncolored.forEach(s -> list.add(ChatColor.translateAlternateColorCodes('&', s)));
		return list;
	}
	

}
