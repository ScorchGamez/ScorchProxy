package me.skizzy.scorchproxy;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("[ScorchProxy] v" + getDescription().getVersion() + " is now disabled!");
	}

	public void onEnable() {
		getConfig().options().copyDefaults(true);
		// getConfig().addDefault("config-version", Integer.valueOf(1));
		saveDefaultConfig();
		saveConfig();
		reloadConfig();

		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		Bukkit.getConsoleSender().sendMessage("[ScorchProxy] v" + getDescription().getVersion() + " is now enabled!");
		Bukkit.getConsoleSender()
				.sendMessage("[ScorchProxy] Detected config version: " + getConfig().get("config-version"));
		Bukkit.getConsoleSender().sendMessage(
				"[ScorchProxy] Permission bypass is set to: " + getConfig().getBoolean("Permissions-Bypass"));
		Bukkit.getConsoleSender().sendMessage("[ScorchProxy] IP(s): " + getConfig().getStringList("IPs"));
		Bukkit.getConsoleSender().sendMessage("[ScorchProxy] Port(s): " + getConfig().getIntegerList("Ports"));
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player player = (Player) sender;

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to issue this command.");
			sender.sendMessage(ChatColor.RED + "Look in the config.yml");
			return true;
		}

		if (((command.getName().equalsIgnoreCase("scorchproxy")) || (command.getName().equalsIgnoreCase("sp")))
				&& player.hasPermission("scorchproxy.cmd")) {
			if (args.length == 0) {
				{
					player.sendMessage("");
					player.sendMessage(ChatColor.GOLD + " Use one of the subcommands below");
					player.sendMessage(
							ChatColor.AQUA + " /scorchproxy ips: " + ChatColor.DARK_AQUA + "Valid entry IPs");
					player.sendMessage(
							ChatColor.AQUA + " /scorchproxy ports: " + ChatColor.DARK_AQUA + "Valid entry ports");
					player.sendMessage(ChatColor.AQUA + " /scorchproxy version: " + ChatColor.DARK_AQUA
							+ "Config and plugin version");
					player.sendMessage(ChatColor.AQUA + " /scorchproxy reload: " + ChatColor.DARK_AQUA
							+ "Reload the plugin config");
					player.sendMessage("");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 1);
					return true;
				}
			} else if (args[0].equalsIgnoreCase("ips") && player.hasPermission("scorchproxy.cmd.ips")) {
				{
					player.sendMessage("");
					player.sendMessage(ChatColor.GOLD + " IP addresses in config:");
					player.sendMessage(ChatColor.AQUA + " " + getConfig().getStringList("IPs"));
					player.sendMessage("");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 1);
					return true;
				}
			} else if (args[0].equalsIgnoreCase("ports") && player.hasPermission("scorchproxy.cmd.ports")) {
				{
					player.sendMessage("");
					player.sendMessage(ChatColor.GOLD + " Ports in config:");
					player.sendMessage(ChatColor.AQUA + " " + getConfig().getStringList("Ports"));
					player.sendMessage("");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 1);
					return true;
				}
			} else if (args[0].equalsIgnoreCase("version") && player.hasPermission("scorchproxy.cmd.version")) {
				{
					player.sendMessage("");
					player.sendMessage(
							ChatColor.AQUA + " Plugin version: " + ChatColor.DARK_AQUA + getDescription().getVersion());
					player.sendMessage(ChatColor.AQUA + " Configuration version: " + ChatColor.DARK_AQUA
							+ getConfig().get("config-version"));
					player.sendMessage("");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 1);
					return true;
				}
			} else if (args[0].equalsIgnoreCase("reload") && player.hasPermission("scorchproxy.cmd.reload")) {
				{
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 10, 1);
					player.sendMessage("");
					player.sendMessage(ChatColor.RED + " It is " + ChatColor.BOLD + "HIGHLY" + ChatColor.RED
							+ " recommended that you restart your server rather than reloading.");
					player.sendMessage(ChatColor.AQUA + " Reloading config.yml (v" + ChatColor.DARK_AQUA
							+ getConfig().get("config-version") + ChatColor.AQUA + ")");
					this.reloadConfig();
					player.sendMessage(ChatColor.AQUA + " The config.yml (v" + ChatColor.DARK_AQUA
							+ getConfig().get("config-version") + ChatColor.AQUA + ") has been reloaded.");
					player.sendMessage("");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 1);
					return true;
				}
			} else if (args.length >= 1) {
				{
					player.sendMessage(ChatColor.RED + " Invalid arguments!");
					player.sendMessage(ChatColor.GRAY + " Check " + ChatColor.AQUA + "/scorchproxy" + ChatColor.GRAY
							+ " for valid commands.");
					player.sendMessage("");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 10, 1);
				}
			}
		} else if (((command.getName().equalsIgnoreCase("scorchproxy")) || (command.getName().equalsIgnoreCase("sp")))
				&& (!player.hasPermission("scorchproxy.cmd") || !player.hasPermission("scorchproxy.cmd.ips")
						|| !player.hasPermission("scorchproxy.cmd.ports")
						|| !player.hasPermission("scorchproxy.cmd.version")
						|| !player.hasPermission("scorchproxy.cmd.reload"))) {
			player.sendMessage(getConfig().getString("No-Permission").replaceAll("&", "§").replaceAll("%player%",
					player.getName()));
		}
		return false;
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