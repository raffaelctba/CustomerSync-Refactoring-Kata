package codingdojo;

import codingdojo.dto.CustomerDto;
import codingdojo.entity.Company;
import codingdojo.entity.Customer;
import codingdojo.entity.Person;
import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@Data
public class CustomerMatches {
    private Collection<CustomerDto> duplicates = new ArrayList<>();
    @Setter
    private String matchTerm;
    private CustomerDto customer;

    public CustomerDto getCustomerDto() {
        return customer;
    }

    public boolean hasDuplicates() {
        return !duplicates.isEmpty();
    }

    public void addDuplicate(CustomerDto duplicate) {
        duplicates.add(duplicate);
    }

    public Collection<CustomerDto> getDuplicates() {
        return duplicates;
    }

    public String getMatchTerm() {
        return matchTerm;
    }

    public void setCustomerDto(CustomerDto customer) {
        this.customer = customer;
    }


}
