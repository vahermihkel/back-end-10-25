package ee.mihkel.rendipood.controller;

import ee.mihkel.rendipood.entity.Film;
import ee.mihkel.rendipood.entity.Rental;
import ee.mihkel.rendipood.model.RentalFilm;
import ee.mihkel.rendipood.repository.FilmRepository;
import ee.mihkel.rendipood.repository.RentalRepository;
import ee.mihkel.rendipood.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RentalController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalService rentalService;

    @GetMapping("rentals")
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    @PostMapping("start-rental")
    public Rental startRental(@RequestBody List<RentalFilm> rentalFilms) {

        Rental rental = new Rental();
        List<Film> films = new ArrayList<>();

        double sum = rentalService.getSumAndAddRentalToFilm(rentalFilms, rental, films);

        rental.setInitialFee(sum);
        rental.setFilms(films);

        return rentalRepository.save(rental);
    }

    @PostMapping("end-rental")
    public Rental endRental(@RequestBody List<RentalFilm> rentalFilms) {

//        List<Film> films = rentalService.getAllFilmsFromDb(rentalFilms);

//        Rental rental = rentalService.checkIfAllFilmsFromSameRental(films);

        Rental rental = rentalService.calculateLateFee(rentalFilms);

        return rentalRepository.save(rental);
    }



}
