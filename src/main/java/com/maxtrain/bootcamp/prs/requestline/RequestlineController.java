package com.maxtrain.bootcamp.prs.requestline;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxtrain.bootcamp.prs.product.Product;
import com.maxtrain.bootcamp.prs.product.ProductRepository;
import com.maxtrain.bootcamp.prs.request.Request;
import com.maxtrain.bootcamp.prs.request.RequestRepository;



@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlineController {

	@Autowired
	private RequestlineRepository reqLineRepo;
	@Autowired
	private RequestRepository reqRepo;
	@Autowired
	private ProductRepository prodRepo;
	
	private boolean recalculateRequestTotal(int requestId) {
		Optional<Request> aRequest = reqRepo.findById(requestId);
		if(aRequest.isEmpty()) {
			return false;
		}
		Request request = aRequest.get();
		Iterable<Requestline> requestlines = reqLineRepo.findByRequestId(requestId);
		double total = 0;
		for(Requestline rl : requestlines) {
			if(rl.getProduct().getName() == null) {
				Product product = prodRepo.findById(rl.getProduct().getId()).get();
				rl.setProduct(product);
			}
			total += rl.getProduct().getPrice() * rl.getQuantity();
		}
		request.setTotal(total);
		reqRepo.save(request);
		return true;
	}
	
	@GetMapping
	public ResponseEntity<Iterable<Requestline>> getRequestlines(){
		Iterable<Requestline> requestlines = reqLineRepo.findAll();
		return new ResponseEntity<Iterable<Requestline>>(requestlines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Requestline> getOrderline(@PathVariable int id){
		Optional<Requestline> requestline = reqLineRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Requestline>(requestline.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Requestline> postRequestline(@RequestBody Requestline requestline){
		Requestline newRequestline = (Requestline) reqLineRepo.save(requestline);
		Optional<Request> request = reqRepo.findById(requestline.getRequest().getId());
		if(!request.isEmpty()) {
			boolean success = recalculateRequestTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<Requestline>(newRequestline, HttpStatus.CREATED);
	}
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity<Requestline> putRequestline(@PathVariable int id, @RequestBody Requestline requestline){
		if(requestline.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		reqLineRepo.save(requestline);
		Optional<Request> request = reqRepo.findById(requestline.getRequest().getId());
		if(!request.isEmpty()) {
			boolean success = recalculateRequestTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity<Requestline> deleteRequestline(@PathVariable int id){
		Optional<Requestline> requestline = reqLineRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		reqLineRepo.delete(requestline.get());
		Optional<Request> request = reqRepo.findById(requestline.get().getRequest().getId());
		if(!request.isEmpty()) {
			boolean success = recalculateRequestTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
