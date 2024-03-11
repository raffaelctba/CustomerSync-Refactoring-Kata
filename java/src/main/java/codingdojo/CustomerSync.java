package codingdojo;

import codingdojo.dto.CustomerDto;
import codingdojo.dto.ExternalCustomer;
import codingdojo.entity.Customer;
import codingdojo.entity.ShoppingList;
import codingdojo.entity.Person;
import codingdojo.enums.CustomerType;
import codingdojo.exception.ConflictException;
import codingdojo.mapper.CompanyMapper;
import codingdojo.mapper.CustomerMapper;
import codingdojo.mapper.PersonMapper;
import codingdojo.service.CustomerServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class CustomerSync {

    private final CustomerServiceImpl customerService;
    private final CustomerMapper customerMapper;
    private final PersonMapper personMapper;
    private final CompanyMapper companyMapper;


    @Bean
    public boolean syncWithDataLayer(ExternalCustomer externalCustomer) {

        CustomerMatches customerMatches;
        if (externalCustomer.isCompany()) {

            customerMatches = loadCompany(externalCustomer);
        } else {
            customerMatches = loadPerson(externalCustomer);
        }
        CustomerDto customer = customerMatches.getCustomer();

        if (customer == null) {
            customer =  customerMapper.mapToCustomerDto(externalCustomer);
        }

        if (customerMatches.hasDuplicates()) {
            for (CustomerDto duplicate : customerMatches.getDuplicates()) {
                updateDuplicate(externalCustomer, duplicate);
            }
        }

        updateRelations(externalCustomer, customer);

        boolean created = false;
        if (customer.getInternalId() == null) {
            customer = createCustomer(customer);
            created = true;
        } else {
            updateCustomer(customer);

        }


        return created;
    }

    private void updateRelations(ExternalCustomer externalCustomer, CustomerDto customer) {
        List<ShoppingList> consumerShoppingLists = externalCustomer.getShoppingLists();
        for (ShoppingList consumerShoppingList : consumerShoppingLists) {
            customerService.updateShoppingList(customer, consumerShoppingList);
        }
    }

    private CustomerDto updateCustomer(CustomerDto customer) {
        return customerService.updateCustomerRecord(customer);
    }

    private void updateDuplicate(ExternalCustomer externalCustomer, CustomerDto duplicate) {
        CustomerDto customerDto = customerMapper.mapToCustomerDto(externalCustomer);

        if (customerDto.getInternalId() == null) {
            createCustomer(customerDto);
        } else {
            updateCustomer(customerDto);
        }
    }

    private void updatePreferredStore(ExternalCustomer externalCustomer, Customer customer) {
        customer.setPreferredStore(externalCustomer.getPreferredStore());
    }

    private CustomerDto createCustomer(CustomerDto customer) {
        return this.customerService.createCustomerRecord(customer);
    }

    private void populateFields(ExternalCustomer externalCustomer, Customer customer) {

        if (externalCustomer.isCompany()) {
            customerMapper.mapToCustomerDto(externalCustomer);
        } else {
           personMapper.mapToPerson(externalCustomer);
        }
    }

    private void updateContactInfo(ExternalCustomer externalCustomer, CustomerDto customer) {
        customer.setAddress(externalCustomer.getPostalAddress());
    }

    public CustomerMatches loadCompany(ExternalCustomer externalCustomer) {

        final String externalId = externalCustomer.getExternalId();
        final String companyNumber = externalCustomer.getCompanyNumber();

        CustomerMatches customerMatches = customerService.loadCompanyCustomer(externalId, companyNumber);

        if (customerMatches.getCustomer() != null && !CustomerType.COMPANY.equals(customerMatches.getCustomer().getCustomerType())) {
            throw new ConflictException("Existing customer for externalCustomer " + externalId + " already exists and is not a company");
        }

        if ("ExternalId".equals(customerMatches.getMatchTerm())) {
            String customerCompanyNumber = customerMatches.getCustomerDto().getCompanyNumber();
            if (!companyNumber.equals(customerCompanyNumber)) {
                customerMatches.getCustomer().setMasterExternalId(null);
                customerMatches.addDuplicate(customerMatches.getCustomer());
                customerMatches.setCustomer(null);
                customerMatches.setMatchTerm(null);
            }
        } else if ("CompanyNumber".equals(customerMatches.getMatchTerm())) {
            String customerExternalId = customerMatches.getCustomer().getExternalId();
            if (customerExternalId != null && !externalId.equals(customerExternalId)) {
                throw new ConflictException("Existing customer for externalCustomer " + companyNumber + " doesn't match external id " + externalId + " instead found " + customerExternalId );
            }
            CustomerDto customer = customerMatches.getCustomerDto();
            customer.setExternalId(externalId);
            customer.setMasterExternalId(externalId);
            customerMatches.addDuplicate(null);
        }

        return customerMatches;
    }

    public CustomerMatches loadPerson(ExternalCustomer externalCustomer) {
        final String externalId = externalCustomer.getExternalId();

        CustomerMatches customerMatches = customerService.loadPersonCustomer(externalId);

        if (customerMatches.getCustomer() != null) {
            if (!CustomerType.PERSON.equals(customerMatches.getCustomer().getCustomerType())) {
                throw new ConflictException("Existing customer for externalCustomer " + externalId + " already exists and is not a person");
            }

            if (!"ExternalId".equals(customerMatches.getMatchTerm())) {
                CustomerDto customer = customerMatches.getCustomer();
                customer.setExternalId(externalId);
                customer.setMasterExternalId(externalId);
            }
        }

        return customerMatches;
    }
    public Person loadCustomer(ExternalCustomer externalCustomer) {
        return personMapper.mapToPerson(customerService.loadCompanyCustomer(externalCustomer.getExternalId(),"").getCustomerDto());


    }

}
