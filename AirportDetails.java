package com.example.flightapp;

public class AirportDetails {
    private int departingFlights;
    private int arrivingFlights;
    private int totalArrivingBaggage;
    private int totalDepartingBaggage;

    public AirportDetails(int departingFlights, int arrivingFlights, int totalArrivingBaggage, int totalDepartingBaggage) {
        this.departingFlights = departingFlights;
        this.arrivingFlights = arrivingFlights;
        this.totalArrivingBaggage = totalArrivingBaggage;
        this.totalDepartingBaggage = totalDepartingBaggage;
    }

    public int getDepartingFlights() {
        return departingFlights;
    }

    public void setDepartingFlights(int departingFlights) {
        this.departingFlights = departingFlights;
    }

    public int getArrivingFlights() {
        return arrivingFlights;
    }

    public void setArrivingFlights(int arrivingFlights) {
        this.arrivingFlights = arrivingFlights;
    }

    public int getTotalArrivingBaggage() {
        return totalArrivingBaggage;
    }

    public void setTotalArrivingBaggage(int totalArrivingBaggage) {
        this.totalArrivingBaggage = totalArrivingBaggage;
    }

    public int getTotalDepartingBaggage() {
        return totalDepartingBaggage;
    }

    public void setTotalDepartingBaggage(int totalDepartingBaggage) {
        this.totalDepartingBaggage = totalDepartingBaggage;
    }

    // Constructor, getter, and setter methods

    // Add any additional methods or constructors if needed
}

