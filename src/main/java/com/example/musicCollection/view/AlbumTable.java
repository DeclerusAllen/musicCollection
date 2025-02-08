package com.example.musicCollection.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.stereotype.Component;
import com.example.musicCollection.model.Album;
import com.example.musicCollection.service.MusicCollectionService;
import com.example.musicCollection.repository.ArtistRepository;
import com.example.musicCollection.view.dialog.UpdateDialog;
import com.example.musicCollection.view.dialog.DetailsDialog;
import com.example.musicCollection.exception.AppException;

@Component
public class AlbumTable {

    private final MusicCollectionService musicCollectionService;
    private final ArtistRepository artistRepository;

    public AlbumTable(MusicCollectionService musicCollectionService, ArtistRepository artistRepository) {
        this.musicCollectionService = musicCollectionService;
        this.artistRepository = artistRepository;
    }

    @SuppressWarnings("unchecked")
    public void showAlbum() {
        ObservableList<Album> albumsObs = FXCollections.observableList(this.musicCollectionService.listAlbums());

        TableView<Album> table = new TableView<>();
        table.setStyle("-fx-background-color: transparent; -fx-border-color: #ccd;");

        
        TableColumn<Album, String> titleColumn = new TableColumn<>("Titre");
        titleColumn.setMinWidth(300);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setStyle("-fx-font-weight: bold;");

        TableColumn<Album, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setMinWidth(200);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.setStyle("-fx-font-weight: bold;");

        TableColumn<Album, String> releaseDateColumn = new TableColumn<>("Date de sortie");
        releaseDateColumn.setMinWidth(150);
        releaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        releaseDateColumn.setStyle("-fx-font-weight: bold;");

        TableColumn<Album, String> artistColumn = new TableColumn<>("Artiste");
        artistColumn.setMinWidth(200);
        artistColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getArtist() != null ?
                cellData.getValue().getArtist().getName() : "Inconnu")
        );
        artistColumn.setStyle("-fx-font-weight: bold;");

        TableColumn<Album, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setMinWidth(300);
        actionColumn.setCellFactory(createActionCellFactory(this::updateAlbumTable));

        table.getColumns().addAll(titleColumn, genreColumn, releaseDateColumn, artistColumn, actionColumn);
        table.setItems(albumsObs);

       
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par titre, artiste, genre");
        searchField.setStyle(
            "-fx-padding: 10;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #ccc;" +
            "-fx-focus-color: #007bff;" +
            "-fx-pref-width: 300px;" +
            "-fx-border-width: 1;"
        );

      
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Album> filteredAlbums = FXCollections.observableList(
                this.musicCollectionService.searchByTitleArtistGenre(newValue)
            );
            table.setItems(filteredAlbums);
        });

   
        HBox searchBox = new HBox(10, searchField);
        searchBox.setAlignment(Pos.CENTER);

        Label label = new Label("Liste des albums");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Conteneur principal
        VBox vbox = new VBox(10, searchBox, label, table);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxWidth(1200);
        vbox.setStyle(
            "-fx-padding: 20;" +
            "-fx-background-image: url('/icons/back.jpg');" +
            "-fx-background-repeat: no-repeat;" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center -100px;"
        );

        ApplicationView.getRoot().setCenter(vbox);
    }

    private Callback<TableColumn<Album, Void>, TableCell<Album, Void>> createActionCellFactory(Runnable updateAlbumTable) {
        return param -> new TableCell<>() {
            private final Button deleteButton = createTextButton("Supprimer", "#dc3545");
            private final Button editButton = createTextButton("Modifier", "#007bff");
            private final Button detailsButton = createTextButton("Détails", "#606060");

            {
                detailsButton.setOnAction(event -> {
                    Album album = getTableView().getItems().get(getIndex());
                    try {
                        DetailsDialog.showDetailsDialog(album, musicCollectionService, artistRepository);
                        styleDialog();
                    } catch (Exception e) {
                        showErrorDialog("Erreur", "Une erreur est survenue en affichant les détails.");
                    }
                });

                deleteButton.setOnAction(event -> {
                    Album album = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet album ?");
                    alert.setContentText("Cette action est irréversible.");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                musicCollectionService.deleteAlbum(album.getId());
                                updateAlbumTable.run();
                            } catch (AppException e) {
                                showErrorDialog("Erreur lors de la suppression", e.getMessage());
                            }
                        }
                    });
                });

                editButton.setOnAction(event -> {
                    Album album = getTableView().getItems().get(getIndex());
                    try {
                        UpdateDialog.showEditDialog(album, musicCollectionService, artistRepository, updateAlbumTable);
                        styleDialog();
                    } catch (AppException e) {
                        showErrorDialog("Erreur lors de la mise à jour", e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, detailsButton, editButton, deleteButton);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }
            }
        };
    }

    private Button createTextButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + "; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-border-radius: 5; " +
            "-fx-padding: 5 10;"
        );
        return button;
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updateAlbumTable() {
        showAlbum();
    }

    // Applique le style au dialogue
    private void styleDialog() {
        Dialog<?> dialog = new Dialog<>();
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f0f0f0, #e0e0e0); " +
            "-fx-border-color: #ccc; " +
            "-fx-border-radius: 8; " +
            "-fx-padding: 20;"
        );
    }
}
