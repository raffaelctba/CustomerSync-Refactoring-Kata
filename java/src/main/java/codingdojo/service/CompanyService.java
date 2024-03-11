package codingdojo.service;



import codingdojo.entity.Company;
import codingdojo.entity.ShoppingList;

import java.util.Optional;


public interface CompanyService {

    Optional<Company> loadCompanyWithPossibleDuplicates(String id, String companyNumber);

    Company create(Company company);

    void update(Company company);


    Optional<Company> findByCompanyNumber(String id, String companyNumber);
}
