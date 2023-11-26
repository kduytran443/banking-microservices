package com.kduytran.loans.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter @Setter @NoArgsConstructor
public class Loans extends BaseEntity {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(strategy = "native", name = "native")
    private Long loanId;

    @Column
    private String mobileNumber;

    @Column
    private String loanNumber;

    @Column
    private String loanType;

    @Column
    private Integer totalLoan;

    @Column
    private Integer amountPaid;

    @Column
    private Integer outstandingAmount;

}
