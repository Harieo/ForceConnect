package uk.co.harieo.forceconnect.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * A command which allows a user with the correct permissions to call {@link ForceConnect#generateToken()}
 */
public class ForceConnectCommand implements SimpleCommand {

    private final ForceConnect plugin;

    public ForceConnectCommand(ForceConnect plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length > 0 && args[0].equalsIgnoreCase("generate")) {
            if (source.hasPermission("forceconnect.generate")) {
                plugin.generateToken();
            } else {
                source.sendMessage(Component.text("You do not have permission to do that.").color(NamedTextColor.RED));
            }
        } else {
            source.sendMessage(Component.text("Connections secured with ").color(NamedTextColor.GRAY)
                    .append(Component.text("ForceConnect ").color(NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text("by ").color(NamedTextColor.GRAY))
                    .append(Component.text("Harieo").color(NamedTextColor.BLUE)));
        }
    }

}
