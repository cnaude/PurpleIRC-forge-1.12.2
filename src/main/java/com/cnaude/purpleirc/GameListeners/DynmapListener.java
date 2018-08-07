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
package com.cnaude.purpleirc.GameListeners;

import com.cnaude.purpleirc.Hooks.DynmapHook;
import com.cnaude.purpleirc.PurpleBot;
import com.cnaude.purpleirc.PurpleIRC;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;

/**
 *
 * @author cnaude
 */
@SideOnly(Side.SERVER)
public class DynmapListener extends DynmapCommonAPIListener {

    private final PurpleIRC plugin;

    /**
     *
     * @param plugin
     */
    public DynmapListener(PurpleIRC plugin) {
        plugin.logInfo("Initializing Dynmap listener.");   
        this.plugin = plugin;
        register();
    }

    private void register() {
        DynmapCommonAPIListener.register(this);
    }

    @Override
    public boolean webChatEvent(String source, String name, String message) {
        plugin.logDebug("DynmapWebChat: " + source + " : " + name + ":" + message);
        for (PurpleBot ircBot : plugin.ircBots.values()) {
            ircBot.dynmapWebChat(source, name, message);
        }
        return true;
    }

    @Override
    public void apiEnabled(DynmapCommonAPI api) {
        plugin.logInfo("Registering Dynmap listener.");
        MinecraftForge.EVENT_BUS.register(this);
        plugin.logInfo("Enabling Dynmap hook.");
        this.plugin.dynmapHook = new DynmapHook(plugin, api);        
    }

    @Override
    public void apiDisabled(DynmapCommonAPI api) {
        plugin.logInfo("Unregistering Dynmap listener.");
        try {
            MinecraftForge.EVENT_BUS.unregister(this);
            this.plugin.dynmapHook = null;
        } catch (Exception ex) {
            plugin.logDebug(ex.getMessage());
        }
    }

}
