package codingdojo.repository;

import codingdojo.entity.Company;
import codingdojo.entity.ShoppingList;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository {

    Company update(Company company);

    Company create(Company company);

    void updateShoppingList(ShoppingList consumerShoppingList);

    Company findByExternalId(String externalId);

    Company findByMasterExternalId(String externalId);

    Company findByCompanyNumber(String companyNumber);
}
