package uk.co.harieo.forceop.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final ForceConnect plugin;

    public ReloadCommand(@NotNull ForceConnect plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        plugin.loadHash();
        sender.sendMessage(ChatColor.GREEN + "ForceConnect has been reloaded successfully.");
        return false;
    }

}
