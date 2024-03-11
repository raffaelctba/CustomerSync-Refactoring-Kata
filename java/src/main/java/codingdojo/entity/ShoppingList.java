package codingdojo.entity;

import lombok.*;

import java.util.Arrays;
import java.util.List;


@Data
@Setter
@Getter
@RequiredArgsConstructor
@Builder
public class ShoppingList {

    private final List<String> products;

    public ShoppingList(String... products) {
        this.products = Arrays.asList(products);
    }

}
