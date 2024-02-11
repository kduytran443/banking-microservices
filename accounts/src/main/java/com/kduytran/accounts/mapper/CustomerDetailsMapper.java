package com.kduytran.accounts.mapper;

import com.kduytran.accounts.dto.CustomerDetailsDto;
import com.kduytran.accounts.entity.Customer;

public class CustomerDetailsMapper {

    public static CustomerDetailsDto mapToCustomerDetailsDto(Customer customer, CustomerDetailsDto customerDetailsDto) {
        customerDetailsDto.setName(customer.getName());
        customerDetailsDto.setEmail(customer.getEmail());
        customerDetailsDto.setMobileNumber(customer.getMobileNumber());
        return customerDetailsDto;
    }

    public static Customer mapToCustomer(CustomerDetailsDto customerDetailsDto, Customer customer) {
        customer.setName(customerDetailsDto.getName());
        customer.setEmail(customerDetailsDto.getEmail());
        customer.setMobileNumber(customerDetailsDto.getMobileNumber());
        return customer;
    }

}
