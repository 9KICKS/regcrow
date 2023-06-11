package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.AddBankAccountRequest;
import africa.semicolon.regcrow.dtos.response.*;
import africa.semicolon.regcrow.exceptions.*;
import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

public interface BankAccountService {
    AddBankAccountResponse addBankAccount(AddBankAccountRequest addBankAccountRequest) throws AddBankAccountFailedException;

    BankAccountResponse getBankAccountById(Long id) throws BankAccountNotFoundException;

    List<BankAccountResponse> getAllBankAccounts(int page, int items);

    ApiResponse<?> deleteBankAccount(Long id);

    void deleteAll();

    ApiResponse<?> updateBankAccountDetails(Long id, JsonPatch jsonPatch) throws BankAccountNotFoundException, BankAccountUpdateFailedException;
}
