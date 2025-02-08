package com.example.musicCollection.repository;

import com.example.musicCollection.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByTitleContaining(String title);
    List<Album> findByGenreContaining(String genre);
    List<Album> findByArtistNameContaining(String artistName);
    List<Album> findByReleaseDate(LocalDate releaseDate);
}
