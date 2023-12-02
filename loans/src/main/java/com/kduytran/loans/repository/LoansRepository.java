package com.kduytran.loans.repository;

import com.kduytran.loans.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoansRepository extends JpaRepository<Loans, Long> {
    Optional<Loans> findByMobileNumber(String MobileNumber);
    void deleteByMobileNumber(String MobileNumber);
}
