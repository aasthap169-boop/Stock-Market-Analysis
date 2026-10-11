package com.stockmarket.analysis.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "watchlist_items",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_watchlist_user_symbol",
						columnNames = {
								"user_id",
								"symbol"
						}
				)
		}
)
public class WatchlistItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "user_id",
			nullable = false,
			foreignKey = @ForeignKey(
					name = "fk_watchlist_user"
			)
	)
	private User user;

	@Column(
			name = "symbol",
			nullable = false,
			length = 30
	)
	private String symbol;

	@Column(
			name = "company_name",
			nullable = false,
			length = 150
	)
	private String companyName;

	@Column(
			name = "created_at",
			nullable = false,
			updatable = false
	)
	private LocalDateTime createdAt;

	@PrePersist
	public void onCreate() {

		createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}