package com.stl.crm.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import com.stl.crm.model.Customer;

public class OAuth2Client {
	private static final String CRM_OAUTH2_URI = "http://localhost:8080/crm-oauth2/api/customers";
	private OAuth2RestTemplate oauth2RestTemplate = null;

	public static void main(String[] args) {
		OAuth2Client oauth2Client = new OAuth2Client();
		oauth2Client.runOAuth2Client();
	}
	
	
	public void runOAuth2Client() {
		//-- get the OAuth2 Rest template first
		oauth2RestTemplate = restTemplate();
		
		getCustomers();
//		getCustomer(3L);
//		createCustomer();
//		updateCustomer();
//		deleteCustomer(9L);
//		getCustomerEntity(8L);
		
	}
	
	/**
	 * 
	 * get all Customers
	 * 
	 */
	private void getCustomers() {
		Customer[] customers = oauth2RestTemplate.getForObject(CRM_OAUTH2_URI, Customer[].class);
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
		Customer customer = oauth2RestTemplate.getForObject(CRM_OAUTH2_URI + "/{customerId}", Customer.class, customerId);
		System.out.println("customer: " + customer);
		
		return customer;
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

		Customer savedCustomer = oauth2RestTemplate.postForObject(CRM_OAUTH2_URI, customer, Customer.class);
		
		System.out.println("savedCostomer: " + savedCustomer);
	}
	
	/**
	 * 
	 * update an existing Customer
	 * 
	 */
	private void updateCustomer() {
		Customer customer = getCustomer(4L);
		customer.setAddress("100 Dell way");
		
		oauth2RestTemplate.put(CRM_OAUTH2_URI + "/{customerId}", customer, customer.getId());
		
		Customer updatedCustomer = getCustomer(4L);
		System.out.println("updatedCostomer: " + updatedCustomer);
		
	}
	
	/**
	 * 
	 * delete a Customer
	 * @param customerId
	 * 
	 */
	private void deleteCustomer(Long customerId) {
		oauth2RestTemplate.delete(CRM_OAUTH2_URI + "/{customerId}", customerId);
	}

	/**
	 * 
	 * get a particular Customer using getForEntity
	 * @param customerId
	 * @return
	 */
	private Customer getCustomerEntity(Long customerId) {
		ResponseEntity entity = oauth2RestTemplate.getForEntity(CRM_OAUTH2_URI + "/{customerId}", Customer.class, customerId);
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
	
	/**
	 * 
	 * OAuth2 Rest template
	 * @return
	 */
	private OAuth2RestTemplate restTemplate() {
		System.out.println("getting OAuth2RestTemplate ...");

		ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
		resourceDetails.setGrantType("password");
		resourceDetails.setAccessTokenUri("http://localhost:8080/crm-oauth2/oauth/token");
	
		//-- set the clients info
		resourceDetails.setClientId("crmClient1");
		resourceDetails.setClientSecret("crmSuperSecret");
		
		// set scopes
		List<String> scopes = new ArrayList<>();
		scopes.add("read"); 
		scopes.add("write");
		scopes.add("trust");
		resourceDetails.setScope(scopes);
		
		//-- set Resource Owner info
		resourceDetails.setUsername("crmadmin");
		resourceDetails.setPassword("adminpass");
		
		return new OAuth2RestTemplate(resourceDetails);	
	}

}
