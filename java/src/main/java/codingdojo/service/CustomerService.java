package codingdojo.service;

import codingdojo.dto.CustomerDto;
import codingdojo.entity.Company;
import codingdojo.entity.Customer;

import java.util.Optional;

public interface CustomerService {

    CustomerDto updateCustomerRecord(CustomerDto customer);

    CustomerDto createCustomerRecord(CustomerDto customer);

    CustomerDto findByExternalId(String externalId);

    CustomerDto findByMasterExternalId(String externalId);

    Optional<Company> findByCompanyNumber(String id, String companyNumber);


}
