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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMessageListener implements PluginMessageListener {

    private final TARDIS plugin;

    public TARDISMessageListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        plugin.debug("Channel: " + channel);
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        plugin.debug("Subchannel: " + subchannel);
        if (subchannel.equals("TARDIS")) {
            try {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);

                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
                // Read the data in the same way you wrote it
                String from = msgin.readUTF();
                plugin.debug("From: " + from);
                UUID uuid = UUID.fromString(msgin.readUTF());
                Location loc = plugin.getLocationUtils().getLocationFromBukkitString(msgin.readUTF());
            } catch (IOException ex) {
                plugin.debug("Failed to read PluginMessage! " + ex.getMessage());
            }
        }
    }
}
