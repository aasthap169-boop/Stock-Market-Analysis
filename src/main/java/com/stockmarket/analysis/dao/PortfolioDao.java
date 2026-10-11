package com.stockmarket.analysis.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stockmarket.analysis.entity.Portfolio;
import com.stockmarket.analysis.repository.PortfolioRepository;

@Repository
public class PortfolioDao {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public Optional<Portfolio> getPortfolioByUserId(Long userId) {
        return portfolioRepository.findByUserId(userId);
    }
}