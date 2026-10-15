package org.example.Trendyol;

public class OrderLine {

    private final String barcode;
    private final int quantity;

    public OrderLine(String barcode,
                     int quantity) {

        this.barcode = barcode;
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public int getQuantity() {
        return quantity;
    }
}