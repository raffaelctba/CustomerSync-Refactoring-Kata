package codingdojo.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@Getter
@SuperBuilder
public class Person extends Customer{

    private int bonusPointsBalance;

}
