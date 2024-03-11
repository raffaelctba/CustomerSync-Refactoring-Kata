package codingdojo.service;

import codingdojo.CustomerMatches;
import codingdojo.entity.Company;
import codingdojo.entity.Customer;
import codingdojo.entity.ShoppingList;
import codingdojo.enums.CustomerType;
import codingdojo.exception.ConflictException;
import codingdojo.mapper.CompanyMapper;
import codingdojo.mapper.CustomerMapper;
import codingdojo.repository.CompanyRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {


    private final CompanyRepository companyRepository;

    private final CustomerMapper customerMapper;
    private final CompanyMapper companyMapper;


    @Override
    public Optional<Company> loadCompanyWithPossibleDuplicates(String id, String companyNumber) {
        Company company = companyRepository.findByExternalId(id);
        if (company != null) {
            Optional<CustomerMatches> customerMatches = findDuplicates(company, id, companyNumber);
            if(customerMatches.isPresent()){
                // If duplicates found, return the associated customer
                return Optional.ofNullable(companyMapper.mapToCompany(customerMatches.get().getCustomer()));
            }

        }
        return findByCompanyNumber(id, companyNumber);
    }

    public Optional<CustomerMatches> findDuplicates(Company company, String id, String companyNumber) {
        CustomerMatches customerMatches = new CustomerMatches();
        Company duplicate = companyRepository.findByMasterExternalId(id);
        if (duplicate != null) {

            customerMatches.addDuplicate(customerMapper.mapToCustomerDto(duplicate));
        }

        if (!companyNumber.equals(company.getCompanyNumber())) {
            company.setMasterExternalId(null);
            customerMatches.addDuplicate(customerMapper.mapToCustomerDto(company));
        }

        return Optional.of(customerMatches);
    }

    public Optional<Company> findByCompanyNumber(String id, String companyNumber) {
        Company company = companyRepository.findByCompanyNumber(companyNumber);
        if (company == null) {
            return Optional.empty();
        }

        validateCustomerType(company);
        validateExternalId(company, id, companyNumber);

        return Optional.of(company);
    }

    private void validateCustomerType(Customer customer) {
        if (!CustomerType.COMPANY.equals(customer.getCustomerType())) {
            throw new ConflictException("Existing customer for externalCustomer " + customer.getExternalId() + " already exists and is not a company");
        }
    }

    private void validateExternalId(Customer customer, String id, String companyNumber) {
        if (!customer.getExternalId().equals(id)) {
            throw new ConflictException("Existing customer for externalCustomer " + companyNumber + " doesn't match external id " + id + " instead found "
                  + customer.getExternalId());
        }
    }

    @Override
    public Company create(Company company) {
       return companyRepository.create(company);
    }

    @Override
    public void update(Company company) {
        companyRepository.update(company);
    }



}
