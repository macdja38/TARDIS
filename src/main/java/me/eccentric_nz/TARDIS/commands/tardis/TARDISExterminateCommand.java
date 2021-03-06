/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.destroyers.TARDISExterminator;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISExterminateCommand {

    private final TARDIS plugin;

    public TARDISExterminateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doExterminate(Player player) {

        if (!plugin.getTrackerKeeper().getExterminate().containsKey(player.getUniqueId())) {
            TARDISMessage.send(player, "TARDIS_BREAK_SIGN");
            return false;
        }
        TARDISExterminator del = new TARDISExterminator(plugin);
        return del.exterminate(player, plugin.getTrackerKeeper().getExterminate().get(player.getUniqueId()));
    }
}
