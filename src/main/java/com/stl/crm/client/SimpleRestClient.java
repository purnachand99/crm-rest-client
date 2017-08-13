package com.stl.crm.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.stl.crm.model.Customer;

public class SimpleRestClient {
	private static final String CRM_JPA_URI = "http://localhost:8080/crm-jpa/customers";
	private RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SimpleRestClient simpleRestClient = new SimpleRestClient();
		simpleRestClient.runRestClient();
	}
	
	
	public void runRestClient() {

		createCustomer();
//		getCustomers();		
//		getCustomer(3L);
//		updateCustomer();
//		deleteCustomer(4L);
		getCustomerEntity(3L);
		
	}

	/**
	 * 
	 * add a new Customer
	 * 
	 */
	private void createCustomer() {
		Customer customer = new Customer();
		
		customer.setName("Dell Inc");
		customer.setAddress("101 Dell way");
		customer.setPhone("101-202-3033");
		customer.setContact("CIO");

		Customer savedCustomer = restTemplate.postForObject(CRM_JPA_URI, customer, Customer.class);
		
		System.out.println("savedCustomer: " + savedCustomer);
	}
	
	/**
	 * 
	 * get all Customers
	 * 
	 */
	private void getCustomers() {
		Customer[] customers = restTemplate.getForObject(CRM_JPA_URI, Customer[].class);
		for (Customer customer : customers) {
			System.out.println(customer);
		}
	}

	/**
	 * 
	 * get a particular Customer
	 * @param customerId
	 * @return
	 */
	private Customer getCustomer(Long customerId) {
		Customer customer = restTemplate.getForObject(CRM_JPA_URI + "/{customerId}", Customer.class, customerId);
		System.out.println("customer: " + customer);
		
		return customer;
	}

	/**
	 * 
	 * update an existing Customer
	 * 
	 */
	private void updateCustomer() {
		Customer customer = getCustomer(4L);
		customer.setAddress("100 Dell way");
		
		restTemplate.put(CRM_JPA_URI + "/{customerId}", customer, customer.getId());
		
		Customer updatedCustomer = getCustomer(4L);
		System.out.println("updatedCustomer: " + updatedCustomer);
		
	}
	
	/**
	 * 
	 * delete a Customer
	 * @param customerId
	 * 
	 */
	private void deleteCustomer(Long customerId) {
		restTemplate.delete(CRM_JPA_URI + "/{customerId}", customerId);
	}
	
	/**
	 * 
	 * get a particular Customer using getForEntity
	 * @param customerId
	 * @return
	 */
	private Customer getCustomerEntity(Long customerId) {
		ResponseEntity entity = restTemplate.getForEntity(CRM_JPA_URI + "/{customerId}", Customer.class, customerId);
		Customer customer = (Customer) entity.getBody();
		System.out.println("customer: " + customer);

		//-- getting response header info 
		HttpHeaders responseHeaders = entity.getHeaders();
		responseHeaders.entrySet().forEach(header-> {
			System.out.println(header.getKey() + ":" + header.getValue());
		});

		//-- getting response status info
		System.out.println("status name: " + entity.getStatusCode().name());
		System.out.println("status value: " + entity.getStatusCode().value());
		
		return customer;
	}
}
