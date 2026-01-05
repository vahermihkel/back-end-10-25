package ee.mihkel.rendipood.controller;

import ee.mihkel.rendipood.entity.Film;
import ee.mihkel.rendipood.entity.FilmType;
import ee.mihkel.rendipood.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    @GetMapping("films")
    public List<Film> getFilm(){
        return filmRepository.findAll();
    }

    @PostMapping("films")
    public List<Film> addFilm(@RequestBody Film film){
        film.setDays(0);
        film.setRental(null);
        filmRepository.save(film);
        return filmRepository.findAll();
    }

    @PostMapping("add-all-films")
    public List<Film> addAllFilms(@RequestBody List<Film> films){
        filmRepository.saveAll(films);
        return filmRepository.findAll();
    }

    @DeleteMapping("films/{id}")
    public List<Film> removeFilm(@PathVariable Long id){
        filmRepository.deleteById(id);
        return filmRepository.findAll();
    }

//    // Change the type of film
//    @PutMapping("films")
//    public List<Film> updateFilm(@RequestBody Film film){
//        filmRepository.save(film);
//        return filmRepository.findAll();
//    }

    // localhost:8080/change-type?id=1&newType=OLD
    @PatchMapping("change-type")
    public List<Film> changeFilmType(@RequestParam Long id, @RequestParam FilmType newType) {
        Film film = filmRepository.findById(id).orElseThrow();
        film.setType(newType);
        filmRepository.save(film);
        return filmRepository.findAll();
    }

    @GetMapping("available-films")
    public List<Film> availableFilms(){
        return filmRepository.findByDays(0);
    }
}
