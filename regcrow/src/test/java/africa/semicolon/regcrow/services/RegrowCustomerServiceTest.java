package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.exception.UserNotFoundException;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static africa.semicolon.regcrow.utils.AppUtils.*;
import static java.math.BigInteger.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RegrowCustomerServiceTest {
    
    @Autowired
    private CustomerService customerService;

    private CustomerRegistrationRequest customerRegistrationRequest;

    private CustomerRegistrationResponse customerRegistrationResponse;

    @BeforeEach
    public void setUp() throws CustomerRegistrationFailedException {
        customerService.deleteAll();
        customerRegistrationRequest = new CustomerRegistrationRequest();
        customerRegistrationRequest.setEmail("folahan@gmail.com");
        customerRegistrationRequest.setPassword("OneOfOne1");

        customerRegistrationResponse = customerService.register(customerRegistrationRequest);
    }

    @Test public void testThatCustomerCanRegister() throws CustomerRegistrationFailedException {
        customerRegistrationResponse = customerService.register(customerRegistrationRequest);
        assertThat(customerRegistrationResponse).isNotNull();
    }

    @Test public void getCustomerByIdTest() throws UserNotFoundException {
        var foundCustomer = customerService.getCustomerById(1L);
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getEmail()).isNotNull();
        assertThat(foundCustomer.getEmail()).isEqualTo(customerRegistrationRequest.getEmail());
    }

    @Test public void getAllCustomersTest() throws CustomerRegistrationFailedException {
        customerRegistrationRequest.setEmail("omisakinjoshua@gmail.com");
        customerRegistrationRequest.setPassword("folahan");
        customerService.register(customerRegistrationRequest);
        List<CustomerResponse> customers = customerService.getAllCustomers();
        assertThat(customers.size()).isEqualTo(TWO.intValue());
    }

    @Test public void deleteCustomerTest() {
        var customers = customerService.getAllCustomers();
        int numberOfCustomers = customers.size();
        assertThat(numberOfCustomers).isGreaterThan(ZERO.intValue());
        customerService.deleteCustomer(customerRegistrationResponse);
        List<CustomerResponse> currentCustomers = customerService.getAllCustomers();
        assertThat(currentCustomers.size()).isEqualTo(numberOfCustomers-ONE.intValue());
    }

    @Test public void updateCustomerTest() {
        JsonPatch updateForm = buildUpdatePatch();
        var response = customerService.updateCustomerDetails(customerRegistrationResponse.getId(), updateForm);

    }

    private JsonPatch buildUpdatePatch() {
        try {
            List<JsonPatchOperation> updates = List.of(
                    new ReplaceOperation(new JsonPointer(""), new )
            )
        } catch (JsonPointerException e) {
            throw new RuntimeException(e);
        }
    }
}
