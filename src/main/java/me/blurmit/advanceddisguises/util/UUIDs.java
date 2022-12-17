package me.blurmit.advanceddisguises.util;

import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class UUIDs {

    private static final Map<String, UUID> cachedPlayers;

    static {
        cachedPlayers = new HashMap<>();

        Bukkit.getScheduler().runTaskTimerAsynchronously(Bukkit.getPluginManager().getPlugins()[0], cachedPlayers::clear, 0L, 30 * 60 * 20L);
    }

    public static void retrieveUUID(String username, Consumer<UUID> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugins()[0], () -> {
            if (!cachedPlayers.containsKey(username)) {
                try {
                    URL apiServer = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
                    InputStreamReader uuidReader = new InputStreamReader(apiServer.openStream());
                    String uuidString = new JsonParser().parse(uuidReader).getAsJsonObject().get("id").getAsString();
                    UUID uuid = UUID.fromString(uuidString.replaceFirst(
                            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                            "$1-$2-$3-$4-$5"
                    ));

                    consumer.accept(uuid);
                    cachedPlayers.put(synchronouslyGetNameFromUUID(uuid), uuid);
                } catch (IOException | IllegalStateException e) {
                    consumer.accept(null);
                }
            } else {
                consumer.accept(cachedPlayers.get(username));
            }
        });
    }

    public static UUID synchronouslyRetrieveUUID(String username) {
        if (!cachedPlayers.containsKey(username)) {
            try {
                URL apiServer = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
                InputStreamReader uuidReader = new InputStreamReader(apiServer.openStream());
                String uuidString = new JsonParser().parse(uuidReader).getAsJsonObject().get("id").getAsString();
                UUID uuid = UUID.fromString(uuidString.replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                        "$1-$2-$3-$4-$5"
                ));

                cachedPlayers.put(synchronouslyGetNameFromUUID(uuid), uuid);
                return uuid;
            } catch (IOException | IllegalStateException e) {
                return null;
            }
        } else {
            return cachedPlayers.get(username);
        }
    }

    public static String synchronouslyGetNameFromUUID(UUID uuid) {
        if (!cachedPlayers.containsValue(uuid)) {
            try {
                String uuidString = uuid.toString().replace("-", "");
                URL sessionServer = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidString);
                InputStreamReader nameReader = new InputStreamReader(sessionServer.openStream());

                String username = new JsonParser().parse(nameReader).getAsJsonObject().get("name").getAsString();
                cachedPlayers.put(username, uuid);

                return username;
            } catch (IOException | IllegalStateException e) {
                return null;
            }
        } else {
            return cachedPlayers.keySet().stream().filter(username -> cachedPlayers.get(username).equals(uuid)).findFirst().orElse(null);
        }
    }

}
