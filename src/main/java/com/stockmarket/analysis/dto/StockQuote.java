package com.stockmarket.analysis.dto;

public class StockQuote {

    private String symbol;
    private String name;
    private String exchange;
    private String currency;
    private String datetime;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double previousClose;
    private Double change;
    private Double percentChange;
    private Long volume;
    private Boolean marketOpen;
    private Double week52Low;
    private Double week52High;

    public StockQuote() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(Double previousClose) {
        this.previousClose = previousClose;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(Double percentChange) {
        this.percentChange = percentChange;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Boolean getMarketOpen() {
        return marketOpen;
    }

    public void setMarketOpen(Boolean marketOpen) {
        this.marketOpen = marketOpen;
    }

    public Double getWeek52Low() {
        return week52Low;
    }

    public void setWeek52Low(Double week52Low) {
        this.week52Low = week52Low;
    }

    public Double getWeek52High() {
        return week52High;
    }

    public void setWeek52High(Double week52High) {
        this.week52High = week52High;
    }
}