package com.example.StreetvendorBackend.Service;


import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.StreetvendorBackend.Entity.Product;
import com.example.StreetvendorBackend.Entity.Vendor;
import com.example.StreetvendorBackend.Exception.ProductServiceException;
import com.example.StreetvendorBackend.Modal.LoginRequest;
import com.example.StreetvendorBackend.Modal.RequestProduct;
import com.example.StreetvendorBackend.Modal.RequestVendor;
import com.example.StreetvendorBackend.Modal.ResponseVendor;
import com.example.StreetvendorBackend.Repositrory.ProductRepository;
import com.example.StreetvendorBackend.Repositrory.VendorRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class VendorServices {

	@Autowired
	private VendorRepository vendorrepository;
	
	@Autowired
	private ProductRepository productrepository;
	
	public ResponseVendor RegisterVendor(RequestVendor requestvendor) {
		log.info("registering vendor!!");
		Vendor vendor=Vendor.builder()
				.vendorusername(requestvendor.getVendorname())
				.vendorcontact(requestvendor.getVendorcontact())
				.latitude(requestvendor.getLatitude())
				.longitude(requestvendor.getLongitude())
				.location(requestvendor.getLocation())
				.shopname(requestvendor.getShopname())
				.build();
		vendorrepository.save(vendor);
		log.info("registered vendor!!");
		
		ResponseVendor ven= new ResponseVendor();
		ven.setId(vendor.getId());
		ven.setLatitude(vendor.getLatitude());
		ven.setLocation(vendor.getLocation());
		ven.setLongitude(vendor.getLongitude());
		ven.setShopname(vendor.getShopname());
		ven.setVendorcontact(vendor.getVendorcontact());
		ven.setVendorname(vendor.getVendorusername());
		return ven;
	}

	public boolean addproduct(RequestProduct requestproduct , Long vendorid) {

		log.info("finding vendor");
		Vendor vendor=vendorrepository.findById(vendorid).orElseThrow( () -> new ProductServiceException("vendor id not found " ,"VENDOR_NOT_FOUND" ));
		log.info("found vendor");
		Product product= Product.builder()
				.productname(requestproduct.getProductname())
				.price(requestproduct.getPrice())
				.vendor(vendor)
				.build();
		
		productrepository.save(product);
		
		log.info("product created");
		
		
		Set<Product> s=vendor.getProducts();
		s.add(product);
		vendor.setProducts(s);
		log.info("vendor products updated");
		return true;
	}

	
	public ArrayList<Product> Getproductbyvendorid(Long vendor_id) {

		log.info("finding vendor....");
		Vendor vendor=vendorrepository.findById(vendor_id).orElseThrow( () -> new ProductServiceException("vendor id not found " ,"VENDOR_NOT_FOUND" ));
		log.info("found vendor");
		Set <Product> s1=productrepository.findAllByVendor(vendor);
		ArrayList<Product> al=new ArrayList<>();
		
		al.addAll(s1);
		log.info("returned products");
		return al;
	}

	public void Deleteproduct(Long product_id) {
		log.info("finding product");
		Product product=productrepository.findById(product_id).orElseThrow( () -> new ProductServiceException("product id not found " ,"PRODUCT_NOT_FOUND" ));
		log.info("found product");
		
		productrepository.deleteById(product.getProductid());
		
		
	}

	public Product upadteproduct(RequestProduct requestproduct, Long product_id) {
			
		log.info("finding product");
		Product product=productrepository.findById(product_id).orElseThrow( () -> new ProductServiceException("product id not found " ,"PRODUCT_NOT_FOUND" ));
		log.info("found product");
		
		product.setPrice(requestproduct.getPrice());
		product.setProductname(requestproduct.getProductname());
		log.info("new product"+ product);
		return productrepository.save(product);
	}
	
	public 	ResponseEntity<Vendor> getVendorByVendorUsernameAndPassword(String vendorUsername, String password) {

			log.info(vendorUsername + "  " + password);  
			Optional<Vendor> optionalVendor = vendorrepository.findByVendorusernameAndPassword(vendorUsername, password);
		      if (optionalVendor.isPresent()) {
		    	  log.info("found ");
		          Vendor vendor = optionalVendor.get();
		          return ResponseEntity.ok(vendor);
		      } else {
		    	  log.info("not found");
		          return ResponseEntity.notFound().build();
		      } 	
		}

	
	public ResponseEntity<Vendor> changepassword(String vendorusername, String password) {
		Optional<Vendor> optionalVendor = vendorrepository.findByVendorusername(vendorusername);
		if (optionalVendor.isPresent()) {
	    	  log.info("found");
	    	  Vendor v=optionalVendor.get();
	    	  v.setPassword(password);
	    	  vendorrepository.save(v);
	          return ResponseEntity.ok(v);
	      } else {
	    	  log.info("not found");
	          return ResponseEntity.notFound().build();
	      } 	
		
	}



	
	
	
	
}
