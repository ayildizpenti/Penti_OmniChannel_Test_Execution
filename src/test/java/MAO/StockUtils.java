package MAO;

import com.microsoft.playwright.Playwright;
import org.example.InventoryService;

public class StockUtils {

    private static final InventoryService inventoryService;

    static {
        Playwright playwright = Playwright.create();
        inventoryService = new InventoryService(playwright);
    }

    public static void updateStock(
            String itemId,
            String locationId,
            int quantity,
            boolean resetOtherLocations)
            throws Exception {

        inventoryService.updateStock(
                itemId,
                locationId,
                quantity,
                resetOtherLocations
        );
    }
}