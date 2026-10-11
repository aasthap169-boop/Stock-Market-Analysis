package com.stockmarket.analysis.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "portfolios")
public class Portfolio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "user_id",
			nullable = false,
			unique = true,
			foreignKey = @ForeignKey(
					name = "fk_portfolio_user"
			)
	)
	private User user;

	@Column(
			name = "cash_balance",
			nullable = false,
			precision = 19,
			scale = 2
	)
	private BigDecimal cashBalance =
			new BigDecimal("100000.00");

	@Column(
			name = "created_at",
			nullable = false,
			updatable = false
	)
	private LocalDateTime createdAt;

	@Column(
			name = "updated_at",
			nullable = false
	)
	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {

		LocalDateTime now = LocalDateTime.now();

		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	public void onUpdate() {

		updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BigDecimal getCashBalance() {
		return cashBalance;
	}

	public void setCashBalance(BigDecimal cashBalance) {
		this.cashBalance = cashBalance;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}