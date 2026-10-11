package com.stockmarket.analysis.dto;

import java.math.BigDecimal;

public class PortfolioResponse {

    private Long id;
    private BigDecimal cashBalance;

    public PortfolioResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }
}