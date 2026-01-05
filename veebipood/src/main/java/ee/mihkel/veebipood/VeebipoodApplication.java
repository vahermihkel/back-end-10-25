package ee.mihkel.veebipood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
public class VeebipoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeebipoodApplication.class, args);
	}

}

// 1. Spring algus - controller, entity, andmebaas
// 2. Veateated, Order, Person
// 3. Rendipood algus
// 4. Rendipood jätk
// 5. Unit Testing, Front-end
// KOJU:
// a) Unit Testid lõpetada rendipoes
// b) Unit Teste proovida kaardimängus
// c) Frontend luua Rendipoes + tsükkel filmide mudeli järgi (copy-paste andmed)
// d) Frontend luua Kaardimängus + kaardi välja näitamine (copy-paste andmed)
// 6. 14.11 R 9.00 Front-end
// 7. 17.11 E 9.00 Custom Repository päringud, localStorage
// 8. 18.11 T 9.00 Ostukorvi kogus. Ostukorvi kogusumma Navbaris. ModelMapper.
// 9. 26.11 K 9.00  API päringud: Supplier, Pakiautomaadid. @Autowired.
//10. 01.12 E 9.00 makse lõpetamine, SecurityConfig + JWT
//11. 03.12 K 9.00 auth Reacti context.
//12. 05.12 R 9.00 auth. token frontendis, veateated tõlkes. profile. parooli hashimine, login, signup
//13. 08.12 E 9.00 auth. parooli muutmine. custom hook. ainult sinu tellimused. lisamisel token, muutmisel token
//14. 11.12 N 9.00 auth. rollid: Admin, Superadmin. staatusi muuta
//15. 15.12 E 9.00 Järgmine nupp kinni, teade toodet lisades (kui õnnestub/ei õnnestu), Guava cache
//16. 17.12 K 9.00 redis cache, email, CRON, shell-script, automaatselt DB tekitab lisamise/muutmise, logid
//17. 19.12 R 9.00 back-end serverisse üles, front-end serverisse üles, erinevad keskkonnad,
//                  taaskasutamine front-endis tegi punaseks
//17. 23.12 T 9.00-10.00 front-end serverisse üles + taaskasutamine front-endis tegi punaseks
// KOJU: lõpuprojekt teha
//18. 07.01 K 12.00-13.30 ---> lõpuprojekti esitlemine

// LinkedIn
// MeetFrank
// Ettevõtete kodulehelt kontakt


// veahaldus???
// pakiautomaadid ostukorvis
// admin vaates toote kustutamine/ toote muutmine

// ERR_CONNECTION_REFUSED <-- ei tööta localhost:6379
// EMPTY_RESPONSE <-- töötab 6379
// proovige Dockeris redis alla laadida
