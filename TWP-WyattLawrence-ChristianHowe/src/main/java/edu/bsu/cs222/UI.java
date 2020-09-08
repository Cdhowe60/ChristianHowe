package edu.bsu.cs222;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

//The following code was based off David Largent's program "UITranslator";

public class UI extends Application {

    private TextField inputField;
    private Button searchButton;
    private Button clearButton;
    private TextArea outputField;
    private ComboBox<String> responseTypeSelector;

    public static void main(String args[]){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        inputField = new TextField();
            searchButton = new Button("Search");
            clearButton = new Button("Clear");
            outputField = new TextArea();
            responseTypeSelector = new ComboBox<>();



        outputField.setEditable(false);
        configure(primaryStage);
        configureComboBox();
        configureSearchButton();
        configureClearButton();
    }


    private void configureClearButton() {
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                clearSearchBar();
            }
        });
    }

    private void clearSearchBar() {
        inputField.clear();
        outputField.clear();
        responseTypeSelector.getSelectionModel().clearSelection();
    }

    private void configure(Stage stage) {
        stage.setTitle("Wiki Parser");
        stage.setScene(new Scene(createRoot()));
        stage.sizeToScene();
        stage.show();
    }

    private Pane createRoot() {
        VBox root = new VBox();
        root.getChildren().addAll( //
                new Label("Type Your Search Item to Receive List of Most Recent or Most Frequent Edits:"), //
                inputField, //
                responseTypeSelector, //
                searchButton, //
                clearButton,//
                new Label("Editors:"),//
                outputField);
        return root;
    }

    private void configureComboBox() {
        responseTypeSelector.getItems().addAll("Most Frequent Editors", "Most Recent Edits");
    }

    private void configureSearchButton() {
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                translateInputFieldToOutputFiled();
            }
        });
    }

    private void translateInputFieldToOutputFiled() {
        OutputSorter jsonSorter = new OutputSorter();
        boolean isMostFrequent;
        if (responseTypeSelector.getValue().equals("Most Frequent Editors")) {
            isMostFrequent = true;
            String searchText = String.valueOf(inputField.getText()).replace(" ", "_");
            String printableString = jsonSorter.sortByMostRecent(searchText, isMostFrequent);
            outputField.setText(printableString);
        }
        if (responseTypeSelector.getValue().equals("Most Recent Edits")) {
            isMostFrequent = false;
            String searchText = String.valueOf(inputField.getText()).replace(" ", "_");
            String printableString = jsonSorter.sortByMostRecent(searchText, isMostFrequent);
            outputField.setText(printableString);
        }
    }
}
