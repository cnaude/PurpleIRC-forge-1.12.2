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
package com.cnaude.purpleirc;

import com.cnaude.purpleirc.Commands.*;
import com.cnaude.purpleirc.Proxies.CommonProxy;
import com.google.common.base.Joiner;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 *
 * @author cnaude
 */
public class CommandHandlers extends CommandBase {

    public HashMap<String, IRCCommandInterface> commands = new HashMap<>();
    public ArrayList<String> sortedCommands = new ArrayList<>();
    private final PurpleIRC plugin;
    private final CommonProxy proxy;

    /**
     *
     * @param plugin
     * @param proxy
     */
    public CommandHandlers(PurpleIRC plugin, CommonProxy proxy) {

        this.plugin = plugin;
        this.proxy = proxy;

        commands.put("addop", new AddOp(plugin));
        commands.put("addvoice", new AddVoice(plugin));
        commands.put("connect", new Connect(plugin));
        commands.put("ctcp", new CTCP(plugin));
        commands.put("deop", new DeOp(plugin));
        commands.put("devoice", new DeVoice(plugin));
        commands.put("debug", new Debug(plugin));
        commands.put("disconnect", new Disconnect(plugin));
        commands.put("join", new Join(plugin));
        commands.put("kick", new Kick(plugin));
        commands.put("leave", new Leave(plugin));
        commands.put("list", new List(plugin));
        commands.put("listbots", new ListBots(plugin));
        commands.put("listops", new ListOps(plugin));
        commands.put("listvoices", new ListVoices(plugin));
        commands.put("login", new Login(plugin));
        commands.put("load", new Load(plugin, proxy));
        commands.put("messagedelay", new MessageDelay(plugin));
        commands.put("msg", new Msg(plugin));
        commands.put("motd", new Motd(plugin));
        commands.put("mute", new Mute(plugin));
        commands.put("mutelist", new MuteList(plugin));
        commands.put("nick", new Nick(plugin));
        commands.put("notice", new Notice(plugin));
        commands.put("op", new Op(plugin));
        commands.put("reloadbot", new ReloadBot(plugin));
        commands.put("reloadbotconfig", new ReloadBotConfig(plugin));
        commands.put("reloadbotconfigs", new ReloadBotConfigs(plugin));
        commands.put("reloadbots", new ReloadBots(plugin));
        commands.put("reloadconfig", new ReloadConfig(plugin));
        commands.put("removeop", new RemoveOp(plugin));
        commands.put("removevoice", new RemoveVoice(plugin));
        commands.put("save", new Save(plugin));
        commands.put("say", new Say(plugin));
        commands.put("send", new Send(plugin));
        commands.put("sendraw", new SendRaw(plugin));
        commands.put("server", new Server(plugin));
        commands.put("topic", new Topic(plugin));
        commands.put("unmute", new UnMute(plugin));
        commands.put("updatecheck", new UpdateCheck(plugin));
        commands.put("unload", new Unload(plugin));
        commands.put("voice", new Voice(plugin));
        commands.put("whois", new Whois(plugin));
        commands.put("help", new Help(plugin, proxy));

        for (String s : commands.keySet()) {
            sortedCommands.add(s);
        }
        Collections.sort(sortedCommands, Collator.getInstance());
        plugin.logDebug("Commands enabled: " + Joiner.on(", ").join(sortedCommands));
    }

    /**
     *
     * @param server
     * @param isender
     * @param args
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender isender, String[] args) {
        CommandSender sender = new CommandSender(isender, plugin, proxy);
        if (args.length >= 1) {
            String subCmd = args[0].toLowerCase();
            if (commands.containsKey(subCmd)) {
                if (!sender.hasPermission("irc." + subCmd)) {
                    sender.sendMessage(plugin.noPermission);
                    return;
                }
                commands.get(subCmd).dispatch(sender, args);
                return;
            }
        }
        commands.get("help").dispatch(sender, args);
    }

    @Override
    public boolean isUsernameIndex(String[] sender, int args) {
        //return IRCCommandHandler.isUsernameIndex(sender, args);
        return true;
    }

    private boolean isPlayerOpped(ICommandSender sender) {
        if (sender instanceof EntityPlayer) {
            try {
                for (String player : sender.getServer().getPlayerList().getOppedPlayerNames()) {
                    if (player.equals(sender.getName())) {
                        return true;
                    }
                }
            } catch (Exception ex) {
                return false;
            }
            return false;
        }
        return true; // If it isn't a player, then it's the console
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return isPlayerOpped(sender);
    }

    @Override
    public String getName() {
        return "irc";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

}
