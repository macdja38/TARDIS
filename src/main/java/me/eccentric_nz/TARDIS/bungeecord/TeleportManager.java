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
import java.io.DataOutputStream;
import java.io.IOException;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * @author eccentric_nz
 */
public class TeleportManager {

    private final TARDIS plugin;

    public TeleportManager(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void teleportToLocation(String toServer, String fromServer, String uuid, String loc) {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        try {
            out.writeUTF("Forward");
            out.writeUTF(toServer);
            out.writeUTF("TARDIS");

            msgout.writeUTF(fromServer);
            msgout.writeUTF(uuid);
            msgout.writeUTF(loc);

            byte[] data = msgbytes.toByteArray();
            out.writeShort(data.length);
            out.write(data);

            new PluginMessageTask(plugin, b).runTaskAsynchronously(plugin);

        } catch (IOException e) {
            plugin.debug("Could not send plugin message! " + e.getMessage());
        } finally {
            try {
                msgbytes.close();
                msgout.close();
                out.close();
                b.close();
            } catch (IOException e) {
                plugin.debug("Could not close output streams! " + e.getMessage());
            }
        }
    }
}
