package ee.mihkel.veebipood.controller;

import ee.mihkel.veebipood.dto.PersonDTO;
import ee.mihkel.veebipood.entity.Person;
import ee.mihkel.veebipood.entity.PersonRole;
import ee.mihkel.veebipood.model.AuthToken;
import ee.mihkel.veebipood.model.LoginCredentials;
import ee.mihkel.veebipood.model.PasswordCredentials;
import ee.mihkel.veebipood.repository.PersonRepository;
import ee.mihkel.veebipood.service.MailService;
import ee.mihkel.veebipood.service.PersonService;
import ee.mihkel.veebipood.util.Validations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PersonService personService;

    @Autowired
    private MailService mailService;

    @GetMapping("persons")
    public List<Person> getPersons() {
        return personRepository.findByOrderByIdAsc();
    }

    @GetMapping("public-persons")
    public List<PersonDTO> getPublicPersons() {

       // mailService.sendPlainText("vahermihkel@gmail.com", "Pealkiri", "Sisu");
        mailService.sendPlainText("arendusmihkel1@gmail.com", "Pealkiri", "Sisu");

//        List<Person> persons = personRepository.findAll();
//        List<PersonDTO> personDTOs = new ArrayList<>();
//        for (Person person : persons) {
//            PersonDTO personDTO = new PersonDTO();
//            personDTO.setFirstName(person.getFirstName());
//            personDTO.setLastName(person.getLastName());
//            personDTOs.add(personDTO);
//        }
//        return personDTOs;

        return List.of(modelMapper.map(personRepository.findAll(), PersonDTO[].class));
    }

    @PostMapping("signup")
    public Person signup(@RequestBody Person person) {
        return personService.savePerson(person);
    }


    @CachePut(value = "personCache", key = "#person.id")
    @PutMapping("persons")
    public Person editPerson(@RequestBody Person person) {
        if (person.getId() == null){
            throw new RuntimeException("Cannot edit when id missing");
        }
        if (person.getEmail() == null || person.getEmail().isBlank()){
            throw new RuntimeException("Email cannot be empty");
        }
        if (!Validations.validateEmail(person.getEmail())){
            throw new RuntimeException("Email is not valid");
        }
        Person existingPerson = personRepository.findById(person.getId()).orElseThrow();
        Person dbPerson = personRepository.findByEmail(person.getEmail());
        if (!existingPerson.getEmail().equals(person.getEmail()) && dbPerson != null){
            throw new RuntimeException("Email already taken");
        }
        person.setPassword(existingPerson.getPassword());
//        if (person.getPassword() == null || person.getPassword().isBlank()){
//            throw new RuntimeException("Password cannot be empty");
//        }
        return personRepository.save(person);
    }

    @PostMapping("login")
    public AuthToken login(@RequestBody LoginCredentials loginCredentials) {
        AuthToken authToken = new AuthToken();
        authToken.setToken(personService.getToken(loginCredentials));
        return authToken;
    }

    @GetMapping("person")
    public Person getPerson() {
        Long personId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return personRepository.findById(personId).orElseThrow();
    }

    @Cacheable(value = "personCache", key = "#id")
    @GetMapping("person/{id}")
    public Person getPerson(@PathVariable Long id) {
        return personRepository.findById(id).orElseThrow();
    }

    @PatchMapping("update-password")
    public Person updatePassword(@RequestBody PasswordCredentials passwordCredentials) {
        if (passwordCredentials.getId() == null){
            throw new RuntimeException("Cannot edit when id missing");
        }
        if (passwordCredentials.getOldPassword() == null){
            throw new RuntimeException("Cannot edit when old password is missing");
        }
        if (passwordCredentials.getNewPassword() == null){
            throw new RuntimeException("Cannot edit when new password is missing");
        }
        return personService.changePassword(passwordCredentials);
    }

    @PatchMapping("change-admin")
    public List<Person> changeAdmin(@RequestParam Long id) {
        Person person = personRepository.findById(id).orElseThrow();
        if (person.getRole().equals(PersonRole.CUSTOMER)){
            person.setRole(PersonRole.ADMIN);
        } else {
            person.setRole(PersonRole.CUSTOMER);
        }
        personRepository.save(person);
        return personRepository.findByOrderByIdAsc();
    }

}
