package ee.mihkel.rendipood.service;

import ee.mihkel.rendipood.entity.Film;
import ee.mihkel.rendipood.entity.FilmType;
import ee.mihkel.rendipood.entity.Rental;
import ee.mihkel.rendipood.model.RentalFilm;
import ee.mihkel.rendipood.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private FilmRepository filmRepository;

    private final int premium_price = 4;
    private final int basic_price = 3;
    private final int regular_free_days = 3;
    private final int old_free_days = 5;

    public double getSumAndAddRentalToFilm(List<RentalFilm> rentalFilms, Rental rental, List<Film> films) {
        double sum = 0;
        for (RentalFilm rentalFilm : rentalFilms) {
            if (rentalFilm.getRentedDays() <= 0) {
                throw new RuntimeException("Rental days must be greater than 0!");
            }
            Film film = filmRepository.findById(rentalFilm.getFilmId()).orElseThrow();
            if (film.getDays() > 0) {
                throw new RuntimeException("Film is already rented!");
            }
            sum += calculateFilmSum(film.getType(), rentalFilm.getRentedDays());
//             film.setRental(dbRental);
            film.setDays(rentalFilm.getRentedDays());
            film.setRental(rental);
            films.add(film);
        }
        return sum;
    }

    private double calculateFilmSum(FilmType filmType, int rentedDays) {
        switch (filmType) {
            case NEW -> {
                return premium_price * rentedDays;
            }
            case REGULAR -> {
                if (rentedDays <= regular_free_days) {
                    return basic_price;
                }
                return basic_price + (rentedDays-regular_free_days) * basic_price;
            }
            case OLD -> {
                if (rentedDays <= old_free_days) {
                    return basic_price;
                }
                return basic_price + (rentedDays-old_free_days) * basic_price;
            }
        }
        return 0;
    }


    public Rental calculateLateFee(List<RentalFilm> rentalFilms) {
        double sum = 0;
        Rental rental = filmRepository.findById(rentalFilms.getFirst().getFilmId()).orElseThrow().getRental();
        if (rental == null) {
            throw new RuntimeException("Film has not been rented!");
        }
        for (RentalFilm rentalFilm : rentalFilms) {
            Film film = filmRepository.findById(rentalFilm.getFilmId()).orElseThrow();
            if (rental != film.getRental()) {
                throw new RuntimeException("Films are from different rentals!");
            }
            switch (film.getType()) {
                case NEW -> {
                    sum += (rentalFilm.getRentedDays() - film.getDays()) * premium_price;
                }
                case REGULAR, OLD -> {
                    sum += (rentalFilm.getRentedDays() - film.getDays()) * basic_price;
                }
            }
            film.setDays(0);
            film.setRental(null);
            filmRepository.save(film);
        }
        rental.setLateFee(sum);
        return rental;
    }


}
