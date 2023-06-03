package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.exception.UserNotFoundException;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import africa.semicolon.regcrow.models.BioData;
import africa.semicolon.regcrow.models.Customer;
import africa.semicolon.regcrow.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static africa.semicolon.regcrow.utils.ExceptionUtils.USER_REGISTRATION_FAILED;
import static africa.semicolon.regcrow.utils.ExceptionUtils.USER_WITH_ID_NOT_FOUND;
import static africa.semicolon.regcrow.utils.ResponseUtils.USER_DELETED_SUCCESSFULLY;
import static africa.semicolon.regcrow.utils.ResponseUtils.USER_REGISTRATION_SUCCESSFUL;

@Service
@AllArgsConstructor
@Slf4j
public class RegrowCustomerService implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    @Override
    public CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest) throws CustomerRegistrationFailedException {
        BioData bioData = modelMapper.map(customerRegistrationRequest, BioData.class);
        Customer customer = new Customer();
        customer.setBioData(bioData);
        Customer savedCustomer = customerRepository.save(customer);
        boolean isSavedCustomer = savedCustomer.getId() != null;
        if (!isSavedCustomer) throw new CustomerRegistrationFailedException(String.format(USER_REGISTRATION_FAILED, customerRegistrationRequest.getEmail()));
        return buildRegistrationCustomerResponse();
    }

    @Override
    public CustomerResponse getCustomerById(Long id) throws UserNotFoundException {
        Optional<Customer> foundCustomer = customerRepository.findById(id);
        Customer customer = foundCustomer.orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id)));
        CustomerResponse customerResponse = buildCUstomerResponse(customer);
        log.info("registration response {}", customerResponse);
        return customerResponse;
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers
                .stream()
                .map(RegrowCustomerService::buildCUstomerResponse)
                .toList();
    }

    @Override
    public ApiResponse<?> deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        return ApiResponse.builder().message(USER_DELETED_SUCCESSFULLY).build();
    }

    @Override
    public void deleteAll() {
        customerRepository.deleteAll();
    }

    private static CustomerResponse buildCUstomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .email(customer.getBioData().getEmail())
                .name(customer.getFirstName() + " " + customer.getLastName())
                .build();
    }

    private static CustomerRegistrationResponse buildRegistrationCustomerResponse() {
        CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
        customerRegistrationResponse.setMessage(USER_REGISTRATION_SUCCESSFUL);
        return customerRegistrationResponse;
    }
}
