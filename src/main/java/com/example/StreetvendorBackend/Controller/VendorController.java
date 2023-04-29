package com.example.StreetvendorBackend.Controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.StreetvendorBackend.Entity.Product;
import com.example.StreetvendorBackend.Entity.Vendor;
import com.example.StreetvendorBackend.Modal.LoginRequest;
import com.example.StreetvendorBackend.Modal.RequestProduct;
import com.example.StreetvendorBackend.Modal.RequestVendor;
import com.example.StreetvendorBackend.Modal.ResponseVendor;
import com.example.StreetvendorBackend.Repositrory.VendorRepository;
import com.example.StreetvendorBackend.Service.VendorServices;

@RestController
@RequestMapping("vendor")
public class VendorController {
	
	//register , login Profile , Change Password , Home
	
	@Autowired
	private VendorServices vendorservice;
	
	@Autowired
	private VendorRepository vendorrepoisitory; 	
	
	@PostMapping("/register")
	public ResponseEntity<ResponseVendor> registervendor(@RequestBody RequestVendor requestvendor){

		
		ResponseVendor vendor=vendorservice.RegisterVendor(requestvendor);
		
		return new ResponseEntity<>(vendor, HttpStatus.OK);
	}
	
	@PostMapping("/addproduct/{vendorid}")
	public boolean addproduct(@RequestBody RequestProduct product,@PathVariable("vendorid") Long vendor_id) {
		boolean b=vendorservice.addproduct(product , vendor_id);
		
		return b;
	}
	
	@GetMapping("/getproducts/{vendorid}")
	public ResponseEntity<ArrayList<Product>> getproducts(@PathVariable("vendorid") Long vendor_id){
		ArrayList<Product> set = null;
		set=vendorservice.Getproductbyvendorid(vendor_id);
		return new ResponseEntity<>(set, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{productid}")
	public void deleteproduct(@PathVariable("productid") Long product_id){
		
		vendorservice.Deleteproduct(product_id);
		return ;
	}
	
	@PutMapping("/update/{productid}")
	public ResponseEntity<Product> updateproduct(
			@PathVariable("productid") Long product_id,
			@RequestBody RequestProduct product) {
		 Product p =vendorservice.upadteproduct(product,product_id);
		 return new ResponseEntity<>(p,HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<Vendor> login(@RequestBody LoginRequest req){

		 return vendorservice.getVendorByVendorUsernameAndPassword(req.getVendorusername(),req.getPassword());
    }
	
	@PutMapping("/changepassword")
	public  ResponseEntity<Vendor> Changepassword(@RequestBody LoginRequest req) {
		return vendorservice.changepassword(req.getVendorusername(),req.getPassword());
		
	}
		
}
