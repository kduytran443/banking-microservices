package com.kduytran.loans.service;

import com.kduytran.loans.dto.LoansDto;

public interface ILoansService {

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    void createLoan(String mobileNumber);

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Loans details based on a given mobileNumber
     */
    LoansDto fetchLoan(String mobileNumber);

    /**
     * @param loansDto - LoansDto object
     * @return boolean indicating if the update of Loan Details is successful or not
     */
    boolean updateLoan(LoansDto loansDto);

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Loan Details is successful or not
     */
    boolean deleteLoan(String mobileNumber);

}
