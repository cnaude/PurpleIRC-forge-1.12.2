/*
 * Copyright (C) 2014 cnaude
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cnaude.purpleirc.Commands;

import com.cnaude.purpleirc.PurpleBot;
import com.cnaude.purpleirc.PurpleIRC;
import java.io.File;

import com.cnaude.purpleirc.CommandSender;
import com.cnaude.purpleirc.Proxies.CommonProxy;
import net.minecraft.util.text.TextFormatting;

/**
 *
 * @author cnaude
 */
public class Load implements IRCCommandInterface {

    private final PurpleIRC plugin;
    CommonProxy proxy;
    private final String usage = "[bot]";
    private final String desc = "Load a bot file.";
    private final String name = "load";
    private final String fullUsage = TextFormatting.WHITE + "Usage: " + TextFormatting.GOLD + "/irc " + name + " " + usage;

    /**
     *
     * @param plugin
     * @param proxy
     */
    public Load(PurpleIRC plugin, CommonProxy proxy) {
        this.plugin = plugin;
        this.proxy = proxy;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            String bot = plugin.botify(args[1]);
            if (plugin.ircBots.containsKey(bot)) {
                sender.sendMessage(TextFormatting.RED + "Sorry that bot is already loaded. Try to unload it first.");
                return;
            }
            File file = new File(plugin.botsFolder, bot);
            if (file.exists()) {
                sender.sendMessage(TextFormatting.WHITE + "Loading " + bot + "...");
                plugin.ircBots.put(file.getName(), new PurpleBot(file, plugin, proxy));
                sender.sendMessage("Loaded bot: " + file.getName() + "[" + plugin.ircBots.get(file.getName()).botNick + "]");
            } else {
                sender.sendMessage(TextFormatting.RED + "No such bot file: " + TextFormatting.WHITE + bot);
            }
        } else {
            sender.sendMessage(fullUsage);
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String desc() {
        return desc;
    }

    @Override
    public String usage() {
        return usage;
    }
}
