package codingdojo;

import codingdojo.dto.CustomerDto;
import codingdojo.dto.ExternalCustomer;
import codingdojo.entity.*;
import codingdojo.enums.CustomerType;
import codingdojo.mapper.CompanyMapper;
import codingdojo.mapper.CustomerMapper;
import codingdojo.repository.CompanyRepository;
import codingdojo.repository.CustomerRepository;
import codingdojo.repository.PersonRepository;
import codingdojo.service.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.mockito.Mock;



import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
public class CustomerServiceSyncTest {


    private CustomerSync customerSync;

    private CustomerMapper customerMapper;

    private CompanyMapper companyMapper;

    @Mock
    private PersonService personService;

    @Mock
    private  CustomerRepository customerRepository;
    @Mock
    private  CustomerServiceImpl customerService;

    @Mock
    private  CompanyServiceImpl companyService;

  @Mock
  private CompanyRepository companyRepository;
    @Mock
    private PersonRepository personRepository;

    @Test
    public void testCreateCompany() {
        // Given
        Company companyToCreate = createCompany();// Create a company object to be passed to the method
        when(companyRepository.create(companyToCreate)).thenReturn(companyToCreate); // Mock the behavior of companyRepository.create() method

        // When
        Company createdCompany = companyService.create(companyToCreate);

        // Then
        assertNotNull(createdCompany); // Ensure that the created company is not null
        assertEquals(companyToCreate, createdCompany); // Verify that the returned company is the same as the one passed to the method

        // Optionally, you can add more assertions based on the behavior of your create method
    }
    @Test
    public void testUpdateCompany() {
        // Given
        Company companyToUpdate = createCompany();// Create a company object to be passed to the method
        when(companyRepository.create(companyToUpdate)).thenReturn(companyToUpdate); // Mock the behavior of companyRepository.create() method

        // When
        companyService.update(companyToUpdate);

        // Then
        verify(companyRepository, times(1)).update(companyToUpdate); // Verify that companyRepository.update() method is called with the correct argument


    }
    @Test
    public void testCreatePerson() {
        // Given
        Person personToCreate = createPerson();// Create a company object to be passed to the method
        when(personRepository.create(personToCreate)).thenReturn(personToCreate); // Mock the behavior of companyRepository.create() method

        // When
        Person createdPerson= personService.create(personToCreate);

        // Then
        assertNotNull(createdPerson); // Ensure that the created company is not null
        assertEquals(personToCreate, createdPerson); // Verify that the returned company is the same as the one passed to the method

        // Optionally, you can add more assertions based on the behavior of your create method
    }
    @Test
    public void testUpdatePerson() {
        // Given
        Person personToUpdate = createPerson();// Create a company object to be passed to the method
        when(personRepository.create(personToUpdate)).thenReturn(personToUpdate); // Mock the behavior of companyRepository.create() method

        // When
        personService.update(personToUpdate);

        // Then
        verify(personRepository, times(1)).update(personToUpdate); // Verify that companyRepository.update() method is called with the correct argument


    }

    @Test
    public void syncCompanyByExternalId(){
        // Arrange
        String externalId = "12345";

        ExternalCustomer externalCustomer = createExternalCompany();
        externalCustomer.setExternalId(externalId);

        CustomerDto customerDto = createCustomerWithSameCompanyAs(externalCustomer);
        customerDto.setExternalId(externalId);

        // Define the behavior of the mock object
        when(customerService.findByExternalId(externalId)).thenReturn(customerDto);

        // Act
        boolean created = customerSync.syncWithDataLayer(externalCustomer);

        // Assert
        assertFalse(created);

        // Verify interactions with the mock object
        ArgumentCaptor<CustomerDto> argumentCaptor = ArgumentCaptor.forClass(CustomerDto.class);
        verify(customerService, atLeastOnce()).updateCustomerRecord(argumentCaptor.capture());
        CustomerDto updatedCustomer = argumentCaptor.getValue();

        assertEquals(externalCustomer.getName(), updatedCustomer.getName());
        assertEquals(externalCustomer.getExternalId(), updatedCustomer.getExternalId());
        assertNull(updatedCustomer.getMasterExternalId());
        assertEquals(externalCustomer.getCompanyNumber(), updatedCustomer.getCompanyNumber());
        assertEquals(externalCustomer.getPostalAddress(), updatedCustomer.getAddress());
        assertEquals(externalCustomer.getShoppingLists(), updatedCustomer.getShoppingLists());
        assertEquals(CustomerType.COMPANY, updatedCustomer.getCustomerType());
        assertNull(updatedCustomer.getPreferredStore());
    }


