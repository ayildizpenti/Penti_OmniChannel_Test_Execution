package Trendyol;



import com.microsoft.playwright.Playwright;
import org.example.OrderService;
import org.example.Trendyol.OrderLine;

import java.util.ArrayList;
import java.util.List;

public class OrderUtils {

    public static String createOrder(
            Object... products)
            throws Exception {

        if (products.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "SKU ve Quantity çift olarak gönderilmelidir.");
        }

        List<OrderLine> lines =
                new ArrayList<>();

        for (int i = 0; i < products.length; i += 2) {

            String barcode =
                    (String) products[i];

            int quantity =
                    (Integer) products[i + 1];

            lines.add(
                    new OrderLine(
                            barcode,
                            quantity
                    )
            );
        }

        try (Playwright playwright =
                     Playwright.create()) {

            OrderService service =
                    new OrderService(playwright);

            return service.createOrder(lines);
        }
    }
}