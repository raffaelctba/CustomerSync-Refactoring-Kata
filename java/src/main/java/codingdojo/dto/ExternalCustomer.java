package codingdojo.dto;

import codingdojo.entity.ShoppingList;
import codingdojo.entity.Address;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Getter
@Setter
@Builder
public class ExternalCustomer {
    private Address address;
    private String name;
    private String preferredStore;
    private List<ShoppingList> shoppingLists;
    private String externalId;
    private String companyNumber;

    private int bonusPointsBalance;

    public boolean isCompany() {
        return companyNumber != null;
    }

    public Address getPostalAddress() {
        return address;
    }


}
