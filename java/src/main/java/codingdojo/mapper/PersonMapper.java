package codingdojo.mapper;

import codingdojo.dto.CustomerDto;
import codingdojo.dto.ExternalCustomer;
import codingdojo.entity.Customer;
import codingdojo.entity.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    private ModelMapper modelMapper;
    public CustomerDto mapToCustomerDto(Person person) {
        return modelMapper.map(person, CustomerDto.class);
    }

    public Person mapToPerson(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Person.class);
    }

    public ExternalCustomer mapToExternalCustomer(Person person){
        return modelMapper.map(person, ExternalCustomer.class);
    }
    public Person mapToPerson(ExternalCustomer externalCustomer){
        return modelMapper.map(externalCustomer, Person.class);
    }


}
