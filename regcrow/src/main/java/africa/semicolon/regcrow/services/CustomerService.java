package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.exception.UserNotFoundException;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

public interface CustomerService {
    CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest) throws CustomerRegistrationFailedException;

    CustomerResponse getCustomerById(Long id) throws UserNotFoundException;

    List<CustomerResponse> getAllCustomers();

    ApiResponse<?> deleteCustomer(Long id);

    void deleteAll();

    ApiResponse<?> updateCustomerDetails(Long id, JsonPatch jsonPatch);
}
