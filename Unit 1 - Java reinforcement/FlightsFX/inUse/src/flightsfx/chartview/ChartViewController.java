package flightsfx.chartview;

import flightsfx.FXMLMainViewController;
import flightsfx.model.Flight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartViewController {

    @FXML
    private PieChart pieChart;

    List<Flight> flights;

    public void initialize()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/flightsfx/FXMLMainView.fxml"));

        try
        {
            Parent root = (Parent)loader.load();
        } catch (Exception e) {}

        FXMLMainViewController controller = loader.getController();
        flights = controller.getFlights();

        pieChart.getData().clear();

        Map<String, Long> result;
        result = flights.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getDestination(),
                        Collectors.counting())
                );
        result.forEach((cat, sum) -> {
            pieChart.getData().add(new PieChart.Data(cat, sum));
        });
    }

    @FXML
    private void back(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/flightsfx/FXMLMainView.fxml"));
        Parent view1 = loader.load();
        Scene view1Scene = new Scene(view1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(view1Scene);
        stage.show();
    }
}
