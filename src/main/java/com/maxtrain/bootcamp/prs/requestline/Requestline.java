package com.maxtrain.bootcamp.prs.requestline;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxtrain.bootcamp.prs.product.Product;
import com.maxtrain.bootcamp.prs.request.Request;

import jakarta.persistence.*;


@Entity
@Table(name="requestlines")
public class Requestline {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(columnDefinition="int not null default 1")
	private int quantity;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="requestId", referencedColumnName="id")
	@JsonProperty("requestId")
	private Request request;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="productId", referencedColumnName="id")
	@JsonProperty("productId")
	private Product product;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@JsonIgnore
	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
	@JsonIgnore
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	
	
}
