package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.mappers.PersonMapper;
import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.models.dto.PersonDTO;
import com.example.inventorysystem.repos.PersonRepo;
import com.example.inventorysystem.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);
    private final PersonRepo personRepo;

    public PersonServiceImpl(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public List<PersonDTO> getAll() {
        List<Person> peopleList = personRepo.findAll();
        log.info("Fetched all persons. Count: {}", peopleList.size());
        return peopleList.stream()
                .map(PersonMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<PersonDTO> getPersonById(int personId) {
        Optional<Person> personOptional = personRepo.findById(personId);
        if (personOptional.isPresent()) {
            log.info("Person found with ID: {}", personId);
        } else {
            log.warn("Person with ID {} not found", personId);
        }
        return personOptional.map(PersonMapper::toDTO);
    }

    @Override
    public void createPerson(PersonDTO dto) {
        try {
            Person person = PersonMapper.toEntity(dto);
            personRepo.save(person);
            log.info("Created new person: {}", person);
        } catch (Exception e) {
            log.error("Error while creating person", e);
        }
    }

    @Override
    public void updatePersonById(int personId, PersonDTO dto) {
        try {
            Optional<Person> personOpt = personRepo.findById(personId);
            if (personOpt.isEmpty()) {
                throw new NoSuchElementException("Person not found");
            }

            Person person = personOpt.get();
            log.info("Updating person with ID: {}", personId);

            person.setName(dto.getName());
            person.setSurname(dto.getSurname());
            person.setPhoneNumber(dto.getPhoneNumber());
            person.setCourseName(dto.getCourseName());
            Person updated = personRepo.save(person);

            log.info("Updated person: {}", updated);
        } catch (Exception e) {
            log.error("Error updating person with ID: {}", personId, e);
        }
    }

    @Override
    public void deletePersonById(int personId) {
        try {
            Optional<Person> personOpt = personRepo.findById(personId);
            if (personOpt.isEmpty()) {
                log.warn("Person with ID {} not found for deletion", personId);
                return;
            }
            personRepo.deleteById(personId);
            log.info("Deleted person with ID: {}", personId);
        } catch (Exception e) {
            log.error("Error deleting person with ID: {}", personId, e);
        }
    }
}
