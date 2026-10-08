package tests.MAO;

import com.microsoft.playwright.Playwright;
import org.example.InventoryService;
import org.junit.jupiter.api.Test;

public class StokUpdate {
    @Test
    void stockUpdateAndResetOthersTest(String itemId,String targetLocation,int quantity,boolean resetOtherLocations)
            throws Exception {

        try (Playwright playwright =
                     Playwright.create()) {

            InventoryService service =
                    new InventoryService(playwright);

            service.updateStock(
                    "PLNADGST22IY003",
                    "1302",
                    10,
                    true
            );
        }
    }
}
