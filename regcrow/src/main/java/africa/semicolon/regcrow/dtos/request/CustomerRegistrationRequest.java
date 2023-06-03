package africa.semicolon.regcrow.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerRegistrationRequest {
    private String email;
    private String password;
}
