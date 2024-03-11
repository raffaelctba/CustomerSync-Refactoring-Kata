package codingdojo.dto;


import codingdojo.entity.Address;
import codingdojo.entity.ShoppingList;
import codingdojo.enums.CustomerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Builder
public class CustomerDto {
    private String externalId;
    private String masterExternalId;
    private Address address;
    private String preferredStore;
    private List<ShoppingList> shoppingLists;
    private String internalId;
    private String name;
    private CustomerType customerType;
    private String companyNumber;
    private int bonusPointsBalance;

    public void addShoppingList(ShoppingList consumerShoppingList) {
        List<ShoppingList> newList = new ArrayList<>(this.shoppingLists);
        newList.add(consumerShoppingList);
        this.setShoppingLists(newList);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerDto)) return false;
        CustomerDto customer = (CustomerDto) o;
        return Objects.equals(externalId, customer.externalId) &&
              Objects.equals(masterExternalId, customer.masterExternalId) &&
              Objects.equals(companyNumber, customer.companyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId, masterExternalId, companyNumber);
    }
}
