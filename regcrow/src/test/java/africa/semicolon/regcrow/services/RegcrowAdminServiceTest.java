package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.AdminRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.AdminRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.AdminResponse;
import africa.semicolon.regcrow.exceptions.AdminRegistrationFailedException;
import africa.semicolon.regcrow.exceptions.ProfileUpdateFailedException;
import africa.semicolon.regcrow.exceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.node.TextNode;;
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
public class RegcrowAdminServiceTest {
    @Autowired
    private AdminService adminService;

    private AdminRegistrationRequest adminRegistrationRequest;

    private AdminRegistrationResponse adminRegistrationResponse;

    @BeforeEach public void setUp() throws AdminRegistrationFailedException {
        adminService.deletAll();
        adminRegistrationRequest = new AdminRegistrationRequest();
        adminRegistrationRequest.setEmail("folahan9@gmail.com");
        adminRegistrationRequest.setPassword("OneOfOne!");
        adminRegistrationResponse = adminService.register(adminRegistrationRequest);
    }

    @Test public void testThatAdminCanRegister() throws AdminRegistrationFailedException {
        assertThat(adminRegistrationResponse).isNotNull();
    }

    @Test public void getAdminByIdTest() throws AdminRegistrationFailedException, UserNotFoundException {
        var foundAdmin = adminService.getAdminById(adminRegistrationResponse.getId());
        assertThat(foundAdmin).isNotNull();
        assertThat(foundAdmin.getEmail()).isNotNull();
        assertThat(foundAdmin.getEmail()).isEqualTo(adminRegistrationRequest.getEmail());
    }

    @Test public void getAllAdminsTest() throws AdminRegistrationFailedException {
        adminRegistrationRequest.setEmail("folahan@gmail.com");
        adminRegistrationRequest.setPassword("0908");
        adminService.register(adminRegistrationRequest);
        List<AdminResponse> admins = adminService.getAllAdmins(ONE.intValue(), TEN.intValue());
        assertThat(admins.size()).isEqualTo(TWO.intValue());
     }

     @Test public void deleteAdminTest() {
        var admins = adminService.getAllAdmins(ONE.intValue(), TEN.intValue());
        int numberOfAdmins = admins.size();
        assertThat(numberOfAdmins).isGreaterThan(ZERO.intValue());
        adminService.deleteAdmin(adminRegistrationResponse.getId());
        List<AdminResponse> currentAdmins = adminService.getAllAdmins(ONE.intValue(), TEN.intValue());
        assertThat(currentAdmins.size()).isEqualTo(numberOfAdmins - ONE.intValue());
     }

     @Test public void updateCustomerTest() throws UserNotFoundException, ProfileUpdateFailedException {
         JsonPatch updateForm = buildUpdatePatch();
         AdminResponse foundAdmin = adminService.getAdminById(adminRegistrationResponse.getId());
         assertThat(foundAdmin.getName().contains("Folahan") && foundAdmin.getName().contains("Omisakin")).isFalse();

         var response = adminService.updateAdminDetails(adminRegistrationResponse.getId(), updateForm);
         assertThat(response).isNotNull();

         AdminResponse adminResponse = adminService.getAdminById(adminRegistrationResponse.getId());
         assertThat(adminResponse.getName().contains("Folahan") && adminResponse.getName().contains("Omisakin")).isTrue();
     }

    private JsonPatch buildUpdatePatch() {
        try {
            List<JsonPatchOperation> updates = List.of(
                    new ReplaceOperation(
                            new JsonPointer("/email"),
                            new TextNode("Folahan")
                    ),
                    new ReplaceOperation(
                            new JsonPointer("/email"),
                            new TextNode("Joshua")
                    )
            );
            return new JsonPatch(updates);
        } catch (JsonPointerException e) {
            throw new RuntimeException(e);
        }
    }
}