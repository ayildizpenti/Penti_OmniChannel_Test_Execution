
package tests.MAO;

import org.example.MAOAuthService;
import org.junit.jupiter.api.Test;

public class MAO_Token {

    @Test
    void getTokenTest() throws Exception {

        String token = MAOAuthService.getAccessToken();

        System.out.println("TOKEN:");
        System.out.println(token);
    }
}