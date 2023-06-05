package africa.semicolon.regcrow.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBankAccountRequest {
    private String bankName;
    private String accountName;
    private String accountNumber;
}
