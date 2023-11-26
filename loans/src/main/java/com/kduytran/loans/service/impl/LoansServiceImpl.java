package com.kduytran.loans.service.impl;

import com.kduytran.loans.dto.LoansDto;
import com.kduytran.loans.repository.LoansRepository;
import com.kduytran.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {

    private final LoansRepository loansRepository;

    /**
     * @param loansDto - LoansDto object
     */
    @Override
    public void createLoan(LoansDto loansDto) {

    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Loans details based on a given mobileNumber
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        return null;
    }

    /**
     * @param loansDto - LoansDto object
     * @return boolean indicating if the update of Loan Details is successful or not
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        return false;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Loan Details is successful or not
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        return false;
    }

}
