package codingdojo.service;

import codingdojo.CustomerMatches;
import codingdojo.dto.CustomerDto;
import codingdojo.entity.Company;
import codingdojo.entity.Customer;
import codingdojo.entity.ShoppingList;
import codingdojo.enums.CustomerType;
import codingdojo.exception.ConflictException;
import codingdojo.mapper.CompanyMapper;
import codingdojo.mapper.CustomerMapper;
import codingdojo.repository.CompanyRepository;
import codingdojo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {


    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;




    public CustomerMatches loadCompanyCustomer(String externalId, String companyNumber) {
        CustomerMatches matches = new CustomerMatches();
        CustomerDto matchByExternalId =  findByExternalId(externalId);

        if (matchByExternalId != null) {
            matches.setCustomer(matchByExternalId);
            matches.setMatchTerm("ExternalId");
            CustomerDto matchByMasterId = findByMasterExternalId(externalId);
            if (matchByMasterId != null) matches.addDuplicate(matchByMasterId);
        } else {
            Optional<Company> matchByCompanyNumber = findByCompanyNumber(externalId,companyNumber);
            if (matchByCompanyNumber.isPresent()) {
                matches.setCustomer(customerMapper.mapToCustomerDto(matchByCompanyNumber.get()));
                matches.setMatchTerm("CompanyNumber");
            }
        }
        return matches;
    }
    public Optional<Company> findByCompanyNumber(String id, String companyNumber) {
        Company company = companyRepository.findByCompanyNumber(companyNumber);
        if (company == null) {
            return Optional.empty();
        }

        validateCustomerType(company);
        validateExternalId(company, id, companyNumber);

        company.setExternalId(id);
        company.setMasterExternalId(id);

        return Optional.of(company);
    }
    public CustomerMatches loadPersonCustomer(String externalId) {
        CustomerMatches matches = new CustomerMatches();
        CustomerDto matchByPersonalNumber = customerMapper.mapToCustomerDto(customerRepository.findByExternalId(externalId));
        matches.setCustomer(matchByPersonalNumber);
        if (matchByPersonalNumber != null) matches.setMatchTerm("ExternalId");
        return matches;
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
    public CustomerDto updateCustomerRecord(CustomerDto customer) {

        return customerMapper.mapToCustomerDto(customerRepository.update(customerMapper.mapToCustomer(customer)));
    }

    public CustomerDto createCustomerRecord(CustomerDto customerDto) {
        Customer customer = customerMapper.mapToCustomer(customerDto);
        return customerMapper.mapToCustomerDto(customerRepository.create(customer));

    }

    @Override
    public CustomerDto findByExternalId(String externalId) {
        return customerMapper.mapToCustomerDto(customerRepository.findByExternalId(externalId));
    }

    @Override
    public CustomerDto findByMasterExternalId(String externalId) {
        return customerMapper.mapToCustomerDto(customerRepository.findByMasterExternalId(externalId));
    }

    public void updateShoppingList(CustomerDto customer, ShoppingList consumerShoppingList) {
        customer.addShoppingList(consumerShoppingList);

        customerRepository.updateShoppingList(consumerShoppingList);
        customerRepository.update(customerMapper.mapToCustomer(customer));
    }

}
