package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import constants.LocationConstants;

public class InventoryService {

    private static final String BASE_URL =
            "https://upgts.omni.manh.com";

    private final APIRequestContext requestContext;

    private final ObjectMapper mapper =
            new ObjectMapper();

    public InventoryService(Playwright playwright) {

        this.requestContext =
                playwright.request().newContext();
    }

    public void updateStock(
            String itemId,
            String targetLocation,
            int quantity,
            boolean resetOtherLocations)
            throws Exception {

        // Hedef lokasyonu güncelle

        updateSingleLocation(
                itemId,
                targetLocation,
                quantity);

        // Diğer lokasyonları sıfırla

        if (resetOtherLocations) {

            for (String location :
                    LocationConstants.ALL_LOCATIONS) {

                if (location.equals(targetLocation)) {
                    continue;
                }

                updateSingleLocation(
                        itemId,
                        location,
                        0);
            }
        }
    }

    private void updateSingleLocation(
            String itemId,
            String locationId,
            int quantity)
            throws Exception {

        String token =
                MAOAuthService.getAccessToken();

        String supplyTypeId =
                determineSupplyType(locationId);

        String query =
                String.format(
                        "ItemId='%s' AND LocationId='%s' AND SupplyTypeId.SupplyTypeId='%s'",
                        itemId,
                        locationId,
                        supplyTypeId);

        APIResponse getResponse =
                requestContext.get(
                        BASE_URL +
                                "/inventory/api/inventory/supply",
                        RequestOptions.create()
                                .setHeader(
                                        "Authorization",
                                        "Bearer " + token)
                                .setQueryParam(
                                        "query",
                                        query));

        JsonNode json =
                mapper.readTree(
                        getResponse.text());

        JsonNode data =
                json.path("data")
                        .get(0);

        long newSequenceNumber =
                data.path("ExternalSequenceNumber")
                        .asLong() + 1;

        int allocatedQty =
                data.path("SupplyAllocation")
                        .path("AllocatedQuantity")
                        .asInt();

        int finalQuantity =
                quantity == 0
                        ? 0
                        : quantity + allocatedQty;

        String requestBody =
                """
                {
                  "SupplyEvent": [
                    {
                      "SupplyDefinition": {
                        "ItemId":"%s",
                        "LocationId":"%s",
                        "SupplyData":{
                          "Quantity":%d,
                          "UOM":"ADT",
                          "QuantityAdjustmentType":"R",
                          "ExternalSequenceNumber":%d
                        },
                        "SupplyType":{
                          "SupplyTypeId":"%s"
                        }
                      },
                      "TransactionTypeId":"Adjustment"
                    }
                  ]
                }
                """
                        .formatted(
                                itemId,
                                locationId,
                                finalQuantity,
                                newSequenceNumber,
                                supplyTypeId);

        APIResponse postResponse =
                requestContext.post(
                        BASE_URL +
                                "/inventory/api/inventory/supply/supplyEvent",
                        RequestOptions.create()
                                .setHeader(
                                        "Authorization",
                                        "Bearer " + token)
                                .setHeader(
                                        "Content-Type",
                                        "application/json")
                                .setData(
                                        requestBody));

        System.out.println(
                "Location : " + locationId);

        System.out.println(
                "Status : " +
                        postResponse.status());
    }

    private String determineSupplyType(
            String locationId) {

        return switch (locationId) {

            case "1302",
                 "7000",
                 "8000",
                 "9000"
                    -> "On Hand Turkey Ecom";

            default
                    -> "On Hand Store";
        };
    }
}

