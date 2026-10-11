package com.stockmarket.analysis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
public class StockRepository {

    @Autowired
    private WebClient webClient;

    @Value("${twelvedata.base-url}")
    private String baseUrl;

    @Value("${twelvedata.api-key}")
    private String apiKey;

    @Value("${twelvedata.exchange}")
    private String exchange;

    public String getStockBySymbol(String symbol, String stockExchange) {
        String selectedExchange = stockExchange == null || stockExchange.isBlank()
                ? exchange
                : stockExchange.trim();

        return webClient.get()
                .uri(baseUrl + "/quote", uriBuilder -> uriBuilder
                        .queryParam("symbol", symbol.trim().toUpperCase())
                        .queryParam("exchange", selectedExchange)
                        .queryParam("apikey", apiKey.trim())
                        .build())
                .exchangeToMono(response -> response.bodyToMono(String.class))
                .block();
    }

    public String getStockHistory(String symbol, Integer outputSize, String stockExchange) {
        String selectedExchange = stockExchange == null || stockExchange.isBlank() ? exchange : stockExchange;

        return webClient.get().uri(baseUrl + "/time_series", uriBuilder -> uriBuilder.queryParam("symbol", symbol.trim().toUpperCase()).queryParam("exchange", selectedExchange.trim().toUpperCase()).queryParam("interval", "1day").queryParam("outputsize", outputSize).queryParam("apikey", apiKey.trim()).build()).retrieve().bodyToMono(String.class).block();
    }

    public String searchStock(String query) {
        return webClient.get().uri(baseUrl + "/symbol_search", uriBuilder -> uriBuilder.queryParam("symbol", query.trim()).queryParam("apikey", apiKey.trim()).build()).retrieve().bodyToMono(String.class).block();
    }
}