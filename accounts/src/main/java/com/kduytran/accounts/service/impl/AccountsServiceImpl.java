package com.kduytran.accounts.service.impl;

import com.kduytran.accounts.constants.AccountsConstants;
import com.kduytran.accounts.dto.AccountsDto;
import com.kduytran.accounts.dto.CustomerDto;
import com.kduytran.accounts.entity.Accounts;
import com.kduytran.accounts.entity.Customer;
import com.kduytran.accounts.exception.CustomerAlreadyExistsException;
import com.kduytran.accounts.exception.ResourceNotFoundException;
import com.kduytran.accounts.mapper.AccountsMapper;
import com.kduytran.accounts.mapper.CustomerMapper;
import com.kduytran.accounts.repository.AccountsRepository;
import com.kduytran.accounts.repository.CustomerRepository;
import com.kduytran.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     * @param customerDto - CustomerDto object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException(String.format("Customer already registered with given mobileNumber %s",
                    customerDto.getMobileNumber()));
        }

        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * @param mobileNumber@return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }
}
