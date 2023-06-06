package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.AddBankAccountRequest;
import africa.semicolon.regcrow.dtos.response.*;
import africa.semicolon.regcrow.exceptions.*;
import africa.semicolon.regcrow.models.BankAccount;
import africa.semicolon.regcrow.repositories.BankAccountRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static africa.semicolon.regcrow.utils.AppUtils.buildPageRequest;
import static africa.semicolon.regcrow.utils.ExceptionUtils.*;
import static africa.semicolon.regcrow.utils.ResponseUtils.*;

@Service
@AllArgsConstructor
@Slf4j
public class RegrowBankAccountService implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    @Override
    public AddBankAccountResponse addBankAccount(AddBankAccountRequest addBankAccountRequest) throws AddBankAccountFailedException {
        BankAccount bankAccount = new BankAccount();
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        boolean isSavedBankAccount = savedBankAccount.getId() != null;
        if (!isSavedBankAccount)
            throw new AddBankAccountFailedException(String.format(ADD_BANK_ACCOUNT_FAILED, addBankAccountRequest.getBankName()));
        return buildAddBankAccountResponse(savedBankAccount.getId());
    }

    private static AddBankAccountResponse buildAddBankAccountResponse(Long bankAccountId) {
        AddBankAccountResponse addBankAccountResponse = new AddBankAccountResponse();
        addBankAccountResponse.setMessage(ADD_BANK_ACCOUNT_SUCCESSFUL);
        addBankAccountResponse.setId(bankAccountId);
        return addBankAccountResponse;
    }

    @Override
    public BankAccountResponse getBankAccountById(Long id) throws BankAccountNotFoundException {
        Optional<BankAccount> foundBankAccount = bankAccountRepository.findById(id);
        BankAccount bankAccount = foundBankAccount.orElseThrow(() -> new BankAccountNotFoundException(String.format(BANK_ACCOUNT_WITH_ID_NOT_FOUND, id)));
        BankAccountResponse bankAccountResponse = buildBankAccountResponse(bankAccount);
        log.info("Bank account response {}", bankAccountResponse);
        return bankAccountResponse;
    }

    private static BankAccountResponse buildBankAccountResponse(BankAccount bankAccount) {
        return BankAccountResponse.builder()
                .id(bankAccount.getId())
                .accountName(bankAccount.getAccountName())
                .accountNumber(bankAccount.getAccountNumber())
                .build();
    }

    @Override
    public List<BankAccountResponse> getAllBankAccounts(int page, int items) {
        Pageable pageable = buildPageRequest(page, items);
        Page<BankAccount> bankAccountPage = bankAccountRepository.findAll(pageable);
        List<BankAccount> bankAccounts = bankAccountPage.getContent();
        return bankAccounts.stream()
                .map(RegrowBankAccountService::buildBankAccountResponse)
                .toList();
    }

    @Override
    public ApiResponse<?> deleteBankAccount(Long id) {
        bankAccountRepository.deleteById(id);
        return ApiResponse.builder()
                .message(BANK_ACCOUNT_DELETED_SUCCESSFULLY)
                .build();
    }

    @Override
    public void deleteAll() {
        bankAccountRepository.deleteAll();
    }

    @Override
    public ApiResponse<?> updateBankAccountDetails(Long id, JsonPatch jsonPatch) throws BankAccountNotFoundException, BankAccountUpdateFailedException {
        ObjectMapper mapper = new ObjectMapper();
        Optional<BankAccount> foundBankAccount = bankAccountRepository.findById(id);
        BankAccount bankAccount = foundBankAccount.orElseThrow(() -> new BankAccountNotFoundException(String.format(BANK_ACCOUNT_WITH_ID_NOT_FOUND, id)));
        JsonNode bankAccountNode = mapper.convertValue(bankAccount, JsonNode.class);
        try {
            JsonNode updatedNode = jsonPatch.apply(bankAccountNode);
            BankAccount updatedBankAccount = mapper.convertValue(updatedNode, BankAccount.class);
            updatedBankAccount.setLastModifiedDate(LocalDateTime.now());
            bankAccountRepository.save(updatedBankAccount);
            return ApiResponse.builder()
                    .message(UPDATE_BANK_ACCOUNT_DETAILS_SUCCESSFUL)
                    .build();
        } catch (JsonPatchException e) {
            throw new BankAccountUpdateFailedException(e.getMessage());
        }
    }
}
