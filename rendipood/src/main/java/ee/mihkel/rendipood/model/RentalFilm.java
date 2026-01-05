package ee.mihkel.rendipood.model;

import lombok.Data;

@Data
public class RentalFilm {
    private Long filmId;
    private int rentedDays;
}
