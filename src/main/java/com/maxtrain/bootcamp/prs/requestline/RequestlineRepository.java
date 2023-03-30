package com.maxtrain.bootcamp.prs.requestline;

import org.springframework.data.repository.CrudRepository;



public interface RequestlineRepository extends CrudRepository<Requestline, Integer>{
	Iterable<Requestline> findByRequestId(int requestId);
}
