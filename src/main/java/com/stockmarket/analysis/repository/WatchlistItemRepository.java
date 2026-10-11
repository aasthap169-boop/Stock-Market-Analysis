package com.stockmarket.analysis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockmarket.analysis.entity.WatchlistItem;

public interface WatchlistItemRepository
		extends JpaRepository<WatchlistItem, Long> {

	List<WatchlistItem>
	findByUserIdOrderByCreatedAtDesc(
			Long userId
	);

	Optional<WatchlistItem>
	findByUserIdAndSymbolIgnoreCase(
			Long userId,
			String symbol
	);

	boolean existsByUserIdAndSymbolIgnoreCase(
			Long userId,
			String symbol
	);
}