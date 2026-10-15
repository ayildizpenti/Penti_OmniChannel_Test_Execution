package MP_Test_Scenarios;

import MAO.StockUtils;
import Trendyol.OrderUtils;
import org.testng.annotations.Test;

public class MP_01 {

    @Test
    public void stockPreparation() throws Exception {

        StockUtils.updateStock(
                "PLNADGST22IY003",
                "1302",
                10,
                true
        );
    }

    @Test
    void createTYOrder() throws Exception {

        String orderNo =
                OrderUtils.createOrder(
                        "6300249093301", 2,
                        "6300249093318", 1
                );

        System.out.println(
                "Created Order : "
                        + orderNo);
    }

}
