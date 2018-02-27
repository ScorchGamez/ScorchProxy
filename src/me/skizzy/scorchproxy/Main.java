package me.skizzy.scorchproxy;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public void onDisable() {
		Bukkit.getConsoleSender()
				.sendMessage("[ScorchProxy] v" + getDescription().getVersion() + " is now disabled!");
	}

	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		saveConfig();
		reloadConfig();

		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		Bukkit.getConsoleSender()
				.sendMessage("[ScorchProxy] v" + getDescription().getVersion() + " is now enabled!");
		Bukkit.getConsoleSender()
				.sendMessage("[ScorchProxy] Permission bypass is set to: " + getConfig().getBoolean("Permissions-Bypass"));
		Bukkit.getConsoleSender()
				.sendMessage("[ScorchProxy] IP(s): " + getConfig().getStringList("IPs"));
		Bukkit.getConsoleSender()
				.sendMessage("[ScorchProxy] Port(s): " + getConfig().getIntegerList("Ports"));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		String ip = e.getHostname().toString().split(":")[0];
		int port = Integer.valueOf(e.getHostname().toString().split(":")[1]).intValue();

		List<String> ips = getConfig().getStringList("IPs");
		List<Integer> ports = getConfig().getIntegerList("Ports");
		if (getConfig().getBoolean("Only-Port")) {
			if (!ports.contains(Integer.valueOf(port))) {
				if (getConfig().getBoolean("Permissions-Bypass")) {
					if (!p.hasPermission("scorchproxy.bypass")) {
						e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Message").replaceAll("%newline%", "\n")));
					} else {
						e.allow();
					}
				} else {
					e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
							getConfig().getString("Message").replaceAll("%newline%", "\n")));
				}
			}
		} else if ((!ips.contains(ip)) || (!ports.contains(Integer.valueOf(port)))) {
			if (getConfig().getBoolean("Permissions-Bypass")) {
				if (!p.hasPermission("scorchproxy.bypass")) {
					e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
							getConfig().getString("Message").replaceAll("%newline%", "\n")));
				} else {
					e.allow();
				}
			} else {
				e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',
						getConfig().getString("Message").replaceAll("%newline%", "\n")));
			}
		}
	}
}