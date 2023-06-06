package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.AdminRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.AdminRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.AdminResponse;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.exceptions.AdminRegistrationFailedException;
import africa.semicolon.regcrow.exceptions.ProfileUpdateFailedException;
import africa.semicolon.regcrow.exceptions.UserNotFoundException;
import africa.semicolon.regcrow.models.Admin;
import africa.semicolon.regcrow.models.BioData;
import africa.semicolon.regcrow.repositories.AdminRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static africa.semicolon.regcrow.utils.AppUtils.buildPageRequest;
import static africa.semicolon.regcrow.utils.ExceptionUtils.USER_REGISTRATION_FAILED;
import static africa.semicolon.regcrow.utils.ExceptionUtils.USER_WITH_ID_NOT_FOUND;
import static africa.semicolon.regcrow.utils.ResponseUtils.*;

@Service
@AllArgsConstructor
@Slf4j
public class RegcrowAdminService implements AdminService {
    private final AdminRepository adminRepository;

    private final ModelMapper modelMapper;

    @Override
    public AdminRegistrationResponse register(AdminRegistrationRequest adminRegistrationRequest) throws AdminRegistrationFailedException {
        BioData bioData = modelMapper.map(adminRegistrationRequest, BioData.class);
        Admin admin = new Admin();
        admin.setBioData(bioData);
        Admin savedAdmin = adminRepository.save(admin);
        boolean isSavedAdmin = savedAdmin.getId() != null;
        if (!isSavedAdmin) throw new AdminRegistrationFailedException(String.format(USER_REGISTRATION_FAILED, adminRegistrationRequest.getEmail()));
        return buildRegisterAdminResponse(savedAdmin.getId());
    }

    private static AdminRegistrationResponse buildRegisterAdminResponse(Long adminId) {
        AdminRegistrationResponse adminRegistrationResponse = new AdminRegistrationResponse();
        adminRegistrationResponse.setMessage(USER_REGISTRATION_SUCCESSFUL);
        adminRegistrationResponse.setId(adminId);
        return adminRegistrationResponse;
    }

    @Override
    public AdminResponse getAdminById(Long id) throws UserNotFoundException {
        Optional<Admin> foundAdmin = adminRepository.findById(id);
        Admin admin = foundAdmin.orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id)));
        AdminResponse adminResponse = buildAdminResponse(admin);
        log.info("Registration response {}", adminResponse);
        return adminResponse;
    }

    private static AdminResponse buildAdminResponse(Admin admin) {
        return AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getBioData().getEmail())
                .build();
    }

    @Override
    public List<AdminResponse> getAllAdmins(int page, int items) {
        Pageable pageable = buildPageRequest(page, items);
        Page<Admin> adminPage = adminRepository.findAll(pageable);
        List<Admin> admins = adminPage.getContent();
        return admins.stream()
                .map(RegcrowAdminService::buildAdminResponse)
                .toList();
    }

    @Override
    public ApiResponse<?> deleteAdmin(Long id) {
        adminRepository.deleteById(id);
        return ApiResponse.builder()
                .message(USER_DELETED_SUCCESSFULLY)
                .build();
    }

    @Override
    public void deletAll() {
        adminRepository.deleteAll();
    }

    @Override
    public ApiResponse<?> updateAdminDetails(Long id, JsonPatch jsonPatch) throws UserNotFoundException, ProfileUpdateFailedException {
        ObjectMapper mapper = new ObjectMapper();
        Optional<Admin> foundAdmin = adminRepository.findById(id);
        Admin admin = foundAdmin.orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id)));
        JsonNode adminNode = mapper.convertValue(admin, JsonNode.class);
        try {
            JsonNode updatedNode = jsonPatch.apply(adminNode);
            Admin updatedAdmin = mapper.convertValue(updatedNode, Admin.class);
            updatedAdmin.setLastModifiedDate(LocalDateTime.now());
            adminRepository.save(updatedAdmin);
            return ApiResponse.builder()
                    .message(PROFILE_UPDATED_SUCCESSFULLY)
                    .build();
        } catch (JsonPatchException e) {
            throw new ProfileUpdateFailedException(e.getMessage());
        }
    }
}
