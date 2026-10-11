package com.stockmarket.analysis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stockmarket.analysis.dto.ResponseStructure;
import com.stockmarket.analysis.dto.StockHistory;
import com.stockmarket.analysis.dto.StockQuote;
import com.stockmarket.analysis.dto.StockSearch;
import com.stockmarket.analysis.service.StockServices;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockServices stockServices;

    @GetMapping("/search")
    public ResponseEntity<ResponseStructure<List<StockSearch>>> searchStock(@RequestParam String query) {
        return stockServices.searchStock(query);
    }

    @GetMapping("/{symbol}/history")
    public ResponseEntity<ResponseStructure<List<StockHistory>>> getStockHistory(@PathVariable String symbol, @RequestParam(defaultValue = "30") Integer outputSize, @RequestParam(required = false) String exchange) {
        return stockServices.getStockHistory(symbol, outputSize, exchange);
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<ResponseStructure<StockQuote>> getStockBySymbol(@PathVariable String symbol, @RequestParam(required = false) String exchange) {
        return stockServices.getStockBySymbol(symbol, exchange);
    }
}