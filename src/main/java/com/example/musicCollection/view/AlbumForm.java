package com.example.musicCollection.view;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.example.musicCollection.model.Album;
import com.example.musicCollection.model.Artist;
import com.example.musicCollection.repository.AlbumRepository;
import com.example.musicCollection.repository.ArtistRepository;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class AlbumForm {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumTable albumTable;

    public AlbumForm(AlbumRepository albumRepository, ArtistRepository artistRepository, AlbumTable albumTable) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.albumTable = albumTable;
    }

    public void showAlbumForm() {
        Stage stage = new Stage();
        stage.setTitle("Enregistrer un Album");
        
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox formLayout = new VBox(15);
        formLayout.setPadding(new Insets(20));
        formLayout.setStyle("-fx-background-color: #0B173D;");
        formLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Enregistrer un Album");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField titleField = createStyledTextField("Titre de l'album");
        TextField genreField = createStyledTextField("Genre de l'album");
        TextField artistField = createStyledTextField("Nom de l'artiste");

        DatePicker releaseDatePicker = new DatePicker();
        releaseDatePicker.setPromptText("Date de sortie (YYYY-MM-DD)");
        releaseDatePicker.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

        Button saveButton = createStyledButton("Enregistrer", "#4CAF50", "#45A049");
        saveButton.setOnAction(event -> {
            errorLabel.setText("");

            if (titleField.getText().isEmpty() || genreField.getText().isEmpty() || artistField.getText().isEmpty() || releaseDatePicker.getValue() == null) {
                errorLabel.setText("Tous les champs doivent être remplis !");
                return;
            }

            LocalDate releaseDateValue = releaseDatePicker.getValue();
            Optional<Artist> existingArtist = Optional.ofNullable(artistRepository.findByName(artistField.getText()));
            Artist artist = existingArtist.orElseGet(() -> {
                Artist newArtist = new Artist();
                newArtist.setName(artistField.getText());
                return artistRepository.save(newArtist);
            });

            Album album = new Album();
            album.setTitle(titleField.getText());
            album.setGenre(genreField.getText());
            album.setReleaseDate(releaseDateValue);
            album.setArtist(artist);

            albumRepository.save(album);
            albumTable.updateAlbumTable();
            stage.close();
        });

        Button cancelButton = createStyledButton("Annuler", "#f44336", "#d32f2f");
        cancelButton.setOnAction(event -> stage.close());

        HBox buttonBox = new HBox(15, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(10, 0, 10, 0));
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(createStyledLabel("Titre de l'album :"), 0, 0);
        formGrid.add(titleField, 1, 0);
        formGrid.add(createStyledLabel("Genre de l'album :"), 0, 1);
        formGrid.add(genreField, 1, 1);
        formGrid.add(createStyledLabel("Nom de l'artiste :"), 0, 2);
        formGrid.add(artistField, 1, 2);
        formGrid.add(createStyledLabel("Date de sortie :"), 0, 3);
        formGrid.add(releaseDatePicker, 1, 3);

        formLayout.getChildren().addAll(titleLabel, formGrid, errorLabel, buttonBox);

        Scene scene = new Scene(formLayout, 500, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: #ccc;" +
            "-fx-background-radius: 5;"
        );
        return textField;
    }

    private Button createStyledButton(String text, String backgroundColor, String hoverColor) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;"
        );
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: white;"));
        return button;
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        return label;
    }
}