package codingdojo.entity;

import lombok.*;

@Data
@Setter
@Getter
@Builder
public class Address {
    private String street;
    private String city;
    private String postalCode;


}
