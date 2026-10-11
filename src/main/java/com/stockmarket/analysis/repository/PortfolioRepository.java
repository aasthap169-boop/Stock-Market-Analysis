package com.stockmarket.analysis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockmarket.analysis.entity.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByUserId(Long userId);
}