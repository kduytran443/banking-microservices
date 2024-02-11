package com.kduytran.accounts.service.impl;

import com.kduytran.accounts.dto.*;
import com.kduytran.accounts.entity.Accounts;
import com.kduytran.accounts.entity.Customer;
import com.kduytran.accounts.exception.ResourceNotFoundException;
import com.kduytran.accounts.mapper.AccountsMapper;
import com.kduytran.accounts.mapper.CustomerDetailsMapper;
import com.kduytran.accounts.mapper.CustomerMapper;
import com.kduytran.accounts.repository.AccountsRepository;
import com.kduytran.accounts.repository.CustomerRepository;
import com.kduytran.accounts.service.ICustomersService;
import com.kduytran.accounts.service.client.CardsFeignClient;
import com.kduytran.accounts.service.client.LoansFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ICustomersServiceImpl implements ICustomersService {
    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    @Autowired
    public ICustomersServiceImpl(AccountsRepository accountsRepository,
                                 CustomerRepository customerRepository,
                                 CardsFeignClient cardsFeignClient,
                                 LoansFeignClient loansFeignClient) {
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
        this.cardsFeignClient = cardsFeignClient;
        this.loansFeignClient = loansFeignClient;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", String.valueOf(customer.getCustomerId()))
        );
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());
        CardsDto cardsDto = cardsFeignClient.fetchCardDetails(mobileNumber).getBody();
        LoansDto loansDto = loansFeignClient.fetchLoanDetails(mobileNumber).getBody();

        CustomerDetailsDto customerDetailsDto = CustomerDetailsMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(accountsDto);
        customerDetailsDto.setCardsDto(cardsDto);
        customerDetailsDto.setLoansDto(loansDto);
        return customerDetailsDto;
    }

}
