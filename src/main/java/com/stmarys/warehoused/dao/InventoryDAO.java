package com.stmarys.warehoused.dao;

import com.stmarys.warehoused.model.InventoryItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    private Connection conn;

    public InventoryDAO(Connection conn) {
        this.conn = conn;
    }

    public List<InventoryItem> getAllItems() throws SQLException {
        List<InventoryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                InventoryItem item = new InventoryItem(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getInt("item_quantity"),
                    rs.getString("item_location")
                );
                items.add(item);
            }
        }
        return items;
    }

    public void addItem(InventoryItem item) throws SQLException {
        String sql = "INSERT INTO inventory (item_name, item_quantity, item_location) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setInt(2, item.getItemQuantity());
            pstmt.setString(3, item.getItemLocation());
            pstmt.executeUpdate();
        }
    }

    public void updateItem(InventoryItem item) throws SQLException {
        String sql = "UPDATE inventory SET item_name = ?, item_quantity = ?, item_location = ? WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setInt(2, item.getItemQuantity());
            pstmt.setString(3, item.getItemLocation());
            pstmt.setInt(4, item.getItemId());
            pstmt.executeUpdate();
        }
    }

    public void deleteItem(int itemId) throws SQLException {
        String sql = "DELETE FROM inventory WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();
        }
    }
}
