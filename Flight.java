package com.example.flightapp;

import java.time.LocalDateTime;
import java.util.List;

public class Flight {
    private int flightId;
    private int flightNumber;
    private String departureAirportIATACode;
    private String arrivalAirportIATACode;

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    private LocalDateTime departureDate;
    private List<Cargo> cargo;

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public List<Cargo> getCargo() {
        return cargo;
    }

    public void setCargo(List<Cargo> cargo) {
        this.cargo = cargo;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirportIATACode() {
        return departureAirportIATACode;
    }

    public void setDepartureAirportIATACode(String departureAirportIATACode) {
        this.departureAirportIATACode = departureAirportIATACode;
    }

    public String getArrivalAirportIATACode() {
        return arrivalAirportIATACode;
    }

    public void setArrivalAirportIATACode(String arrivalAirportIATACode) {
        this.arrivalAirportIATACode = arrivalAirportIATACode;
    }

    // toString method
}
