package codingdojo.repository;

import codingdojo.entity.ShoppingList;
import codingdojo.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository {

    Customer update(Customer customer);

    Customer create(Customer customer);

    void updateShoppingList(ShoppingList consumerShoppingList);

    Customer findByExternalId(String externalId);

    Customer findByMasterExternalId(String externalId);

    Customer findByCompanyNumber(String companyNumber);
}
