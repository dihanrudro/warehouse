package com.stmarys.warehoused.dao;

import com.stmarys.warehoused.model.Shipment;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAO {
    private Connection conn;

    public ShipmentDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Shipment> getAllShipments() throws SQLException {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM shipments";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Shipment shipment = new Shipment(
                    rs.getInt("shipment_id"),
                    rs.getString("destination"),
                    LocalDate.parse(rs.getString("shipment_date")),
                    rs.getString("shipment_status")
                );
                shipments.add(shipment);
            }
        }
        return shipments;
    }

    public void addShipment(Shipment shipment) throws SQLException {
        String sql = "INSERT INTO shipments (destination, shipment_date, shipment_status) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, shipment.getDestination());
            pstmt.setString(2, shipment.getShipmentDate().toString());
            pstmt.setString(3, shipment.getShipmentStatus());
            pstmt.executeUpdate();
        }
    }

    public void updateShipment(Shipment shipment) throws SQLException {
        String sql = "UPDATE shipments SET destination = ?, shipment_date = ?, shipment_status = ? WHERE shipment_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, shipment.getDestination());
            pstmt.setString(2, shipment.getShipmentDate().toString());
            pstmt.setString(3, shipment.getShipmentStatus());
            pstmt.setInt(4, shipment.getShipmentId());
            pstmt.executeUpdate();
        }
    }

    public void deleteShipment(int shipmentId) throws SQLException {
        String sql = "DELETE FROM shipments WHERE shipment_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentId);
            pstmt.executeUpdate();
        }
    }
}
