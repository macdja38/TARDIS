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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.entity.Player;

/**
 * Do basic SQL INSERT, UPDATE and DELETE queries.
 *
 * @author eccentric_nz
 */
public class QueryFactory {

    private final TARDIS plugin;
    TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    Connection connection = service.getConnection();
    private final String prefix;

    public QueryFactory(TARDIS plugin) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
    }

    /**
     * Inserts data into an SQLite database table. This method executes the SQL
     * in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param data a HashMap<String, Object> of table fields and values to
     * insert.
     */
    public void doInsert(String table, HashMap<String, Object> data) {
        TARDISSQLInsert insert = new TARDISSQLInsert(plugin, table, data);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, insert);
    }

    /**
     * Inserts data into an SQLite database table. This method builds a prepared
     * SQL statement from the parameters supplied and then executes the insert.
     *
     * @param table the database table name to insert the data into.
     * @param data a HashMap<String, Object> of table fields and values to
     * insert.
     * @return the primary key of the record that was inserted
     */
    public int doSyncInsert(String table, HashMap<String, Object> data) {
        PreparedStatement ps = null;
        ResultSet idRS = null;
        String fields;
        String questions;
        StringBuilder sbf = new StringBuilder();
        StringBuilder sbq = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sbf.append(entry.getKey()).append(",");
            sbq.append("?,");
        }
        fields = sbf.toString().substring(0, sbf.length() - 1);
        questions = sbq.toString().substring(0, sbq.length() - 1);
        try {
            service.testConnection(connection);
            ps = connection.prepareStatement("INSERT INTO " + prefix + table + " (" + fields + ") VALUES (" + questions + ")", PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class) || entry.getValue().getClass().equals(UUID.class)) {
                    ps.setString(i, entry.getValue().toString());
                } else {
                    if (entry.getValue().getClass().getName().contains("Double")) {
                        ps.setDouble(i, TARDISNumberParsers.parseDouble(entry.getValue().toString()));
                    }
                    if (entry.getValue().getClass().getName().contains("Long")) {
                        ps.setLong(i, TARDISNumberParsers.parseLong(entry.getValue().toString()));
                    } else {
                        ps.setInt(i, TARDISNumberParsers.parseInt(entry.getValue().toString()));
                    }
                }
                i++;
            }
            data.clear();
            ps.executeUpdate();
            idRS = ps.getGeneratedKeys();
            return (idRS.next()) ? idRS.getInt(1) : -1;
        } catch (SQLException e) {
            plugin.debug("Insert error for " + table + "! " + e.getMessage());
            return -1;
        } finally {
            try {
                if (idRS != null) {
                    idRS.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }

    /**
     * Updates data in an SQLite database table. This method executes the SQL in
     * a separate thread.
     *
     * @param table the database table name to update.
     * @param data a HashMap<String, Object> of table fields and values update.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to update.
     */
    public void doUpdate(String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        TARDISSQLUpdate update = new TARDISSQLUpdate(plugin, table, data, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, update);
    }

    /**
     * Updates data in an SQLite database table. This method executes the SQL on
     * the main thread.
     *
     * @param table the database table name to update.
     * @param data a HashMap<String, Object> of table fields and values update.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to update.
     */
    public void doSyncUpdate(String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        TARDISSQLUpdate update = new TARDISSQLUpdate(plugin, table, data, where);
        plugin.getServer().getScheduler().runTask(plugin, update);
    }

    /**
     * Deletes rows from an SQLite database table. This method executes the SQL
     * in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to delete.
     */
    public void doDelete(String table, HashMap<String, Object> where) {
        TARDISSQLDelete delete = new TARDISSQLDelete(plugin, table, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, delete);
    }

    /**
     * Deletes rows from an SQLite database table. This method executes the SQL
     * in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to delete.
     * @return true or false depending on whether the data was deleted
     * successfully
     */
    public boolean doSyncDelete(String table, HashMap<String, Object> where) {
        Statement statement = null;
        String values;
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class) || entry.getValue().getClass().equals(UUID.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        }
        where.clear();
        values = sbw.toString().substring(0, sbw.length() - 5);
        String query = "DELETE FROM " + prefix + table + " WHERE " + values;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            return (statement.executeUpdate(query) > 0);
        } catch (SQLException e) {
            plugin.debug("Delete error for " + table + "! " + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }

    /**
     * Adds or removes Artron Energy from an SQLite database table. This method
     * executes the SQL in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param amount the amount of energy to add or remove (use a negative
     * value)
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to alter.
     * @param p the player who receives the success message.
     */
    public void alterEnergyLevel(String table, int amount, HashMap<String, Object> where, Player p) {
        TARDISSQLAlterEnergy alter = new TARDISSQLAlterEnergy(plugin, table, amount, where, p);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, alter);
    }

    /**
     * Removes condenser block counts from an SQLite database table. This method
     * executes the SQL in a separate thread.
     *
     * @param amount the amount of blocks to remove
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to alter.
     */
    public void alterCondenserBlockCount(int amount, HashMap<String, Object> where) {
        TARDISSQLCondenserUpdate condense = new TARDISSQLCondenserUpdate(plugin, amount, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, condense);
    }

    /**
     * Inserts or updates data in a database table. This method executes the SQL
     * in a separate thread.
     *
     * @param id the database table name to insert the data into.
     * @param type the type of control to insert.
     * @param l the string location of the control
     * @param s what level the control is (1 primary, 2 secondary, 3 tertiary)
     */
    public void insertControl(int id, int type, String l, int s) {
        TARDISSQLInsertControl control = new TARDISSQLInsertControl(plugin, id, type, l, s);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, control);
    }

    /**
     * Inserts or updates data in a database table. This method executes the SQL
     * in a separate thread.
     *
     * @param id the database table name to insert the data into.
     * @param type the type of control to insert.
     * @param l the string location of the control
     * @param s what level the control is (1 primary, 2 secondary, 3 tertiary)
     */
    public void insertSyncControl(int id, int type, String l, int s) {
        Statement statement = null;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            String select = "SELECT c_id FROM " + prefix + "controls WHERE tardis_id = " + id + " AND type = " + type + " AND secondary = " + s;
            ResultSet rs = statement.executeQuery(select);
            if (rs.isBeforeFirst()) {
                // update
                String update = "UPDATE " + prefix + "controls SET location = '" + l + "' WHERE c_id = " + rs.getInt("c_id");
                statement.executeUpdate(update);
            } else {
                // insert
                String insert = "INSERT INTO " + prefix + "controls (tardis_id, type, location, secondary) VALUES (" + id + ", " + type + ", '" + l + "', " + s + ")";
                statement.executeUpdate(insert);
            }
        } catch (SQLException e) {
            plugin.debug("Insert control error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing insert control statement! " + e.getMessage());
            }
        }
    }

    /**
     * Inserts data into an SQLite database table. This method executes the SQL
     * in a separate thread.
     *
     * @param data a HashMap<String, Object> of table fields and values to
     * insert.
     * @param biome the biome of the Police Box location
     * @param id the tardis_id
     */
    public void insertLocations(HashMap<String, Object> data, String biome, int id) {
        TARDISSQLInsertLocations locate = new TARDISSQLInsertLocations(plugin, data, biome, id);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, locate);
    }

    /**
     * Updates the Artron condenser block count for a specific block.
     *
     * @param new_size the newly calculated total number of blocks condensed
     * @param id the tardis_id of the record to update
     * @param block_data the block_data of the record to update
     */
    public void updateCondensedBlockCount(int new_size, int id, String block_data) {
        Statement statement = null;
        String query = "UPDATE " + prefix + "condenser SET block_count = " + new_size + " WHERE tardis_id = " + id + " AND block_data = '" + block_data + "'";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.debug("Update error for condenser! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing condenser! " + e.getMessage());
            }
        }
    }

    /**
     * Save the biome the Police Box lands in to the current table so that it
     * can be restored after it leaves. This is only done if
     * `police_box.set_biome: true` is set in the config.
     *
     * @param id the TARDIS to update
     * @param biome the biome to save
     */
    public void saveBiome(int id, String biome) {
        PreparedStatement ps = null;
        String query = "UPDATE " + prefix + "current SET biome = ? WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            ps = connection.prepareStatement(query);
            ps.setString(1, biome);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Update error for saving biome to current! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing statement! " + e.getMessage());
            }
        }
    }
}
