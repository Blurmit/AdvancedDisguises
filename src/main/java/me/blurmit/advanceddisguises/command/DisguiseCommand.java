package me.blurmit.advanceddisguises.command;

import me.blurmit.advanceddisguises.AdvancedDisguises;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class DisguiseCommand implements CommandExecutor {

    private final AdvancedDisguises plugin;

    public DisguiseCommand(AdvancedDisguises plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        plugin.getDisguiseManager().disguise(player, args[0]);
        return true;
    }

}
