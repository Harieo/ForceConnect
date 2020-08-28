package uk.co.harieo.forceop.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ShieldCommand extends Command {

	private final ForceOP plugin;

	public ShieldCommand(ForceOP plugin) {
		super("shield");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "You may only do this from console!"));
		} else {
			plugin.generateToken();
		}
	}

}
