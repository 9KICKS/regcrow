package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.AdminRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.AdminRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.AdminResponse;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.exceptions.AdminRegistrationFailedException;
import africa.semicolon.regcrow.exceptions.ProfileUpdateFailedException;
import africa.semicolon.regcrow.exceptions.UserNotFoundException;
import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

public interface AdminService {
    AdminRegistrationResponse register(AdminRegistrationRequest adminRegistrationRequest) throws AdminRegistrationFailedException;

    AdminResponse getAdminById(Long id) throws UserNotFoundException;

    List<AdminResponse> getAllAdmins(int page, int items);

    ApiResponse<?> deleteAdmin(Long id);

    void deletAll();

    ApiResponse<?> updateAdminDetails(Long id, JsonPatch jsonPatch) throws UserNotFoundException, ProfileUpdateFailedException;
}
