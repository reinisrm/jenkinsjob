package services.impl;

import models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repos.PersonRepo;
import services.PersonService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepo personRepo;

    @Override
    public List<Person> getAll() {
        return (List<Person>) personRepo.findAll();
    }

    @Override
    public Optional<Person> getPersonById(int personId) {
        return personRepo.findById(personId);
    }

    @Override
    public void createPerson(Person person) {
        if (person != null && person.getPersonId() == 0) {
            personRepo.save(person);
        } else {
            throw new IllegalArgumentException("Invalid person data or person_id already exists");
        }
    }

    @Override
    public void updatePersonById(int personId, Person updatedPersonData) {
        Optional<Person> existingPersonOptional = personRepo.findById(personId);
        if (existingPersonOptional.isPresent()) {
            Person existingPerson = existingPersonOptional.get();
            if (updatedPersonData != null && existingPerson.getPersonId() == personId) {
                existingPerson.setName(updatedPersonData.getName());
                existingPerson.setSurname(updatedPersonData.getSurname());
                existingPerson.setPhoneNumber(updatedPersonData.getPhoneNumber());
                existingPerson.setCourseName(updatedPersonData.getCourseName());
                personRepo.save(existingPerson);
            } else {
                throw new IllegalArgumentException("Invalid person_id or data");
            }
        } else {
            throw new NoSuchElementException("Person not found");
        }
    }

    @Override
    public void deletePersonById(int personId) {
        Optional<Person> existingPersonOptional = personRepo.findById(personId);
        if (existingPersonOptional.isPresent()) {
            personRepo.deleteById(personId);
        } else {
            throw new NoSuchElementException("Person not found");
        }
    }



}
