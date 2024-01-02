package controllers;

import models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import services.impl.PersonServiceImpl;
import org.springframework.ui.Model;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonServiceImpl personService;

    @GetMapping("/")
    public String showAllPerson(Model model) {
        List<Person> persons = personService.getAll();
        model.addAttribute("persons", persons);
        return "show-all-person";
    }

    @GetMapping("/{personId}")
    public String showOnePerson(@PathVariable("personId") int personId, Model model) {
        Optional<Person> person = personService.getPersonById(personId);
        model.addAttribute("person", person);
        return "show-one-person";
    }

    @GetMapping("/create")
    public String createPersonForm(Model model) {
        model.addAttribute("person", new Person());
        return "create-person";
    }

    @PostMapping("/create")
    public String createPerson(@Valid Person person, BindingResult result) {
        if (result.hasErrors()) {
        return "create-person";
        }
        Person temp = new Person(
                person.getName(),
                person.getSurname(),
                person.getPhoneNumber(),
                person.getCourseName()
        );

        personService.createPerson(temp);
        return "redirect:/person";
    }


    @GetMapping("/update/{personId}")
    public String showUpdateForm(@PathVariable("personId") int personId, Model model) {
         Optional<Person> person = personService.getPersonById(personId);
         model.addAttribute("person", person);
         return "person-update-page";
    }
    @PostMapping("/update/{personId}")
    public String updatePersonById(@PathVariable("personId") int personId, @Valid Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "person-update-page";
        } else {
            personService.updatePersonById(personId, person);
            return "redirect:/person/{personId}";
        }
    }


    @DeleteMapping("/delete/{personId}")
    public String deletePersonById(@PathVariable("personId") int personId) {
        personService.deletePersonById(personId);
        return "redirect:/person";
    }







}
