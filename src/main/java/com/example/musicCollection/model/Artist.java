package com.example.musicCollection.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table
public class Artist {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long artist_id;
	@Column
	private String name;
	
	
	@OneToMany(mappedBy="artist", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<Album> albums;
	
	public long getId() {
		return artist_id;
	}
	public void setId(long id) {
		this.artist_id = id;
	}
	
	
	public long getArtist_id() {
		return artist_id;
	}
	public void setArtist_id(long artist_id) {
		this.artist_id = artist_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Album> getAlbums() {
		return albums;
	}
	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
	public void addAlbum(Album album) {
		if(this.albums==null) {
			this.albums=new  ArrayList<Album>();
		}
		this.albums.add(album);
		album.setArtist(this);
	}
}
