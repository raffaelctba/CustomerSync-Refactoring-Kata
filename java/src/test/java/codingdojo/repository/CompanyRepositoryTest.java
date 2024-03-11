package codingdojo.repository;


import codingdojo.entity.Company;

import codingdojo.enums.CustomerType;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;



@RequiredArgsConstructor
public class CompanyRepositoryTest {


    private CompanyRepository companyRepository;


    @Test
    public void CoRepository_SaveAll_ReturnSavedPokemon() {

        //Arrange
        Company company = Company.builder()
                .customerType(CustomerType.COMPANY)
                .companyNumber("12345")
                .internalId("45435")
                .shoppingLists(new ArrayList<>())
                .build();

        //Act
        Company savedCompany = companyRepository.create(company);

        assertNotNull(savedCompany); // Ensure that the created company is not null
        assertEquals(company, savedCompany);
    }
}
