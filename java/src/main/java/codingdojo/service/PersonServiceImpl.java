package codingdojo.service;


import codingdojo.CustomerMatches;
import codingdojo.entity.Person;
import codingdojo.entity.ShoppingList;
import codingdojo.enums.CustomerType;
import codingdojo.exception.ConflictException;
import codingdojo.repository.CustomerRepository;
import codingdojo.repository.PersonRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final CustomerRepository customerRepository;

    private final PersonRepository personRepository;

    private final CustomerMatches customerMatches;


    @Override
    public Optional<Person> load(String id) {
            Person person = personRepository.findByExternalId(id);
        if (person == null) {
            return Optional.empty();
        }

        if (!CustomerType.COMPANY.equals(person.getCustomerType())) {
            throw new ConflictException("Existing customer for externalCustomer " + id + " already exists and is not a person");
        }

        return Optional.of(person);
    }

    @Override
    public Person create(Person person) {
        return personRepository.create(person);

    }

    @Override
    public void update(Person person) {
        personRepository.update(person);
    }


}
