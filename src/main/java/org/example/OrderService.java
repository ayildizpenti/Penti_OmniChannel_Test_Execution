package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.example.Trendyol.OrderLine;

import java.util.List;

public class OrderService {

    private static final String URL =
            "https://stageapigw.trendyol.com/integration/test/order/orders/core";

    private static final String SELLER_ID =
            "911672";

    private static final String AUTHORIZATION =
            "Basic Y0U2OHl1U3VXTXlSWk9qdU5wNVg6dG1MRHBWd3Fucm9oTnNpZTBCTTE=";

    private final APIRequestContext requestContext;

    private final ObjectMapper mapper =
            new ObjectMapper();

    public OrderService(Playwright playwright) {

        this.requestContext =
                playwright.request().newContext();
    }

    public String createOrder(
            List<OrderLine> lines)
            throws Exception {

        StringBuilder lineBuilder =
                new StringBuilder();

        for (int i = 0; i < lines.size(); i++) {

            OrderLine line = lines.get(i);

            lineBuilder.append(
                    """
                    {
                        "barcode":"%s",
                        "quantity":%d,
                        "discountPercentage":0
                    }
                    """
                            .formatted(
                                    line.getBarcode(),
                                    line.getQuantity()
                            ));

            if (i < lines.size() - 1) {
                lineBuilder.append(",");
            }
        }

        String body =
                """
                {
                  "customer": {
                    "customerFirstName": "Ali",
                    "customerLastName": "Yildiz"
                  },
                  "invoiceAddress": {
                    "addressText": "Test Ev2",
                    "city": "İstanbul",
                    "company": "",
                    "district": "Üsküdar",
                    "invoiceFirstName": "Ali",
                    "invoiceLastName": "Yildiz",
                    "latitude": "string",
                    "longitude": "string",
                    "neighborhood": "",
                    "phone": "5433239235",
                    "postalCode": "10200",
                    "email": "test@gmail.com",
                    "invoiceTaxNumber": "7280059048",
                    "invoiceTaxOffice": "Marmara Kurumlar"
                  },
                  "lines": [
                    %s
                  ],
                  "seller": {
                    "sellerId": 911672
                  },
                  "shippingAddress": {
                    "addressText": "Test Ev",
                    "city": "Erzincan",
                    "company": "",
                    "district": "Üzümlü",
                    "latitude": "string",
                    "longitude": "string",
                    "neighborhood": "Altınbaşak",
                    "phone": "5449795936",
                    "postalCode": "24160",
                    "shippingFirstName": "Ali",
                    "shippingLastName": "RA",
                    "email": "test@gmail.com"
                  },
                  "commercial": false,
                  "microRegion": "String"
                }
                """
                        .formatted(lineBuilder);

        APIResponse response =
                requestContext.post(
                        URL,
                        RequestOptions.create()
                                .setHeader(
                                        "SellerID",
                                        SELLER_ID)
                                .setHeader(
                                        "Authorization",
                                        AUTHORIZATION)
                                .setHeader(
                                        "Content-Type",
                                        "application/json")
                                .setData(body));

        System.out.println(
                "ORDER STATUS : "
                        + response.status());

        System.out.println(
                response.text());

        JsonNode json =
                mapper.readTree(
                        response.text());

        return json
                .get("orderNumber")
                .asText();
    }
}