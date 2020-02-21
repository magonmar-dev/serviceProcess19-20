package linktracker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import linktracker.model.WebPage;
import linktracker.utils.FileUtils;
import linktracker.utils.LinkReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FXMLMainViewController {

    @FXML
    private ListView<WebPage> lvWebPages;

    @FXML
    private ListView<String> lvLinks;

    @FXML
    private Label lblTotalPages;

    @FXML
    private Label lblProcessed;

    @FXML
    private Label lblTotalLinks;

    private List<WebPage> webList;
    private List<Future<WebPage>> futuresList;
    private AtomicInteger linksFoundCount;
    private ThreadPoolExecutor executor;
    private ScheduledService<Boolean> schedServ;

    @FXML
    private void initialize() {
        linksFoundCount = new AtomicInteger(0);
        futuresList = new ArrayList<>();
        webList = new ArrayList<>();

        schedServ = new ScheduledService<Boolean>() {
            protected Task<Boolean> createTask() {
                Task<Boolean> task = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return executor.isTerminated();
                    }
                };
                task.setOnSucceeded(e -> {
                    lblProcessed.setText(String.valueOf(executor.getCompletedTaskCount()));
                    lblTotalLinks.setText(String.valueOf(linksFoundCount.get()));
                });
                return task;
            }
        };

        schedServ.setOnSucceeded(e -> {
            if(schedServ.getValue()) {
                schedServ.cancel();
            }
        });
    }

    @FXML
    private void loadFile(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            webList = FileUtils.loadPages(selectedFile.toPath());
        }

        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setHeaderText("File loaded");
        dialog.setContentText(webList.size() + " pages found");
        dialog.initStyle(StageStyle.UTILITY);
        dialog.showAndWait();

        lblTotalPages.setText(String.valueOf(webList.size()));
        lvWebPages.getItems().setAll(webList);
    }

    private Callable<WebPage> webCallable(WebPage w) {

        return() -> {
            w.setLinks(LinkReader.getLinks(w.getUrl()));
            linksFoundCount.addAndGet(w.getLinks().size());
            return w;
        };
    }

    @FXML
    private void startProcess(ActionEvent event) {

        if(webList.isEmpty()) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setHeaderText("Process error");
            dialog.setContentText("No URL list loaded");
            dialog.initStyle(StageStyle.UTILITY);
            dialog.showAndWait();
        }
        else {
            List<Callable<WebPage>> callables = new ArrayList<>();
            for(WebPage w: webList) {
                callables.add(webCallable(w));
            }
            executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors());

            for(Callable<WebPage> c: callables) {
                futuresList.add(executor.submit(c));
            }
            executor.shutdown();
            schedServ.restart();
        }
    }

    @FXML
    private void showLinks(MouseEvent event) {

        if(webList.isEmpty()) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setHeaderText("Process error");
            dialog.setContentText("No URL list loaded");
            dialog.initStyle(StageStyle.UTILITY);
            dialog.showAndWait();
        }
        else if(futuresList.isEmpty()) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setHeaderText("Process error");
            dialog.setContentText("No links loaded");
            dialog.initStyle(StageStyle.UTILITY);
            dialog.showAndWait();
        }
        else {
            WebPage w = lvWebPages.getSelectionModel().getSelectedItem();
            lvLinks.getItems().setAll(w.getLinks());
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void clearProcess(ActionEvent event) {
        lvWebPages.getItems().clear();
        lvLinks.getItems().clear();
        webList.clear();
        futuresList.clear();
        lblTotalPages.setText("0");
        lblProcessed.setText("0");
        lblTotalLinks.setText("0");
    }
}