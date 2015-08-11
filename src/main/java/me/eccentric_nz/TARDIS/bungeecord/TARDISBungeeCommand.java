/*
 * Copyright (C) 2015 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.bungeecord;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBungeeCommand {

    private final TARDIS plugin;

    public TARDISBungeeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean cord(Player player, String[] args) {

        String one = (args[1].equalsIgnoreCase("1")) ? "one" : "two";
        String two = (args[1].equalsIgnoreCase("1")) ? "two" : "one";

        String l = "Location{world=CraftWorld{name=world},x=0.0,y=64.0,z=0.0,pitch=0.0,yaw=0.0}";
        new TeleportManager(plugin).teleportToLocation(one, two, player.getUniqueId().toString(), l);
        //
        return true;
    }
}
