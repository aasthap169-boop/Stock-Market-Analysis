package com.stockmarket.analysis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockmarket.analysis.dto.BuyStockRequest;
import com.stockmarket.analysis.dto.HoldingResponse;
import com.stockmarket.analysis.dto.PortfolioResponse;
import com.stockmarket.analysis.dto.ResponseStructure;
import com.stockmarket.analysis.dto.SellStockRequest;
import com.stockmarket.analysis.dto.TransactionResponse;
import com.stockmarket.analysis.service.PortfolioServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioServices portfolioServices;

    @GetMapping
    public ResponseEntity<ResponseStructure<PortfolioResponse>> getPortfolio() {
        return portfolioServices.getPortfolio();
    }

    @PostMapping("/buy")
    public ResponseEntity<ResponseStructure<TransactionResponse>> buyStock(@Valid @RequestBody BuyStockRequest request) {
        return portfolioServices.buyStock(request);
    }

    @PostMapping("/sell")
    public ResponseEntity<ResponseStructure<TransactionResponse>> sellStock(@Valid @RequestBody SellStockRequest request) {
        return portfolioServices.sellStock(request);
    }

    @GetMapping("/holdings")
    public ResponseEntity<ResponseStructure<List<HoldingResponse>>> getHoldings() {
        return portfolioServices.getHoldings();
    }

    @GetMapping("/transactions")
    public ResponseEntity<ResponseStructure<List<TransactionResponse>>> getTransactions() {
        return portfolioServices.getTransactions();
    }
}