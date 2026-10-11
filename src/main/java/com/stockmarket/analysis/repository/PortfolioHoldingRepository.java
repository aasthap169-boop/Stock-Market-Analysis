package com.stockmarket.analysis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockmarket.analysis.entity.PortfolioHolding;

public interface PortfolioHoldingRepository extends JpaRepository<PortfolioHolding, Long> {

    List<PortfolioHolding> findByPortfolioIdOrderBySymbolAsc(Long portfolioId);

    Optional<PortfolioHolding> findByPortfolioIdAndSymbolIgnoreCase(Long portfolioId, String symbol);
}