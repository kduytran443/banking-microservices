package com.kduytran.accounts.service.impl;

import com.kduytran.accounts.dto.AccountsDto;
import com.kduytran.accounts.dto.CardsDto;
import com.kduytran.accounts.dto.CustomerDetailsDto;
import com.kduytran.accounts.dto.LoansDto;
import com.kduytran.accounts.entity.Accounts;
import com.kduytran.accounts.entity.Customer;
import com.kduytran.accounts.exception.ResourceNotFoundException;
import com.kduytran.accounts.mapper.AccountsMapper;
import com.kduytran.accounts.mapper.CustomerDetailsMapper;
import com.kduytran.accounts.repository.AccountsRepository;
import com.kduytran.accounts.repository.CustomerRepository;
import com.kduytran.accounts.service.ICustomersService;
import com.kduytran.accounts.service.client.CardsFeignClient;
import com.kduytran.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomersServiceImpl implements ICustomersService {
    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    public CustomersServiceImpl(AccountsRepository accountsRepository,
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
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", String.valueOf(customer.getCustomerId()))
        );

        CustomerDetailsDto customerDetailsDto = CustomerDetailsMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if (cardsDtoResponseEntity != null) {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        if (loansDtoResponseEntity != null) {
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

        customerDetailsDto.setAccountsDto(accountsDto);
        return customerDetailsDto;
    }

}
