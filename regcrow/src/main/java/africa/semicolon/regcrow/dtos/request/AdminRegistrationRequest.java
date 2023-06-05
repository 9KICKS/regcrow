package africa.semicolon.regcrow.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegistrationRequest {
    private String email;
    private String password;
}
