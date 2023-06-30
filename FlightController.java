package com.example.flightapp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {
    private List<Flight> flights;

    private ObjectMapper objectMapper;
    public FlightController(ObjectMapper objectMapper, List<Flight> flights) {
        this.objectMapper = objectMapper;
        this.flights = flights;
    }

    public FlightController() throws IOException {

        // Load flight data from JSON file
        ObjectMapper objectMapper = new ObjectMapper();


        flights = objectMapper.readValue(
                getClass().getResourceAsStream("/flight-data.json"),
                new TypeReference<List<Flight>>() {}
        );

        // Read cargo data from cargo-data.json
        List<Cargo> cargoData = objectMapper.readValue(
                getClass().getResourceAsStream("/cargo-data.json"),
                new TypeReference<List<Cargo>>() {}
        );
    }

    @GetMapping("/{flightNumber}")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public ResponseEntity<FlightDetails> getFlightDetails(@PathVariable int flightNumber, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate flightDate) {
        // Find the flight with the given flight number and flight date
        Flight requestedFlight = findFlightByNumberAndDate(flightNumber, flightDate);
        if (requestedFlight != null) {
            int cargoWeight = calculateCargoWeight(requestedFlight);
            int baggageWeight = calculateBaggageWeight(requestedFlight);
            int totalWeight = cargoWeight + baggageWeight;

            FlightDetails flightDetails = new FlightDetails(cargoWeight, baggageWeight, totalWeight);
            return ResponseEntity.ok(flightDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/airport/{iataCode}")
    public ResponseEntity<AirportDetails> getAirportDetails(
            @PathVariable String iataCode,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate flightDate) {

        int departingFlights = countDepartingFlights(iataCode, flightDate);
        int arrivingFlights = countArrivingFlights(iataCode, flightDate);
        int totalArrivingBaggage = calculateTotalArrivingBaggage(iataCode, flightDate);
        int totalDepartingBaggage = calculateTotalDepartingBaggage(iataCode, flightDate);

        AirportDetails airportDetails = new AirportDetails(departingFlights, arrivingFlights, totalArrivingBaggage, totalDepartingBaggage);
        return ResponseEntity.ok(airportDetails);
    }

    private int countDepartingFlights(String iataCode, LocalDate flightDate) {
        return (int) flights.stream()
                .filter(flight -> flight.getDepartureAirportIATACode().equalsIgnoreCase(iataCode) && flight.getDepartureDate().toLocalDate().equals(flightDate))
                .count();
    }

    private int countArrivingFlights(String iataCode, LocalDate flightDate) {
        return (int) flights.stream()
                .filter(flight -> flight.getArrivalAirportIATACode().equalsIgnoreCase(iataCode) && flight.getDepartureDate().toLocalDate().equals(flightDate))
                .count();
    }

    private int calculateTotalArrivingBaggage(String iataCode, LocalDate flightDate) {
        int totalBaggage = 0;
        for (Flight flight : flights) {
            if (flight.getArrivalAirportIATACode().equalsIgnoreCase(iataCode) && flight.getDepartureDate().toLocalDate().equals(flightDate)) {
                for (Cargo cargo : flight.getCargo()) {
                    for (Baggage baggage : cargo.getBaggage()) {
                        totalBaggage += baggage.getPieces();
                    }
                }
            }
        }
        return totalBaggage;
    }

    private int calculateTotalDepartingBaggage(String iataCode, LocalDate flightDate) {
        int totalBaggage = 0;
        for (Flight flight : flights) {
            if (flight.getDepartureAirportIATACode().equalsIgnoreCase(iataCode) && flight.getDepartureDate().toLocalDate().equals(flightDate)) {
                for (Cargo cargo : flight.getCargo()) {
                    for (Baggage baggage : cargo.getBaggage()) {
                        totalBaggage += baggage.getPieces();
                    }
                }
            }
        }
        return totalBaggage;
    }

        private Flight findFlightByNumberAndDate(int flightNumber, LocalDate flightDate) {
            return flights.stream()
                    .filter(flight -> flight.getFlightNumber() == flightNumber && flight.getDepartureDate().toLocalDate().equals(flightDate))
                    .findFirst()
                    .orElse(null);
        }


    private int calculateBaggageWeight(Flight flight) {
        if (flight.getCargo() != null) {
            int baggageWeight = 0;
            for (Cargo cargo : flight.getCargo()) {
                for (Baggage baggage : cargo.getBaggage()) {
                    baggageWeight += baggage.getWeight();
                }
            }
            return baggageWeight;
        } else {
            return 0;
        }
    }

    private int calculateCargoWeight(Flight flight) {
        int totalWeight = 0;
        for (Cargo cargo : flight.getCargo()) {
            totalWeight += calculateCargoWeight(cargo);
        }
        return totalWeight;
    }

    private int calculateCargoWeight(Cargo cargo) {
        int totalWeight = 0;
        for (Baggage baggage : cargo.getBaggage()) {
            totalWeight += convertWeightToKg(baggage.getWeight(), baggage.getWeightUnit()) * baggage.getPieces();
        }
        for (CargoItem cargoItem : cargo.getCargo()) {
            totalWeight += convertWeightToKg(cargoItem.getWeight(), cargoItem.getWeightUnit()) * cargoItem.getPieces();
        }
        return totalWeight;
    }

    private int convertWeightToKg(int weight, String weightUnit) {
        if (weightUnit.equalsIgnoreCase("lb")) {
            return (int) Math.round(weight * 0.453592); // Convert pounds to kilograms
        } else {
            return weight; // Already in kilograms
        }
    }

    private static class FlightDetails {
        private int cargoWeight;
        private int baggageWeight;
        private int totalWeight;

        public FlightDetails(int cargoWeight, int baggageWeight, int totalWeight) {
            this.cargoWeight = cargoWeight;
            this.baggageWeight = baggageWeight;
            this.totalWeight = totalWeight;
        }

        public int getCargoWeight() {
            return cargoWeight;
        }

        public void setCargoWeight(int cargoWeight) {
            this.cargoWeight = cargoWeight;
        }

        public int getBaggageWeight() {
            return baggageWeight;
        }

        public void setBaggageWeight(int baggageWeight) {
            this.baggageWeight = baggageWeight;
        }

        public int getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(int totalWeight) {
            this.totalWeight = totalWeight;
        }

        // Getters and setters

        // toString method
    }
}