    private CustomerDto createCustomerWithSameCompanyAs(ExternalCustomer externalCustomer) {
        return CustomerDto.builder().
        companyNumber(externalCustomer.getCompanyNumber())
                        .customerType(CustomerType.COMPANY)
                .internalId("45435").build();

    }

    @Test
    public void synchronizeExistingPerson() {
        String externalId = "12345";

        ExternalCustomer externalCustomer = createExternalPerson();
        externalCustomer.setExternalId(externalId);
         CustomerDto customerDto = createPersonDto();
        customerDto.setExternalId(externalId);

        CustomerRepository customerRepository = mock(CustomerRepository.class);
        when(customerRepository.findByExternalId(externalId)).thenReturn(customerMapper.mapToCustomer(customerDto));


        // Use ArgumentCaptor to capture the Person object passed to PersonService.update()
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        // Verify that PersonService.update() method is called at least once with the captured argument
        verify(personService, atLeastOnce()).update(argument.capture());

        // Retrieve the captured Person object
        Person updatedCustomer = argument.getValue();

        // Assert the properties of the updated Customer
        assertEquals(externalCustomer.getName(), updatedCustomer.getName());
        assertEquals(externalCustomer.getExternalId(), updatedCustomer.getExternalId());
        assertNull(updatedCustomer.getMasterExternalId());
        assertEquals(externalCustomer.getPostalAddress(), updatedCustomer.getAddress());
        assertEquals(externalCustomer.getShoppingLists(), updatedCustomer.getShoppingLists());
        assertEquals(externalCustomer.getBonusPointsBalance(), updatedCustomer.getBonusPointsBalance());
        assertNull(updatedCustomer.getPreferredStore());
    }

    @Test
    public void synchronizeExistingCompany() {
        String externalId = "12345";

        ExternalCustomer externalCustomer = createExternalCompany();
        externalCustomer.setExternalId(externalId);

        CustomerDto companyDto= createCompany(externalCustomer);
        companyDto.setExternalId(externalId);

        CustomerRepository customerRepository = mock(CustomerRepository.class);
        when(customerRepository.findByExternalId(externalId)).thenReturn(customerMapper.mapToCustomer(companyDto));


        // ASSERT
        ArgumentCaptor<Company> argument = ArgumentCaptor.forClass(Company.class);
        verify(companyService, atLeastOnce()).update(argument.capture());
        Company updatedCustomer = argument.getValue();
        assertEquals(externalCustomer.getName(), updatedCustomer.getName());
        assertEquals(externalCustomer.getExternalId(), updatedCustomer.getExternalId());
        assertNull(updatedCustomer.getMasterExternalId());
        assertEquals(externalCustomer.getCompanyNumber(), updatedCustomer.getCompanyNumber());
        assertEquals(externalCustomer.getPostalAddress(), updatedCustomer.getAddress());
        assertEquals(externalCustomer.getShoppingLists(), updatedCustomer.getShoppingLists());
        assertNull(updatedCustomer.getPreferredStore());
    }

    private ExternalCustomer createExternalCompany() {
        return
        ExternalCustomer.builder().
        externalId("12345")
        .name("Company Inc.")
        .address(Address.builder().city("Vancouver").street("Howe St").postalCode("V6Z 2X2").build())
        .companyNumber("604-3029-3232")
        .shoppingLists(Arrays.asList(new ShoppingList("keyboard", "monitor"))).build();

    }

    private CustomerDto createCompany(ExternalCustomer externalCustomer) {
        return CustomerDto.builder()
                .customerType(CustomerType.COMPANY)
                .companyNumber(externalCustomer.getCompanyNumber())
                .internalId("45435")
                .shoppingLists(new ArrayList<>())
                .build();
    }

    private Company createCompany() {
        return Company.builder()
                .customerType(CustomerType.COMPANY)
                .companyNumber("12345")
                .internalId("45435")
                .shoppingLists(new ArrayList<>())
                .build();
    }

    private ExternalCustomer createExternalPerson() {
       return ExternalCustomer.builder().
                externalId("12345")
                .name("Company Inc.")
                .address(Address.builder().city("Vancouver").street("Howe St").postalCode("V6Z 2X2").build())
                .companyNumber("604-3029-3232")
                .shoppingLists(Arrays.asList(new ShoppingList("keyboard", "monitor"))).build();
    }

    private Person createPerson() {
        return Person.builder()
                .customerType(CustomerType.PERSON)
                .externalId("12345")
                .internalId("45435")
                .shoppingLists(new ArrayList<>())
                .build();
    }
    private CustomerDto createPersonDto() {
        return CustomerDto.builder()
                .customerType(CustomerType.PERSON)
                .externalId("12345")
                .internalId("45435")
                .shoppingLists(new ArrayList<>())
                .build();
    }


}
