package com.maxtrain.bootcamp.prs.request;

import java.util.ArrayList;
import java.util.Optional;

import org.hibernate.mapping.List;
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

import com.maxtrain.bootcamp.prs.user.User;
import com.maxtrain.bootcamp.prs.user.UserRepository;

import jakarta.persistence.*;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {
	private final String Status_New = "NEW";
	private final String Status_Review = "REVIEW";
	private final String Status_Approved = "APPROVED";
	private final String Status_Rejected = "REJECTED";

		@Autowired
		private RequestRepository requestRepo;
		
		@Autowired
		private UserRepository userRepo;
		
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
		@GetMapping("reviews/{userId}")
		public ResponseEntity<Iterable<Request>> GetReviews(@PathVariable int userId){
			//retrieve user and check if empty
			Optional<User> user = userRepo.findById(userId);
			if(user.isEmpty() || user.get().getId() != userId) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			//retrieve requests with status of review
			Iterable<Request> requests = requestRepo.findByStatus(Status_Review);
			//make a list that stores these requests so it can be altered in loop
			ArrayList<Request> reviewRequests = new ArrayList<Request>();
			for(var r : requests) {
				if(r.getUser().getId() != userId) {
				//add the request to list if the userId does not match
				reviewRequests.add(r);
				}
			}
			//return new collection
			return new ResponseEntity<Iterable<Request>>(reviewRequests, HttpStatus.OK);
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
		public ResponseEntity reviewRequest(@PathVariable int id, @RequestBody Request request) {
			if(request.getId() != id) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			if(request.getTotal() <= 50) {
				request.setStatus("APPROVED");
			}
			else if(request.getTotal() > 50) {
				request.setStatus("REVIEW");
			}
			requestRepo.save(request);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		@SuppressWarnings("rawtypes")
		@PutMapping("approve/{id}")
		public ResponseEntity approveRequest(@PathVariable int id, @RequestBody Request request) {
			if(request.getId() != id) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			request.setStatus("APPROVED");
			requestRepo.save(request);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		@SuppressWarnings("rawtypes")
		@PutMapping("reject/{id}")
		public ResponseEntity rejectRequest(@PathVariable int id, @RequestBody Request request) {
			if(request.getId() != id) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			request.setStatus("REJECTED");
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
