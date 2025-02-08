package com.example.musicCollection.service.impl;

import com.example.musicCollection.exception.AppException;
import com.example.musicCollection.model.Album;
import com.example.musicCollection.repository.AlbumRepository;
import com.example.musicCollection.service.MusicCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MusicCollectionServiceImpl implements MusicCollectionService {

    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public Album addAlbum(Album album) {
        try {
            return albumRepository.save(album);
        } catch (Exception e) {
            throw new AppException("Erreur lors de l'ajout de l'album", e);
        }
    }

    @Override
    public void deleteAlbum(Long albumId) {
        try {
            Optional<Album> album = albumRepository.findById(albumId);
            if (album.isPresent()) {
                albumRepository.delete(album.get());
                System.out.println("Album supprimé : " + album.get().getTitle());
            } else {
                throw new AppException("Album non trouvé pour la suppression");
            }
        } catch (Exception e) {
            throw new AppException("Erreur lors de la suppression de l'album", e);
        }
    }

    @Override
    public Album updateAlbum(Long albumId, Album updatedAlbum) {
        try {
            Optional<Album> albumOptional = albumRepository.findById(albumId);
            if (albumOptional.isPresent()) {
                Album album = albumOptional.get();
                album.setTitle(updatedAlbum.getTitle());
                album.setGenre(updatedAlbum.getGenre());
                album.setReleaseDate(updatedAlbum.getReleaseDate());
                album.setArtist(updatedAlbum.getArtist());
                return albumRepository.save(album);
            } else {
                throw new AppException("Album non trouvé pour la mise à jour");
            }
        } catch (Exception e) {
            throw new AppException("Erreur lors de la mise à jour de l'album", e);
        }
    }

    @Override
    public List<Album> searchByTitleArtistGenre(String text) {
        try {
            String searchText = text.toLowerCase().replaceAll("\\s+", "");
            return albumRepository.findAll().stream()
                .filter(album -> album.getTitle().toLowerCase().replaceAll("\\s+", "").contains(searchText) || 
                                 album.getArtist().getName().toLowerCase().replaceAll("\\s+", "").contains(searchText) || 
                                 album.getGenre().toLowerCase().replaceAll("\\s+", "").contains(searchText)) 
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new AppException("Erreur lors de la recherche d'album", e);
        }
    }

    public List<Album> searchByReleaseDate(LocalDate date) {
        try {
            return albumRepository.findAll().stream()
                .filter(album -> album.getReleaseDate().equals(date))
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new AppException("Erreur lors de la recherche par date de sortie", e);
        }
    }

    @Override
    public List<Album> listAlbums() {
        try {
            return albumRepository.findAll();
        } catch (Exception e) {
            throw new AppException("Erreur lors de la récupération de la liste des albums", e);
        }
    }
}
