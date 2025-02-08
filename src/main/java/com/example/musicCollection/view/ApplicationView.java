package com.example.musicCollection.view;

import com.example.musicCollection.SpringContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ApplicationView extends Application {

    private static BorderPane root;
    private MenuView menuView;
    private AlbumTable albumTable;
    private IconLoader iconLoader;

    @Override
    public void init() throws Exception {
        this.menuView = SpringContext.getBean(MenuView.class);
        this.albumTable = SpringContext.getBean(AlbumTable.class);
        this.iconLoader = SpringContext.getBean(IconLoader.class);
        root = new BorderPane();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage loadingStage = createLoadingWindow();  
        loadingStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                loadingStage.close();
                launchMainApplication(primaryStage);
            });
        }).start();
    }

    private void launchMainApplication(Stage primaryStage) {
        this.menuView.showMenu();
        this.albumTable.showAlbum();
        
        Image logo = iconLoader.getIcon();
        primaryStage.getIcons().add(logo);

        Scene scene = new Scene(root, 1200, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion des Albums");
        primaryStage.show();
    }

    private Stage createLoadingWindow() {
        Stage loadingStage = new Stage();
        loadingStage.setTitle("Chargement...");

        StackPane pane = new StackPane();
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(100, 100);

        Label loadingLabel = new Label("Chargement en cours...");
        loadingLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox vbox = new VBox(20, spinner, loadingLabel);
        vbox.setAlignment(Pos.CENTER);
        pane.getChildren().add(vbox);

        Image logo = iconLoader.getIcon();
        loadingStage.getIcons().add(logo);

        Scene scene = new Scene(pane, 300, 200);
        loadingStage.setScene(scene);
        return loadingStage;
    }

    public static BorderPane getRoot() {
        return root;
    }
}
