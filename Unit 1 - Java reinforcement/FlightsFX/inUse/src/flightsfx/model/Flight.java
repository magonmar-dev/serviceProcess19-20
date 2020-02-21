package flightsfx.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Flight {

    String flightNumber;
    String destination;
    LocalDateTime departureTime;
    LocalTime duration;

    /**
     * Constructor to create a new Flight only with the flight number
     * @param flightNumber
     */
    public Flight(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * Constructor to create a new Flight
     * @param flightNumber
     * @param destination
     * @param departureTime
     * @param duration
     */
    public Flight(String flightNumber, String destination, LocalDateTime departureTime, LocalTime duration) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departureTime = departureTime;
        this.duration = duration;
    }

    /**
     * Method to get the flight number
     * @return flightnumber
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Method to change the flight number
     * @param flightNumber
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * Method to get the destination
     * @return destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Method to change the destination
     * @param destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Method to get the departure time like a String
     * @return departureTime
     */
    public String getDepartureTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return departureTime.format(formatter);
    }

    /**
     * Method to get the departure time formatted
     * @return departureTime
     */
    public LocalDateTime getDepartureTimeF() { return departureTime; }

    /**
     * Method to change the departure time
     * @param departureTime
     */
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * Method to get the duration like a String
     * @return duration
     */
    public String getDuration() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return duration.format(formatter);
    }

    /**
     * Method to get the duration formatted
     * @return duration
     */
    public LocalTime getDurationF() { return duration; }

    /**
     * Method to change the duration
     * @param duration
     */
    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    /**
     * Method to get all the information of the Flight like a String
     * @return all the information of the Flight
     */
    @Override
    public String toString() {
        return flightNumber + ";" + destination + ";" + getDepartureTime() + ";" + getDuration();
    }
}
