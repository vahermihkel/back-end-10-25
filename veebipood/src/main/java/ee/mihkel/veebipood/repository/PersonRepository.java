package ee.mihkel.veebipood.repository;

import ee.mihkel.veebipood.entity.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByEmail(String email);

    List<Person> findByOrderByIdAsc();

}
