package com.example.musicCollection.view.dialog;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.example.musicCollection.model.Album;
import com.example.musicCollection.model.Artist;
import com.example.musicCollection.repository.ArtistRepository;
import com.example.musicCollection.service.MusicCollectionService;

public class UpdateDialog {
    public static void showEditDialog(Album album, MusicCollectionService musicCollectionService, ArtistRepository artistRepository, Runnable updateAlbumTable) {
        Dialog<Album> dialog = new Dialog<>();
        dialog.setTitle("Modifier l'Album");
        dialog.setHeaderText("Modifiez les informations de l'album :");

        // Champs du formulaire
        TextField titleField = new TextField(album.getTitle());
        TextField genreField = new TextField(album.getGenre());
        TextField artistField = new TextField(album.getArtist() != null ? album.getArtist().getName() : "");
        DatePicker releaseDatePicker = new DatePicker(album.getReleaseDate());

        VBox dialogContent = new VBox(10);
        dialogContent.setStyle("-fx-padding: 20;");
        dialogContent.getChildren().addAll(
            new Label("Titre de l'album :"), titleField,
            new Label("Genre :"), genreField,
            new Label("Nom de l'artiste :"), artistField,
            new Label("Date de sortie :"), releaseDatePicker
        );

        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (titleField.getText().trim().isEmpty() || genreField.getText().trim().isEmpty() ||
                    artistField.getText().trim().isEmpty() || releaseDatePicker.getValue() == null) {
                    new Alert(Alert.AlertType.WARNING, "Tous les champs doivent être remplis.").showAndWait();
                    return null;
                }

                album.setTitle(titleField.getText().trim());
                album.setGenre(genreField.getText().trim());
                album.setReleaseDate(releaseDatePicker.getValue());

                Artist artist = artistRepository.findByName(artistField.getText().trim());
                if (artist == null) {
                    artist = new Artist();
                    artist.setName(artistField.getText().trim());
                    artistRepository.save(artist);
                }
                album.setArtist(artist);

                try {
                    musicCollectionService.updateAlbum(album.getId(), album);
                    updateAlbumTable.run();
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour de l'album : " + e.getMessage()).showAndWait();
                }
            }
            return null;
        });

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

	
}
