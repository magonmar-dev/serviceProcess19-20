package flightsfx;

import flightsfx.model.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static flightsfx.utils.FileUtils.loadFlights;

public class FXMLMainViewController {

    @FXML
    private TableView<Flight> table;

    @FXML
    private TableColumn<Flight, String> colNumber;

    @FXML
    private TableColumn<Flight, String> colDestination;

    @FXML
    private TableColumn<Flight, LocalDateTime> colDeparture;

    @FXML
    private TableColumn<Flight, LocalTime> colDuration;

    @FXML
    private TextField txtNumber;

    @FXML
    private TextField txtDestination;

    @FXML
    private TextField txtDeparture;

    @FXML
    private TextField txtDuration;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnModify;

    @FXML
    private ComboBox<String> comboBox;

    static ObservableList<Flight> flights;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H:mm");

    public void initialize() {
        btnDelete.setDisable(true);
        btnModify.setDisable(true);

        ObservableList<String> filters = FXCollections.observableArrayList(
                "Show all flights", "Show flights to currently selected city",
                "Show long flights", "Show next 5 flights", "Show flight duration average");
        comboBox.setItems(filters);

        flights = FXCollections.observableArrayList(loadFlights());

        colNumber.setCellValueFactory(new PropertyValueFactory("flightNumber"));
        colDestination.setCellValueFactory(new PropertyValueFactory("destination"));
        colDeparture.setCellValueFactory(new PropertyValueFactory("departureTime"));
        colDuration.setCellValueFactory(new PropertyValueFactory("duration"));

        table.setItems(flights);
    }

    @FXML
    void addFlight(ActionEvent event) {
        if(txtNumber.getText().equals("") || txtDestination.getText().equals("")
                || txtDeparture.getText().equals("") || txtDuration.getText().equals("")) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error adding data");
            dialog.setContentText("No field can be empty");
            dialog.showAndWait();
        }
        else {
            flights.add(new Flight(txtNumber.getText(), txtDestination.getText(),
                    LocalDateTime.parse(txtDeparture.getText(), formatter),
                    LocalTime.parse(txtDuration.getText(), formatter1)));
        }
    }

    @FXML
    void enableDeleteBtn(MouseEvent event) {
        btnDelete.setDisable(false);
        btnModify.setDisable(false);
        txtNumber.setText(table.getSelectionModel().getSelectedItem().getFlightNumber());
        txtDestination.setText(table.getSelectionModel().getSelectedItem().getDestination());
        txtDeparture.setText(table.getSelectionModel().getSelectedItem().getDepartureTime());
        txtDuration.setText(table.getSelectionModel().getSelectedItem().getDuration());
    }

    @FXML
    void deleteFlight(ActionEvent event) {
        int deleteIndex = table.getSelectionModel().getSelectedIndex();
        flights.remove(deleteIndex);
    }

    @FXML
    void modifyFlight(ActionEvent event) {
        int modifyIndex = table.getSelectionModel().getSelectedIndex();
        Flight f = table.getSelectionModel().getSelectedItem();
        f.setFlightNumber(txtNumber.getText());
        f.setDestination(txtDestination.getText());
        f.setDepartureTime(LocalDateTime.parse(txtDeparture.getText(), formatter));
        f.setDuration(LocalTime.parse(txtDuration.getText(), formatter1));
        flights.set(modifyIndex,f);
    }

    @FXML
    void applyFilter(ActionEvent event) {
        String filter = comboBox.getValue();

        switch (filter)
        {
            case "Show all flights":
                table.setItems(flights);
                break;
            case "Show flights to currently selected city":
                if(table.getSelectionModel().getSelectedItem() != null) {
                    table.setItems(FXCollections.observableArrayList(
                            flights.stream()
                                    .filter(f -> f.getDestination().equals(
                                            table.getSelectionModel().getSelectedItem().getDestination()))
                                    .collect(Collectors.toList())
                    ));
                }
                else {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("Error");
                    dialog.setHeaderText("Error applying filter");
                    dialog.setContentText("You must select a flight");
                    dialog.showAndWait();
                }
                break;
            case "Show long flights":
                table.setItems(FXCollections.observableArrayList(
                        flights.stream()
                                .filter(f -> f.getDurationF().isAfter(LocalTime.parse("3:00", formatter1)))
                                .collect(Collectors.toList())
                ));
                break;
            case "Show next 5 flights":
                table.setItems(FXCollections.observableArrayList(
                        flights.stream()
                                .filter(f -> f.getDepartureTimeF().isAfter(LocalDateTime.now()))
                                .sorted(new Comparator<Flight>() {
                                    @Override
                                    public int compare(Flight o1, Flight o2) {
                                        if (o1.getDepartureTimeF().isAfter(o2.getDepartureTimeF()))
                                            return 1;
                                        if (o1.getDepartureTimeF().isBefore(o2.getDepartureTimeF()))
                                            return -1;
                                        return 0;
                                    }
                                })
                                .limit(5)
                                .collect(Collectors.toList())
                ));
                break;
            case "Show flight duration average":
                int averageInMinutes = flights.stream()
                        .map(f -> (f.getDurationF().getHour() * 60) + f.getDurationF().getMinute())
                        .mapToInt(f -> f / flights.size())
                        .sum();
                String average = String.format("%02d", averageInMinutes / 60)
                        + ":" + String.format("%02d", averageInMinutes % 60);

                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Flights duration average");
                dialog.setHeaderText(average);
                dialog.showAndWait();
                break;
        }
    }

    /**
     * Method to get the list of flights
     * @return the list of flights
     */
    public static List<Flight> getFlights()
    {
        return flights;
    }

    @FXML
    void goToChartView(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chartview/ChartView.fxml"));
        Parent view1 = loader.load();
        Scene view1Scene = new Scene(view1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(view1Scene);
        stage.show();
    }
}
