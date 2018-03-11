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

import com.cnaude.purpleirc.PurpleIRC;

import com.cnaude.purpleirc.CommandSender;
import com.cnaude.purpleirc.Proxies.CommonProxy;
import net.minecraft.util.text.TextFormatting;

/**
 *
 * @author cnaude
 */
public class Help implements IRCCommandInterface {

    private final PurpleIRC plugin;
    private final CommonProxy proxy;
    private final String usage = "([command])";
    private final String desc = "Display help on a specific command or list all commands.";
    private final String name = "help";

    /**
     *
     * @param plugin
     * @param proxy
     */
    public Help(PurpleIRC plugin, CommonProxy proxy) {
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
            String s = args[1];
            if (proxy.getCommandHandlers().commands.containsKey(s)) {
                sender.sendMessage(helpStringBuilder(
                        proxy.getCommandHandlers().commands.get(s).name(),
                        proxy.getCommandHandlers().commands.get(s).desc(),
                        proxy.getCommandHandlers().commands.get(s).usage()));
                return;
            } else {
                sender.sendMessage(TextFormatting.RED + "Invalid sub command: "
                        + TextFormatting.WHITE + s);
                return;
            }
        }
        sender.sendMessage(plugin.colorConverter.translateAlternateColorCodes('&',
                "&5-----[  &fPurpleIRC&5 - &f" + plugin.getDescription().getVersion() + "&5 ]-----"));
        for (String s : proxy.getCommandHandlers().sortedCommands) {
            if (proxy.getCommandHandlers().commands.containsKey(s)) {
                sender.sendMessage(helpStringBuilder(
                        proxy.getCommandHandlers().commands.get(s).name(),
                        proxy.getCommandHandlers().commands.get(s).desc(),
                        proxy.getCommandHandlers().commands.get(s).usage()));
            }
        }

    }

    private String helpStringBuilder(String n, String d, String u) {
        return plugin.colorConverter.translateAlternateColorCodes('&', "&5/irc "
                + n + " &6" + u + " &f- " + d);
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
