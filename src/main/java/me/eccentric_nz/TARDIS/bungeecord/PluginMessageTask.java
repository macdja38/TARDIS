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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author eccentric_nz
 */
public class PluginMessageTask extends BukkitRunnable {

    private final TARDIS plugin;
    private final ByteArrayOutputStream bytes;

    public PluginMessageTask(TARDIS plugin, ByteArrayOutputStream bytes) {
        this.plugin = plugin;
        this.bytes = bytes;
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<Player>(Bukkit.getOnlinePlayers());
        if (players.isEmpty()) {
            return;
        }
        Player p = players.get(0);
        if (p == null) {
            return;
        }
        p.sendPluginMessage(plugin, "BungeeCord", bytes.toByteArray());
    }
}
