package com.stmarys.warehoused.model;

import java.time.LocalDate;

public class Shipment {
    private int shipmentId;
    private String destination;
    private LocalDate shipmentDate;
    private String shipmentStatus;

    public Shipment(int shipmentId, String destination, LocalDate shipmentDate, String shipmentStatus) {
        this.shipmentId = shipmentId;
        this.destination = destination;
        this.shipmentDate = shipmentDate;
        this.shipmentStatus = shipmentStatus;
    }

    // Getters and Setters
    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(LocalDate shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }
}
