package ee.mihkel.rendipood.repository;

import ee.mihkel.rendipood.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film,Long> {

    // SELECT * FROM films WHERE days = 0
    List<Film> findByDays(int days);

//    @Query("select * from films")
//    List<Film> leiaPaevadeJargi(int days);
}
