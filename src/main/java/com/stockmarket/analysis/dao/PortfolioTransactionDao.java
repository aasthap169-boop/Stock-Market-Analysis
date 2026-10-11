package com.stockmarket.analysis.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stockmarket.analysis.entity.PortfolioTransaction;
import com.stockmarket.analysis.repository.PortfolioTransactionRepository;

@Repository
public class PortfolioTransactionDao {

    @Autowired
    private PortfolioTransactionRepository portfolioTransactionRepository;

    public PortfolioTransaction saveTransaction(PortfolioTransaction transaction) {
        return portfolioTransactionRepository.save(transaction);
    }

    public List<PortfolioTransaction> getTransactionsByPortfolioId(Long portfolioId) {
        return portfolioTransactionRepository.findByPortfolioIdOrderByCreatedAtDesc(portfolioId);
    }
}