package com.stockmarket.analysis.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BuyStockRequest {

    @NotBlank(message = "Stock symbol is required")
    private String symbol;

    @NotBlank(message = "Exchange is required")
    private String exchange;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    private BigDecimal quantity;


    public BuyStockRequest() {
    }


    public String getSymbol() {
        return symbol;
    }


    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public String getExchange() {
        return exchange;
    }


    public void setExchange(String exchange) {
        this.exchange = exchange;
    }


    public BigDecimal getQuantity() {
        return quantity;
    }


    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}