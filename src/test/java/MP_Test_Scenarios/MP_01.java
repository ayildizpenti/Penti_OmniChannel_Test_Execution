package MP_Test_Scenarios;

import MAO.StockUtils;
import Trendyol.OrderUtils;
import org.testng.annotations.Test;

public class MP_01 {

    // MP Single Line TY Scenario
    //Item A - 2 qty

    String ItemA = "PLNADGST22IY003";
    String ItemA_barcode="6300249093301";
    String Location = "1302";
    int quantity= 30;
    int orderqunatity = 2;
    boolean resetOtherLocations= true;

    @Test
    public void stockPreparation() throws Exception {

        StockUtils.updateStock(
                ItemA,
                Location,
                quantity,
                resetOtherLocations
        );
    }

    @Test
    void createTYOrder() throws Exception {

        String orderNo =
                OrderUtils.createOrder(
                        ItemA_barcode, orderqunatity

                );

        System.out.println(
                "Created Order : "
                        + orderNo);
    }

}
