package flightsfx.utils;

import flightsfx.model.Flight;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private static String fileName = "flights.txt";

    /**
     * Method to get the flight of the text file
     * @return a list of flights
     */
    public static List<Flight> loadFlights() {

        List<Flight> list = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H:mm");

        try {
            BufferedReader readerFile = new BufferedReader(
                    new FileReader(new File(fileName)));

            String line = null;
            while((line = readerFile.readLine()) != null) {
                String[] parts = line.split(";");
                String num = parts[0];
                String des = parts[1];
                LocalDateTime dep = LocalDateTime.parse(parts[2], formatter);
                LocalTime dur = LocalTime.parse(parts[3], formatter1);
                list.add(new Flight(num, des, dep, dur));
            }
            readerFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Method to save the flights in the text file
     * @param list
     */
    public static void saveFlights(List<Flight> list) {

        try {
            PrintWriter printerFile = new PrintWriter(fileName);

            for (Flight f: list) {
                printerFile.println(f.toString());
            }

            printerFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
