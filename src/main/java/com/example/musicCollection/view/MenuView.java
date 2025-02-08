package com.example.musicCollection.view;

import org.springframework.stereotype.Component;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

@Component
public class MenuView {

    private final AlbumForm albumForm;
    private final AlbumTable albumTable;

    public MenuView(AlbumForm albumForm, AlbumTable albumTable) {
        this.albumForm = albumForm;
        this.albumTable = albumTable;
    }

    public void showMenu() {
    	
        HBox menuBox = new HBox(15);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle("-fx-background-color: #2196F3; -fx-padding: 4;");

        Button enregistrerAlbumButton = createMenuButton("Enregistrer Album");
        enregistrerAlbumButton.setOnAction(event -> albumForm.showAlbumForm());

        Button listerAlbumsButton = createMenuButton("Lister Albums");
        listerAlbumsButton.setOnAction(event -> albumTable.showAlbum());

        menuBox.getChildren().addAll(enregistrerAlbumButton, listerAlbumsButton);

        ApplicationView.getRoot().setTop(menuBox);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white; -fx-font-size: 14px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;"));
        return button;
    }
}
