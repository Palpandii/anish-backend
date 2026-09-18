package com.ascrackers.backend.service;

public class PdfLine implements PdfService.Line {
    private final String name;
    private final Integer qty;
    private final Double unitPrice;

    public PdfLine(String name, Integer qty, Double unitPrice) {
        this.name = name;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    @Override
    public String getName() { return name; }

    @Override
    public Integer getQty() { return qty; }

    @Override
    public Double getUnitPrice() { return unitPrice; }
}
