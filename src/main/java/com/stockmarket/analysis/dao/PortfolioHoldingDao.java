package com.stockmarket.analysis.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stockmarket.analysis.entity.PortfolioHolding;
import com.stockmarket.analysis.repository.PortfolioHoldingRepository;

@Repository
public class PortfolioHoldingDao {

    @Autowired
    private PortfolioHoldingRepository portfolioHoldingRepository;

    public PortfolioHolding saveHolding(PortfolioHolding holding) {
        return portfolioHoldingRepository.save(holding);
    }

    public List<PortfolioHolding> getHoldingsByPortfolioId(Long portfolioId) {
        return portfolioHoldingRepository.findByPortfolioIdOrderBySymbolAsc(portfolioId);
    }

    public Optional<PortfolioHolding> getHoldingByPortfolioIdAndSymbol(Long portfolioId, String symbol) {
        return portfolioHoldingRepository.findByPortfolioIdAndSymbolIgnoreCase(portfolioId, symbol);
    }

    public void deleteHolding(PortfolioHolding holding) {
        portfolioHoldingRepository.delete(holding);
    }
}