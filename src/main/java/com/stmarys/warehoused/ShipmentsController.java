package com.stmarys.warehoused;

import com.stmarys.warehoused.dao.ShipmentDAO;
import com.stmarys.warehoused.model.Shipment;
import com.stmarys.warehoused.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ShipmentsController {
    @FXML private TableView<Shipment> shipmentsTable;
    @FXML private TableColumn<Shipment, Integer> idColumn;
    @FXML private TableColumn<Shipment, String> destinationColumn;
    @FXML private TableColumn<Shipment, LocalDate> dateColumn;
    @FXML private TableColumn<Shipment, String> statusColumn;

    @FXML private TextField destinationField;
    @FXML private TextField shipmentStatusField;

    private final ObservableList<Shipment> shipmentList = FXCollections.observableArrayList();
    private ShipmentDAO shipmentDAO;

    @FXML
    public void initialize() {
        shipmentsTable.setOnMouseClicked(event -> {
            Shipment selectedShipment = shipmentsTable.getSelectionModel().getSelectedItem();
            if (selectedShipment != null) {
                destinationField.setText(selectedShipment.getDestination());
                shipmentStatusField.setText(selectedShipment.getShipmentStatus());
            }
        });

        try {
            Connection conn = DBConnection.getConnection();
            shipmentDAO = new ShipmentDAO(conn);

            idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getShipmentId()).asObject());
            destinationColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDestination()));
            dateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getShipmentDate()));
            statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getShipmentStatus()));

            loadShipmentData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadShipmentData() throws SQLException {
        List<Shipment> shipments = shipmentDAO.getAllShipments();
        shipmentList.setAll(shipments);
        shipmentsTable.setItems(shipmentList);
    }

    @FXML
    private void handleAddShipment() {
        try {
            String destination = destinationField.getText();
            String status = shipmentStatusField.getText();
            LocalDate date = LocalDate.now();

            Shipment newShipment = new Shipment(0, destination, date, status);
            shipmentDAO.addShipment(newShipment);
            loadShipmentData();

            destinationField.clear();
            shipmentStatusField.clear();
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
    private void handleUpdateShipment() {
        Shipment selectedShipment = shipmentsTable.getSelectionModel().getSelectedItem();
        if (selectedShipment != null) {
            try {
                String destination = destinationField.getText();
                String status = shipmentStatusField.getText();

                if (destination.isEmpty() || status.isEmpty()) {
                    return;
                }

                Shipment updatedShipment = new Shipment(
                    selectedShipment.getShipmentId(),
                    destination,
                    selectedShipment.getShipmentDate(),
                    status
                );

                shipmentDAO.updateShipment(updatedShipment);
                loadShipmentData();

                destinationField.clear();
                shipmentStatusField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteShipment() {
        Shipment selectedShipment = shipmentsTable.getSelectionModel().getSelectedItem();
        if (selectedShipment != null) {
            try {
                shipmentDAO.deleteShipment(selectedShipment.getShipmentId());
                loadShipmentData();

                destinationField.clear();
                shipmentStatusField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
