package com.stockmarket.analysis.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stockmarket.analysis.entity.WatchlistItem;
import com.stockmarket.analysis.repository.WatchlistItemRepository;

@Repository
public class WatchlistItemDao {

	@Autowired
	private WatchlistItemRepository watchlistItemRepository;

	public WatchlistItem saveWatchlistItem(
			WatchlistItem item) {

		return watchlistItemRepository.save(item);
	}

	public List<WatchlistItem> getWatchlistByUserId(
			Long userId) {

		return watchlistItemRepository
				.findByUserIdOrderByCreatedAtDesc(userId);
	}

	public Optional<WatchlistItem>
	getWatchlistItemByUserIdAndSymbol(
			Long userId,
			String symbol) {

		return watchlistItemRepository
				.findByUserIdAndSymbolIgnoreCase(
						userId,
						symbol
				);
	}

	public boolean isStockAlreadyInWatchlist(
			Long userId,
			String symbol) {

		return watchlistItemRepository
				.existsByUserIdAndSymbolIgnoreCase(
						userId,
						symbol
				);
	}

	public void deleteWatchlistItem(
			WatchlistItem item) {

		watchlistItemRepository.delete(item);
	}
}