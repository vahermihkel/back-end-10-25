package ee.mihkel.rendipood.service;

import ee.mihkel.rendipood.entity.Film;
import ee.mihkel.rendipood.entity.FilmType;
import ee.mihkel.rendipood.entity.Rental;
import ee.mihkel.rendipood.model.RentalFilm;
import ee.mihkel.rendipood.repository.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RentalServiceTest {

    @Mock
    FilmRepository filmRepository;

    @InjectMocks
    RentalService rentalService;

    List<RentalFilm> rentalFilms = new ArrayList<>();
    Rental rental = new Rental();
    List<Film> films = new ArrayList<>();

    @BeforeEach
    void setUp() {
        addFilmToMockDatabase(15L, FilmType.REGULAR);
        addFilmToMockDatabase(16L, FilmType.NEW);
        addFilmToMockDatabase(17L, FilmType.OLD);
        addFilmToMockDatabase(18L, FilmType.NEW);

        when(filmRepository.save(any())).thenReturn(new Film());
    }

    private void addFilmToMockDatabase(Long id, FilmType filmType) {
        Film film = new Film();
        film.setId(id);
        film.setTitle("Film");
        film.setType(filmType);
        if (film.getId() == 18L) {
            film.setDays(1);
            film.setRental(rental);
        }
        when(filmRepository.findById(id)).thenReturn(Optional.of(film));
    }

    @Test
    void givenRegularFilmAnd3Days_whenStartingRental_thenSumIs3() {
        addFilmToRentalFilms(15L, 3);

        double sum = rentalService.getSumAndAddRentalToFilm(rentalFilms, rental, films);

        assertEquals(3, sum);
    }

    @Test
    void givenNewFilmAnd3Days_whenStartingRental_thenSumIs12() {
        addFilmToRentalFilms(16L, 3);

        double sum = rentalService.getSumAndAddRentalToFilm(rentalFilms, rental, films);

        assertEquals(12, sum);
    }

    @Test
    void givenNewFilmOldFilmAndRegularFilmFor5Days_whenStartingRental_thenSumIs12() {
        addFilmToRentalFilms(15L, 5);
        addFilmToRentalFilms(16L, 5);
        addFilmToRentalFilms(17L, 5);

        double sum = rentalService.getSumAndAddRentalToFilm(rentalFilms, rental, films);

        assertEquals(12, sum);
    }

    @Test
    void givenRentedDaysAre0_whenStartingRental_thenErrorIsThrown() {
        addFilmToRentalFilms(16L, 0);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> rentalService.getSumAndAddRentalToFilm(rentalFilms, rental, films));

        assertEquals("Rental days must be greater than 0!", exception.getMessage());
    }

    @Test
    void givenFilmIsAlreadyRented_whenStartingRental_thenErrorIsThrown() {
        addFilmToRentalFilms(18L, 1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> rentalService.getSumAndAddRentalToFilm(rentalFilms, rental, films));

        assertEquals("Film is already rented!", exception.getMessage());
    }

    private void addFilmToRentalFilms(long filmId, int rentedDays) {
        RentalFilm rentalFilm = new RentalFilm();
        rentalFilm.setFilmId(filmId);
        rentalFilm.setRentedDays(rentedDays);
        rentalFilms.add(rentalFilm);
    }

    @Test
    void givenNewFilmIs2DaysLate_whenReturningFilm_thenLateFeeIs8() {
        addFilmToRentalFilms(18L, 3);

        double sum = rentalService.calculateLateFee(rentalFilms).getLateFee();

        assertEquals(8, sum);
    }
}