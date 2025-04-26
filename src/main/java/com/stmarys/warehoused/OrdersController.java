package com.stmarys.warehoused;

import com.stmarys.warehoused.dao.OrderDAO;
import com.stmarys.warehoused.model.Order;
import com.stmarys.warehoused.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class OrdersController {
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> idColumn;
    @FXML private TableColumn<Order, LocalDate> dateColumn;
    @FXML private TableColumn<Order, String> customerColumn;
    @FXML private TableColumn<Order, String> statusColumn;

    @FXML private TextField customerNameField;
    @FXML private TextField orderStatusField;

    private final ObservableList<Order> orderList = FXCollections.observableArrayList();
    private OrderDAO orderDAO;

    @FXML
    public void initialize() {
        ordersTable.setOnMouseClicked(event -> {
            Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                customerNameField.setText(selectedOrder.getCustomerName());
                orderStatusField.setText(selectedOrder.getOrderStatus());
            }
        });

        try {
            Connection conn = DBConnection.getConnection();
            orderDAO = new OrderDAO(conn);

            idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getOrderId()).asObject());
            dateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getOrderDate()));
            customerColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCustomerName()));
            statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrderStatus()));

            loadOrderData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOrderData() throws SQLException {
        List<Order> orders = orderDAO.getAllOrders();
        orderList.setAll(orders);
        ordersTable.setItems(orderList);
    }

    @FXML
    private void handleAddOrder() {
        try {
            String customer = customerNameField.getText();
            String status = orderStatusField.getText();
            LocalDate date = LocalDate.now();

            Order newOrder = new Order(0, date, customer, status);
            orderDAO.addOrder(newOrder);
            loadOrderData();

            customerNameField.clear();
            orderStatusField.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void switchToInventory() {
        try {
            App.setRoot("inventory");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToOrders() {
        try {
            App.setRoot("orders");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToShipments() {
        try {
            App.setRoot("shipments");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleUpdateOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                String customerName = customerNameField.getText();
                String orderStatus = orderStatusField.getText();

                if (customerName.isEmpty() || orderStatus.isEmpty()) {
                    return;
                }

                Order updatedOrder = new Order(
                    selectedOrder.getOrderId(),
                    selectedOrder.getOrderDate(),
                    customerName,
                    orderStatus
                );

                orderDAO.updateOrder(updatedOrder);
                loadOrderData();

                customerNameField.clear();
                orderStatusField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                orderDAO.deleteOrder(selectedOrder.getOrderId());
                loadOrderData();

                customerNameField.clear();
                orderStatusField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
