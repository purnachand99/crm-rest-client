package com.stl.crm.client;

import java.util.ArrayList;
import java.util.List;

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
		
//		getCustomerEntity(3L);
		
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

		Customer savedCostomer = oauth2RestTemplate.postForObject(CRM_OAUTH2_URI, customer, Customer.class);
		
		System.out.println("savedCostomer: " + savedCostomer);
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
		
		Customer updatedCostomer = getCustomer(4L);
		System.out.println("updatedCostomer: " + updatedCostomer);
		
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
	 * OAuth2 Rest template
	 * @return
	 */
	private OAuth2RestTemplate restTemplate() {
		ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
		resourceDetails.setGrantType("password");
		resourceDetails.setAccessTokenUri("http://localhost:8080/crm-oauth2/oauth/token");
		
		System.out.println("getting OAuth2RestTemplate ...");
		
		//-- set the clients info
		resourceDetails.setClientId("crmClient1");
		resourceDetails.setClientSecret("crmSuperSecret");
		
		// Set scopes
		List<String> scopes = new ArrayList<>();
		scopes.add("read"); 
		scopes.add("write");
		scopes.add("trust");
		resourceDetails.setScope(scopes);
		
		//-- Resource Owner info
		resourceDetails.setUsername("crmadmin");
		resourceDetails.setPassword("adminpass");
		
		return new OAuth2RestTemplate(resourceDetails);	
	}

}
