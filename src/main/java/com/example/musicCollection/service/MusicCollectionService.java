package com.example.musicCollection.service;

import com.example.musicCollection.model.Album;


import java.time.LocalDate;
import java.util.List;

public interface MusicCollectionService {

    Album addAlbum(Album album);
    void deleteAlbum(Long albumId);
    Album updateAlbum(Long albumId, Album album);
    List<Album> searchByTitleArtistGenre(String text);
    List<Album> searchByReleaseDate(LocalDate date);
    List<Album> listAlbums();
   
}
