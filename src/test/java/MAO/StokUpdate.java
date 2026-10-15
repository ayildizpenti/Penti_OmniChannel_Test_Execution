package MAO;

import com.microsoft.playwright.Playwright;
import org.example.InventoryService;
import org.junit.jupiter.api.Test;

public class StokUpdate {

    public static void updateLocationStock(
            String itemId,
            String locationId,
            int quantity,
            boolean resetOtherLocations)
            throws Exception {

        try (Playwright playwright = Playwright.create()) {

            InventoryService service =
                    new InventoryService(playwright);

            service.updateStock(
                    itemId,
                    locationId,
                    quantity,
                    resetOtherLocations
            );
        }
    }
}
