package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.AddBankAccountRequest;
import africa.semicolon.regcrow.dtos.response.AddBankAccountResponse;
import africa.semicolon.regcrow.dtos.response.BankAccountResponse;
import africa.semicolon.regcrow.exceptions.*;
import com.fasterxml.jackson.databind.node.TextNode;
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

import static java.math.BigInteger.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RegrowBankAccountServiceTest {

    @Autowired
    private BankAccountService bankAccountService;

    private AddBankAccountRequest addBankAccountRequest;

    private AddBankAccountResponse addBankAccountResponse;

    @BeforeEach
    public void setUp() throws AddBankAccountFailedException {
        bankAccountService.deleteAll();
        addBankAccountRequest = new AddBankAccountRequest();
        addBankAccountRequest.setBankName("9KICKS Bank");
        addBankAccountRequest.setAccountName("Folahan");
        addBankAccountRequest.setAccountNumber("0908");
        addBankAccountResponse = bankAccountService.addBankAccount(addBankAccountRequest);
    }

    @Test
    public void testThatCustomerCanAddBankAccount() throws AddBankAccountFailedException {
        assertThat(addBankAccountResponse).isNotNull();
    }

    @Test public void getBankAccountByIdTest() throws AddBankAccountFailedException, BankAccountNotFoundException {
        var foundBankAccount = bankAccountService.getBankAccountById(addBankAccountResponse.getId());
        assertThat(foundBankAccount).isNotNull();
        assertThat(foundBankAccount.getAccountName()).isNotNull();
        assertThat(foundBankAccount.getAccountName()).isEqualTo(addBankAccountRequest.getAccountName());
    }

    @Test public void getAllBankAccountsTest() throws AddBankAccountFailedException {
        addBankAccountRequest.setBankName("9KICKS Bank");
        addBankAccountRequest.setAccountName("Joshua");
        addBankAccountRequest.setAccountNumber("001");
        bankAccountService.addBankAccount(addBankAccountRequest);
        List<BankAccountResponse> bankAccounts = bankAccountService.getAllBankAccounts(ONE.intValue(), TEN.intValue());
        assertThat(bankAccounts.size()).isEqualTo(TWO.intValue());
    }

    @Test public void deleteBankAccountTest() {
        var bankAccounts = bankAccountService.getAllBankAccounts(ONE.intValue(), TEN.intValue());
        int numberOfBankAccounts = bankAccounts.size();
        assertThat(numberOfBankAccounts).isGreaterThan(ZERO.intValue());
        bankAccountService.deleteBankAccount(addBankAccountResponse.getId());
        List<BankAccountResponse> currentBankAccounts = bankAccountService.getAllBankAccounts(ONE.intValue(), TEN.intValue());
        assertThat(currentBankAccounts.size()).isEqualTo(numberOfBankAccounts - ONE.intValue());
    }

    @Test public void updateBankAccountDetailsTest() throws BankAccountNotFoundException, BankAccountUpdateFailedException {
        JsonPatch updateForm = buildUpdatePatch();
        BankAccountResponse foundBankAccount = bankAccountService.getBankAccountById(addBankAccountResponse.getId());
        assertThat(foundBankAccount.getAccountName().contains("Folahan") && foundBankAccount.getAccountNumber().contains("0908")).isFalse();

        var response = bankAccountService.updateBankAccountDetails(addBankAccountResponse.getId(), updateForm);
        assertThat(response).isNotNull();

        BankAccountResponse bankAccountResponse = bankAccountService.getBankAccountById(addBankAccountResponse.getId());
        assertThat(bankAccountResponse.getAccountName().contains("Folahan") && foundBankAccount.getAccountNumber().contains("0908")).isTrue();
    }

    private JsonPatch buildUpdatePatch() {
        try {
            List<JsonPatchOperation> updates = List.of(
                    new ReplaceOperation(
                            new JsonPointer("/accountName"),
                            new TextNode("Folahan")
                    ),
                    new ReplaceOperation(
                            new JsonPointer("/accountNumber"),
                            new TextNode("0908")
                    )
            );
            return new JsonPatch(updates);
        } catch (JsonPointerException e) {
            throw new RuntimeException(e);
        }
    }
}
