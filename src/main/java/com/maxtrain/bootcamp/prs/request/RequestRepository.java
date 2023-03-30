package com.maxtrain.bootcamp.prs.request;

import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Integer> {
	Iterable<Request> findByStatus(String status);
}
