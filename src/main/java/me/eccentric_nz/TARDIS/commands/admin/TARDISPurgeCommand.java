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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.destroyers.TARDISExterminator;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPurgeCommand {

    private final TARDIS plugin;

    public TARDISPurgeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean clearAll(CommandSender sender, String[] args) {
        // get the player's TARDIS id
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", args[1]);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            sender.sendMessage(plugin.getPluginName() + "Could not find a TARDIS record for player: " + args[1] + "!");
            return true;
        }
        int id = rs.getTardis_id();
        TARDISExterminator purger = new TARDISExterminator(plugin);
        purger.cleanHashMaps(id);
        purger.cleanDatabase(id);
        sender.sendMessage(plugin.getPluginName() + "Database records for player: " + args[1] + "have been purged.");
        return true;
    }
}
