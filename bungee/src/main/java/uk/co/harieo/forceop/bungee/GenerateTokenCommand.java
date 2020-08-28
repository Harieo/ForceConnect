package uk.co.harieo.forceop.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GenerateTokenCommand extends Command {

	private final ForceConnect plugin;

	public GenerateTokenCommand(ForceConnect plugin) {
		super("forceconnect", null, "fc");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "You may only do this from console!"));
		} else {
			if (args.length > 0 && args[0].equalsIgnoreCase("generate")) {
				plugin.generateToken();
			} else {
				sender.sendMessage(TextComponent.fromLegacyText(
						ChatColor.RED + "To prevent accidental regeneration, please use: /fc generate"));
			}
		}
	}

}
