package africa.semicolon.regcrow.services.cloud;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class CloudinaryServiceTest {

    @Autowired
    private CloudService cloudService;

    @Test public void uploadTest() {
        try {
            MultipartFile imageFile = new MockMultipartFile("9KICKS", new FileInputStream("C:\\Users\\Omisakin Joshua\\IdeaProjects\\regcrow\\regcrow\\src\\test\\resources\\assets\\9KICKS.JPG"));
            String cloudinaryUrl = cloudService.upload(imageFile.getBytes());
            assertThat(cloudinaryUrl).isNotNull();
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
