package com.example.flightapp;

import java.util.List;

public class Cargo {
    private int flightId;
    private List<Baggage> baggage;

    public List<CargoItem> getCargo() {
        return cargo;
    }

    public void setCargo(List<CargoItem> cargo) {
        this.cargo = cargo;
    }

    private List<CargoItem> cargo;

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public List<Baggage> getBaggage() {
        return baggage;
    }

    public void setBaggage(List<Baggage> baggage) {
        this.baggage = baggage;
    }


    // Getters and setters

    // toString method
}
