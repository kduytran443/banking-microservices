package com.kduytran.loans.service.impl;

import com.kduytran.loans.constants.LoansConstants;
import com.kduytran.loans.dto.LoansDto;
import com.kduytran.loans.entity.Loans;
import com.kduytran.loans.exception.LoanAlreadyExistsException;
import com.kduytran.loans.exception.ResourceNotFoundException;
import com.kduytran.loans.mapper.LoansMapper;
import com.kduytran.loans.repository.LoansRepository;
import com.kduytran.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {

    private final LoansRepository loansRepository;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> optionalLoans= loansRepository.findByMobileNumber(mobileNumber);
        if(optionalLoans.isPresent()){
            throw new LoanAlreadyExistsException(String.format("Loan already registered with given mobileNumber %s", mobileNumber));
        }
        loansRepository.save(createNewLoan(mobileNumber));
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Loans details based on a given mobileNumber
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loans", "MobileNumber", mobileNumber)
        );
        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    /**
     * @param loansDto - LoansDto object
     * @return boolean indicating if the update of Loan Details is successful or not
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = loansRepository.findByMobileNumber(loansDto.getMobileNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loans", "MobileNumber", loansDto.getLoanNumber())
        );
        loansRepository.save(LoansMapper.mapToLoans(loansDto, loans));
        return true;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Loan Details is successful or not
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loans", "MobileNumber", mobileNumber)
        );
        loansRepository.deleteById(loans.getLoanId());
        return true;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new loan details
     */
    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }
}
