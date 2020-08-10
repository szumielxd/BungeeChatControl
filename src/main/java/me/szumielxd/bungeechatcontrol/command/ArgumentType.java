package me.szumielxd.bungeechatcontrol.command;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Collection;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum ArgumentType {
	
	
	TEXT(String.class),
	NUMBER(Double.class),
	SERVER(String.class),
	BOOLEAN(Boolean.class),
	INTEGER(Integer.class),
	STATIC(String.class);
	
	
	private Class<?> type;
	private String display;
	
	ArgumentType(Class<?> cl) {
		this.type = cl;
	}
	
	public Class<?> getType(){
		return this.type;
	}
	
	public String getDisplay() {
		return new String(this.display);
	}
	
	public void setDisplay(String name) {
		this.display = name;
	}
	
	public Object parseArg(String arg) {
		switch(this) {
			case BOOLEAN: {
				if(arg == null) return null;
				if(Arrays.asList(new String[] {"yes", "y", "true", "t", "on", "allow"}).contains(arg.toLowerCase())) return true;
				if(Arrays.asList(new String[] {"no", "n", "false", "f", "off", "deny"}).contains(arg.toLowerCase())) return false;
				return null;
			}
			case INTEGER: {
				return Integer.parseInt(arg);
			}
			case NUMBER: {
				return Double.parseDouble(arg);
			}
			case SERVER: {
				ServerInfo info;
				if("*".equals(arg)) info = new ServerInfo() {
					@Override
					public boolean canAccess(CommandSender arg0) {
						return true;
					}
					@Override
					public InetSocketAddress getAddress() {
						return null;
					}
					@Override
					public String getMotd() {
						return null;
					}
					@Override
					public String getName() {
						return "*";
					}
					@Override
					public String getPermission() {
						return null;
					}
					@Override
					public Collection<ProxiedPlayer> getPlayers() {
						return ProxyServer.getInstance().getPlayers();
					}
					@Override
					public SocketAddress getSocketAddress() {
						return null;
					}
					@Override
					public boolean isRestricted() {
						return false;
					}
					@Override
					public void ping(Callback<ServerPing> arg0) {
						throw new UnsupportedOperationException();
					}
					@Override
					public void sendData(String arg0, byte[] arg1) {
						throw new UnsupportedOperationException();
					}
					@Override
					public boolean sendData(String arg0, byte[] arg1, boolean arg2) {
						throw new UnsupportedOperationException();
					}
					
				};
				else info = ProxyServer.getInstance().getServerInfo(arg);
				return info;
			}
			case TEXT: {
				return arg;
			}
			case STATIC: {
				return arg;
			}
			default: {
				return null;
			}
		}
	}
	

}
