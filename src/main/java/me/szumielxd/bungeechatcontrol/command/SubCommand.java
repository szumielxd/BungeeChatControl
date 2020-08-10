package me.szumielxd.bungeechatcontrol.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.szumielxd.bungeechatcontrol.Pair;
import net.md_5.bungee.api.CommandSender;

public abstract class SubCommand {
	
	
	private static Map<String, SubCommand> commands = new HashMap<String, SubCommand>();
	public static Map<String, SubCommand> getSubCommands() {
		return new HashMap<String, SubCommand>(commands);
	}
	
	
	private String name;
	private String[] aliases;
	private String description;
	protected String[] staticArgs = new String[0];
	public abstract List<Pair<ArgumentType, Boolean>> getArguments();
	
	
	protected SubCommand(String name, String descr, String... aliases) {
		if(name == null) throw new NullPointerException("Name can't be null");
		if(descr == null) descr = "";
		if(aliases == null) aliases = new String[] {};
		if(commands.containsKey(name.toLowerCase())) throw new IllegalArgumentException("SubCommand with name '" + name + "' already exists");
		this.name = name.toLowerCase();
		this.description= descr; 
		String[] arr = new String[aliases.length];
		for(int i = 0; i < aliases.length; i++) {
			arr[i] = aliases[i].toLowerCase();
		}
		this.aliases = arr.clone();
		SubCommand.commands.put(this.name, this);
	}
	
	public List<Pair<ArgumentType, Object>> parseValues(String... args){
		List<Pair<ArgumentType, Object>> list = new ArrayList<Pair<ArgumentType, Object>>();
		List<Pair<ArgumentType, Boolean>> patterns = this.getArguments();
		int i = 0;
		int statics = 0;
		if(patterns == null || patterns.isEmpty()) return list;
		for(Pair<ArgumentType, Boolean> para : patterns) {
			String arg = i<args.length ? args[i] : null;
			Object obj = para.getLeft().parseArg(arg);
			if(para.getLeft().equals(ArgumentType.STATIC)) {
				if(arg != null && !Arrays.asList(this.staticArgs[statics++].toLowerCase().split(" ")).contains(arg.toLowerCase())) obj = null;
			}
			if(obj == null) {
				if(para.getRight()) throw new IllegalArgumentException(i+","+para.getLeft());
			}
			else i++;
			list.add(new Pair<>(para.getLeft(), obj));
		}
		return list;
	}
	
	public String getDescription() {
		return new String(this.description);
	}
	
	public String getName() {	
		return new String(this.name);
	}
	
	public String[] getAliases() {
		return this.aliases.clone();
	}
	
	public boolean hasPermission(CommandSender s) {
		return s.hasPermission("bungeechatcontrol.command."+this.name);
	}
	
	public String getPermission() {
		return "bungeechatcontrol.command."+this.name;
	}
	
	public boolean isRegistered() {
		return SubCommand.commands.containsKey(this.name);
	}
	
	public boolean match(String name) {
		name = name.toLowerCase();
		if(this.name.equals(name)) return true;
		for(int i = 0; i < this.aliases.length; i++) {
			if(this.aliases[i].equals(name)) return true;
		}
		return false;
	}
	
	public String getArgsString() {
		List<Pair<ArgumentType, Boolean>> args = this.getArguments();
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		if(!args.isEmpty()) for(Pair<ArgumentType, Boolean> param : args) {
			sb.append(" ");
			String display = param.getLeft().getDisplay();
			if(param.getLeft().equals(ArgumentType.STATIC)) {
				display = staticArgs.length>i ? staticArgs[i++].replace(" ", "|") : "PARSE_ERROR";
				sb.append(display);
			} else {
				sb.append((param.getRight()? "<" : "[") + display + (param.getRight()? ">" : "]"));
			}
		};
		return sb.toString();
	}
	
	public abstract boolean execute(CommandSender s, List<Pair<ArgumentType, Object>> args);
	
	public static void onDisable() {
		commands.clear();
	}

}
