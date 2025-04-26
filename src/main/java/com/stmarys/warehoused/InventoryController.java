package com.stmarys.warehoused;

import com.stmarys.warehoused.dao.InventoryDAO;
import com.stmarys.warehoused.model.InventoryItem;
import com.stmarys.warehoused.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InventoryController {
    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem, Integer> idColumn;
    @FXML private TableColumn<InventoryItem, String> nameColumn;
    @FXML private TableColumn<InventoryItem, Integer> quantityColumn;
    @FXML private TableColumn<InventoryItem, String> locationColumn;

    @FXML private TextField itemNameField;
    @FXML private TextField itemQuantityField;
    @FXML private TextField itemLocationField;

    private final ObservableList<InventoryItem> inventoryList = FXCollections.observableArrayList();
    private InventoryDAO inventoryDAO;

    @FXML
    public void initialize() {
        inventoryTable.setOnMouseClicked(event -> {
            InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                itemNameField.setText(selectedItem.getItemName());
                itemQuantityField.setText(String.valueOf(selectedItem.getItemQuantity()));
                itemLocationField.setText(selectedItem.getItemLocation());
            }
        });
        try {
            Connection conn = DBConnection.getConnection();
            inventoryDAO = new InventoryDAO(conn);

            idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getItemId()).asObject());
            nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getItemName()));
            quantityColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getItemQuantity()).asObject());
            locationColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getItemLocation()));

            loadInventoryData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInventoryData() throws SQLException {
        List<InventoryItem> items = inventoryDAO.getAllItems();
        inventoryList.setAll(items);
        inventoryTable.setItems(inventoryList);
    }

    @FXML
    private void handleAddItem() {
        try {
            String name = itemNameField.getText();
            int quantity = Integer.parseInt(itemQuantityField.getText());
            String location = itemLocationField.getText();

            InventoryItem newItem = new InventoryItem(0, name, quantity, location);
            inventoryDAO.addItem(newItem);
            loadInventoryData();

            itemNameField.clear();
            itemQuantityField.clear();
            itemLocationField.clear();
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
    private void handleUpdateItem() {
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                String name = itemNameField.getText();
                int quantity = Integer.parseInt(itemQuantityField.getText());
                String location = itemLocationField.getText();

                InventoryItem updatedItem = new InventoryItem(selectedItem.getItemId(), name, quantity, location);
                inventoryDAO.updateItem(updatedItem);
                loadInventoryData();

                itemNameField.clear();
                itemQuantityField.clear();
                itemLocationField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No item selected for update.");
        }
    }

    @FXML
    private void handleDeleteItem() {
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                inventoryDAO.deleteItem(selectedItem.getItemId());
                loadInventoryData();

                itemNameField.clear();
                itemQuantityField.clear();
                itemLocationField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No item selected for delete.");
        }
    }
}
