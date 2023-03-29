package com.maxtrain.bootcamp.prs.vendor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.maxtrain.bootcamp.prs.vendor.Vendor;

@CrossOrigin
@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorRepository vendorRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Vendor>> getVendors(){
		Iterable<Vendor> vendors = vendorRepo.findAll();
		return new ResponseEntity<Iterable<Vendor>>(vendors, HttpStatus.OK);
	}
	@GetMapping("{id}")
	public ResponseEntity<Vendor> getVendor(@PathVariable int id){
		Optional<Vendor> vendor = vendorRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vendor>(vendor.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Vendor> postVendor(@RequestBody Vendor vendor){
		Vendor savedVendor = vendorRepo.save(vendor);
		return new ResponseEntity<Vendor>(savedVendor, HttpStatus.CREATED);
	}
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putVendor(@PathVariable int id, @RequestBody Vendor vendor) {
		if(vendor.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		vendorRepo.save(vendor);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity<Vendor> deleteVendor(@PathVariable int id){
		Optional<Vendor> vendor = vendorRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		vendorRepo.delete(vendor.get());
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
