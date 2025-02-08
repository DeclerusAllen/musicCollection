package com.example.musicCollection.view;

import org.springframework.stereotype.Component;

import javafx.scene.image.Image;


@Component
public class IconLoader{

	public Image getIcon() {
		 return new Image(IconLoader.class.getClassLoader().getResourceAsStream("icons/R.jpg"));
	}

}
