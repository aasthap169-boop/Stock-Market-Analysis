package com.stockmarket.analysis.entity;

import java.time.LocalDateTime;

import com.stockmarket.analysis.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "users",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_users_email",
						columnNames = "email"
				)
		}
)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(
			name = "first_name",
			nullable = false,
			length = 50
	)
	private String firstName;

	@Column(
			name = "last_name",
			nullable = false,
			length = 50
	)
	private String lastName;

	@Column(
			name = "email",
			nullable = false,
			unique = true,
			length = 100
	)
	private String email;

	@Column(
			name = "password",
			nullable = false
	)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(
			name = "role",
			nullable = false,
			length = 20
	)
	private Role role = Role.USER;

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

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}