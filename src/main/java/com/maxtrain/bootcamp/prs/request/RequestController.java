package com.maxtrain.bootcamp.prs.request;

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


import jakarta.persistence.*;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {

		@Autowired
		private RequestRepository requestRepo;
		
		@GetMapping
		public ResponseEntity<Iterable<Request>> GetRequests(){
			Iterable<Request> requests = requestRepo.findAll();
			return new ResponseEntity<Iterable<Request>>(requests, HttpStatus.OK);
		}
		@GetMapping("{id}")
		public ResponseEntity<Request> GetRequest(@PathVariable int id){
			Optional<Request> request = requestRepo.findById(id);
			if(request.isEmpty()) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Request>(request.get(), HttpStatus.OK);
		}
		@PostMapping
		public ResponseEntity<Request> postRequest(@RequestBody Request request){
			Request savedRequest = requestRepo.save(request);
			return new ResponseEntity<Request>(savedRequest, HttpStatus.OK);
		}
		@SuppressWarnings("rawtypes")
		@PutMapping("{id}")
		public ResponseEntity putRequest(@PathVariable int id, @RequestBody Request request){
			if(request.getId() != id) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			requestRepo.save(request);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		@SuppressWarnings("rawtypes")
		@PutMapping("review/{id}")
		public ResponseEntity approveRequest(@PathVariable int id, @RequestBody Request request) {
			if(request.getId() != id) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			if(request.getTotal() <= 50) {
				request.setStatus("APPROVED");
			}
			request.setStatus("REVIEW");
			requestRepo.save(request);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		@SuppressWarnings("rawtypes")
		@DeleteMapping("{id}")
		public ResponseEntity<Request> deleteRequest(@PathVariable int id){
			Optional<Request> request = requestRepo.findById(id);
			if(request.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			requestRepo.delete(request.get());
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
}
