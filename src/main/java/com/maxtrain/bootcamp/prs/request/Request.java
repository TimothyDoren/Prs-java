package com.maxtrain.bootcamp.prs.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maxtrain.bootcamp.prs.user.User;

import jakarta.persistence.*;


@Entity
@Table(name="Requests")
@JsonIgnoreProperties({"requestlines"})
public class Request {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(length=80, nullable=false)
	private String description;
	@Column(length=80, nullable=false)
	private String justification;
	@Column(length=80, nullable=true)
	private String rejectionReason;
	@Column(length=20, nullable=false, columnDefinition="VARCHAR(20) DEFAULT 'Pickup'")
	private String deliveryMode;
	@Column(length=10, nullable=false, columnDefinition="VARCHAR(10) DEFAULT 'NEW'")
	private String status;
	@Column(columnDefinition="decimal(11,2) NOT NULL DEFAULT 0")
	private double total;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="userId", columnDefinition="int")
	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
