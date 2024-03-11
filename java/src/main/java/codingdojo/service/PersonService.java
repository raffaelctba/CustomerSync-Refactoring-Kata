package codingdojo.service;



import codingdojo.entity.Person;
import codingdojo.entity.ShoppingList;

import java.util.Optional;


public interface PersonService {

    Optional<Person> load(String id);

    Person create(Person person);

    void update(Person person);


}
