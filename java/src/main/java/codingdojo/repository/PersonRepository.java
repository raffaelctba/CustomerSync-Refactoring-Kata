package codingdojo.repository;


import codingdojo.entity.Person;
import codingdojo.entity.ShoppingList;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository {

    Person update(Person person);

    Person create(Person person);

    void updateShoppingList(ShoppingList consumerShoppingList);

    Person findByExternalId(String externalId);

    Person findByMasterExternalId(String externalId);

}
