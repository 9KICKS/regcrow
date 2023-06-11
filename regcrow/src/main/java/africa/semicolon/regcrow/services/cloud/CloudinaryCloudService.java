package africa.semicolon.regcrow.services.cloud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CloudinaryCloudService implements CloudService {

    private Cloudinary cloudinary

    @Override
    public String upload(byte[] image) {
        return null;
    }
}
