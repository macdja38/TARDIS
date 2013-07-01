/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import java.net.MalformedURLException;
import java.net.URL;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

/**
 * Krakenites ware swamp-living aliens with a cylindrical body covered in a
 * thick, grey shell with the texture of barnacle. They have approximately ten
 * tentacles that protrude from the top of their shell.
 *
 * @author eccentric_nz
 */
public class TARDISTexturePackChanger {

    private final TARDIS plugin;

    public TARDISTexturePackChanger(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets a player's texture pack.
     *
     * @param p The player
     * @param url The URL of the texture pack file
     */
    public void changeTP(Player p, String url) {
        // check the URL
        try {
            new URL(url);
            String player = p.getName();
            plugin.getServer().getPlayer(player).setTexturePack(url);
        } catch (MalformedURLException e) {
            p.sendMessage(plugin.pluginName + "Could not access the URL! " + e.getMessage());
        }
    }
}
