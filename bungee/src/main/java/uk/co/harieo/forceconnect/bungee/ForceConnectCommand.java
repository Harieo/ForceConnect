package uk.co.harieo.forceconnect.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ForceConnectCommand extends Command {

	private final ForceConnect plugin;

	public ForceConnectCommand(ForceConnect plugin) {
		super("forceconnect", null, "fc");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("generate")) {
			if (sender.hasPermission("forceconnect.generate")) {
				plugin.generateToken();
			} else {
				sender.sendMessage(
						TextComponent.fromLegacyText(ChatColor.RED + "You do not have permission to do that!"));
			}
		} else {
			sender.sendMessage(TextComponent.fromLegacyText(
					ChatColor.GRAY + "Connections secured with " + ChatColor.LIGHT_PURPLE + "ForceConnect " + ChatColor.GRAY + "by "
							+ ChatColor.BLUE + "Harieo"));
		}
	}

}
