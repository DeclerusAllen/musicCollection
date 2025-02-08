package com.example.musicCollection.view.dialog;

import com.example.musicCollection.model.Album;
import com.example.musicCollection.repository.ArtistRepository;
import com.example.musicCollection.service.MusicCollectionService;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DetailsDialog {

    public static void showDetailsDialog(Album album, MusicCollectionService musicCollectionService, ArtistRepository artistRepository) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Détails de l'album");
        alert.setHeaderText("Détails de l'album : " + album.getTitle());

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 20;");

        Text titleText = new Text("Titre: " + album.getTitle());
        Text genreText = new Text("Genre: " + album.getGenre());
        Text releaseDateText = new Text("Date de sortie: " + (album.getReleaseDate() != null ? album.getReleaseDate() : "Non spécifiée"));
        Text artistText = new Text("Artiste: " + (album.getArtist() != null ? album.getArtist().getName() : "Inconnu"));

        vbox.getChildren().addAll(titleText, genreText, releaseDateText, artistText);
        alert.getDialogPane().setContent(vbox);

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }

	
}
