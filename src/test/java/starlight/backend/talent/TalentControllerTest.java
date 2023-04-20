package starlight.backend.talent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TalentControllerTest {
    @Value(value="${server.port}")
    private int port;
    @Test
    void pagination() {
    }

    @Test
    void searchTalentById() {
    }

    @Test
    void updateTalentFullInfo() {
    }

    @Test
    void deleteTalent() {
    }
}