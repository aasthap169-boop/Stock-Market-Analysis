package com.stockmarket.analysis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockmarket.analysis.entity.PortfolioTransaction;

public interface PortfolioTransactionRepository
		extends JpaRepository<PortfolioTransaction, Long> {

	List<PortfolioTransaction>
	findByPortfolioIdOrderByCreatedAtDesc(
			Long portfolioId
	);
}