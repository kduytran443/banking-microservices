package com.kduytran.loans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter @Setter @NoArgsConstructor
public class Loans extends BaseEntity {

    @Id
    @GeneratedValue(generator = "native", strategy = GenerationType.AUTO)
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
