package codingdojo.mapper;

import codingdojo.dto.CustomerDto;
import codingdojo.dto.ExternalCustomer;
import codingdojo.entity.Company;
import codingdojo.entity.Customer;
import codingdojo.entity.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    private ModelMapper modelMapper;
    public CustomerDto mapToCustomerDto(Company company) {
        return modelMapper.map(company, CustomerDto.class);
    }

    public Company mapToCompany(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Company.class);
    }

    public ExternalCustomer mapToExternalCustomer(Company company){
        return modelMapper.map(company, ExternalCustomer.class);
    }
    public Company mapToCompany(ExternalCustomer externalCustomer){
        return modelMapper.map(externalCustomer, Company.class);
    }
    public Company mapToCompany(Customer customer) {
        return modelMapper.map(customer, Company.class);
    }

}
