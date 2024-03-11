package codingdojo.mapper;

import codingdojo.dto.ExternalCustomer;
import codingdojo.dto.CustomerDto;
import codingdojo.entity.Customer;
import codingdojo.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    private ModelMapper modelMapper;
    public CustomerDto mapToCustomerDto(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }

    public Customer mapToCustomer(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }

    public ExternalCustomer mapToExternalCustomer(CustomerDto customerDto){
        return modelMapper.map(customerDto, ExternalCustomer.class);
    }

    public CustomerDto mapToCustomerDto(ExternalCustomer externalCustomer){
        return modelMapper.map(externalCustomer, CustomerDto.class);
    }


}
