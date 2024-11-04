package com.hindustries.hchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HChatPlugin extends JavaPlugin {
    private List<String> swearWords;

    @Override
    public void onEnable() {
        loadSwearWords();
        getServer().getPluginManager().registerEvents(new HChatListener(this), this);
        getLogger().info("H-CHAT loaded successfully!");
    }

    @Override
    public void onDisable() {
        saveSwearWords();
        getLogger().info("H-CHAT disabled.");
    }

    public List<String> getSwearWords() {
        return swearWords;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("filterhelp")) {
            sender.sendMessage(ChatColor.AQUA + "H-CHAT Commands:");
            sender.sendMessage(ChatColor.YELLOW + "/addword <word>" + ChatColor.WHITE + " - Adds a word to the filter (Admins only)");
            sender.sendMessage(ChatColor.YELLOW + "/removeword <word>" + ChatColor.WHITE + " - Removes a word from the filter (Admins only)");
            sender.sendMessage(ChatColor.YELLOW + "/wordlist" + ChatColor.WHITE + " - Shows the list of banned words.");
            sender.sendMessage(ChatColor.YELLOW + "/filterhelp" + ChatColor.WHITE + " - Shows this help message.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("addword")) {
            if (sender instanceof Player && !sender.hasPermission("hchat.addword")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to add words.");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /addword <word>");
                return true;
            }
            String word = args[0].toLowerCase();
            swearWords.add(word);
            sender.sendMessage(ChatColor.GREEN + "Word '" + word + "' added to the filter.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("removeword")) {
            if (sender instanceof Player && !sender.hasPermission("hchat.removeword")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to remove words.");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /removeword <word>");
                return true;
            }
            String word = args[0].toLowerCase();
            if (swearWords.remove(word)) {
                sender.sendMessage(ChatColor.GREEN + "Word '" + word + "' removed from the filter.");
            } else {
                sender.sendMessage(ChatColor.RED + "Word '" + word + "' was not found in the filter.");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("wordlist")) {
            if (sender instanceof Player && !sender.hasPermission("hchat.wordlist")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to view the word list.");
                return true;
            }
            sender.sendMessage(ChatColor.AQUA + "Filtered Words:");
            if (swearWords.isEmpty()) {
                sender.sendMessage(ChatColor.YELLOW + "No words currently in the filter.");
            } else {
                for (String word : swearWords) {
                    sender.sendMessage(ChatColor.YELLOW + "- " + word);
                }
            }
            return true;
        }

        return false;
    }

    private void loadSwearWords() {
        swearWords = new ArrayList<>();
        try {
            File file = new File(getDataFolder(), "swearwords.txt");
            if (file.exists()) {
                swearWords = Files.readAllLines(Paths.get(file.getPath()));
            }
        } catch (IOException e) {
            getLogger().severe("Could not load swear words file: " + e.getMessage());
        }
    }

    private void saveSwearWords() {
        try {
            File file = new File(getDataFolder(), "swearwords.txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            Files.write(file.toPath(), swearWords);
        } catch (IOException e) {
            getLogger().severe("Could not save swear words file: " + e.getMessage());
        }
    }
}
