package org.fasttrackit.onlineshop;

import org.fasttrackit.onlineshop.domain.Customer;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.service.CustomerService;
import org.fasttrackit.onlineshop.transfer.SaveCustomerRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceIntegrationTests {
    @Autowired
    private CustomerService customerService;

    @Test
    public void testCreateCustomer_whenValidRequest_thenCustomerIsSaved() {
        createCustomer();
    }


    @Test(expected = TransactionSystemException.class)
    public void testCreateCustomer_whenInvalidRequest_thenThrowException() {
        SaveCustomerRequest request = new SaveCustomerRequest();
        //leaving request properties with default null values
        //to validate the negative flow

        customerService.createCustomer(request);
    }

    @Test
    public void testGetCustomer_whenExistingCustomer_thenReturnCustomer() {
        Customer createdCustomer = createCustomer();

        Customer customer = customerService.getCustomer(createdCustomer.getId());

        assertThat(customer, notNullValue());
        assertThat(customer.getId(), is(createdCustomer.getId()));
        assertThat(customer.getFirstName(), is(createdCustomer.getFirstName()));
        assertThat(customer.getLastName(), is(createdCustomer.getLastName()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetCustomer_whenNonExistingCustomer_thenThrowResourceNotFoundException() {
        customerService.getCustomer(99999999L);
    }

    @Test
    public void testUpdateCustomer_whenValidRequest_thenReturnUpdateCustomer(){
    	Customer createdCustomer = createCustomer();

    	SaveCustomerRequest request =new SaveCustomerRequest();
    	request.setFirstName(createdCustomer.getFirstName() +"update");
    	request.setLastName((createdCustomer.getLastName() +"update"));


		Customer updatedCustomer = customerService.updateCustomer(createdCustomer.getId(), request);
		assertThat(updatedCustomer, notNullValue());
		assertThat(updatedCustomer.getFirstName(), is(request.getFirstName()));
		assertThat(updatedCustomer.getLastName(), is(request.getLastName()));


      	}

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteCustomer_whenExistingCustomer_thenCustomerIsDeleted(){
        Customer customer = createCustomer();
        customerService.deleteCustomer(customer.getId());
        customerService.getCustomer(customer.getId());
    }
    private Customer createCustomer() {
        SaveCustomerRequest request = new SaveCustomerRequest();
        request.setFirstName("Ionel" + System.currentTimeMillis());
        request.setLastName("Pop" + System.currentTimeMillis());

        Customer createdCustomer = customerService.createCustomer(request);

        assertThat(createdCustomer, notNullValue());
        assertThat(createdCustomer.getId(), notNullValue());
        assertThat(createdCustomer.getId(), greaterThan(0L));

        assertThat(createdCustomer.getFirstName(), is(request.getFirstName()));
        assertThat(createdCustomer.getLastName(), is(request.getLastName()));

        return createdCustomer;
    }
}
