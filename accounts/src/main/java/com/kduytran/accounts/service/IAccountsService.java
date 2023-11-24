package com.kduytran.accounts.service;

import com.kduytran.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     * @param customerDto - CustomerDto object
     */
    void createAccount(CustomerDto customerDto);

    /**
     * @param mobile Number - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    CustomerDto fetchAccount(String mobileNumber);

}
