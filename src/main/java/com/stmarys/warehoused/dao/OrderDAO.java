package com.stmarys.warehoused.dao;

import com.stmarys.warehoused.model.Order;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection conn;

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String rawDate = rs.getString("order_date");
                LocalDate orderDate;
                try {
                    orderDate = LocalDate.parse(rawDate); // Try parse normally
                } catch (Exception ex) {
                    long timestamp = Long.parseLong(rawDate);
                    orderDate = new java.sql.Date(timestamp).toLocalDate();
                }
                Order order = new Order(
                    rs.getInt("order_id"),
                    orderDate,
                    rs.getString("customer_name"),
                    rs.getString("order_status")
                );
                orders.add(order);
            }
        }
        return orders;
    }

    public void addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (order_date, customer_name, order_status) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(order.getOrderDate()));
            pstmt.setString(2, order.getCustomerName());
            pstmt.setString(3, order.getOrderStatus());
            pstmt.executeUpdate();
        }
    }

    public void updateOrder(Order order) throws SQLException {
        String sql = "UPDATE orders SET order_date = ?, customer_name = ?, order_status = ? WHERE order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(order.getOrderDate()));
            pstmt.setString(2, order.getCustomerName());
            pstmt.setString(3, order.getOrderStatus());
            pstmt.setInt(4, order.getOrderId());
            pstmt.executeUpdate();
        }
    }

    public void deleteOrder(int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
        }
    }
}
