package com.hindustries.hchat;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class HChatListener implements Listener {
    private final HChatPlugin plugin;

    public HChatListener(HChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage().toLowerCase();

        for (String swearWord : plugin.getSwearWords()) {
            if (message.contains(swearWord.toLowerCase())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Please watch your language!");
                return;
            }
        }
    }
}
