package africa.semicolon.regcrow.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankAccountResponse {
    private Long id;
    private String accountName;
    private String accountNumber;
}
