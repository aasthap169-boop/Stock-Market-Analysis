package com.stockmarket.analysis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stockmarket.analysis.repository.StockRepository;

@Repository
public class StockDao {

    @Autowired
    private StockRepository stockRepository;

    public String getStockBySymbol(String symbol, String exchange) {
        return stockRepository.getStockBySymbol(symbol, exchange);
    }

    public String getStockHistory(String symbol, Integer outputSize, String exchange) {
        return stockRepository.getStockHistory(symbol, outputSize, exchange);
    }

    public String searchStock(String query) {
        return stockRepository.searchStock(query);
    }
}