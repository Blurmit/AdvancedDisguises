package me.blurmit.advanceddisguises;

import lombok.Getter;
import me.blurmit.advanceddisguises.command.DisguiseCommand;
import me.blurmit.advanceddisguises.disguises.DisguiseManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedDisguises extends JavaPlugin {

    @Getter
    private DisguiseManager disguiseManager;

    @Override
    public void onEnable() {
        getLogger().info("Loading disguises...");
        this.disguiseManager = new DisguiseManager(this);

        getLogger().info("Registering commands...");
        getCommand("disguise").setExecutor(new DisguiseCommand(this));

        getLogger().info("AdvancedDisguises has been successfully enabled.");
    }

}
