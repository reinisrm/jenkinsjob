package services;

import models.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<Person> getAll();
    void createPerson(Person person);
    Optional<Person> getPersonById(int personId);
    void updatePersonById(int personId, Person updatedPersonData);
    void deletePersonById(int personId);
}
