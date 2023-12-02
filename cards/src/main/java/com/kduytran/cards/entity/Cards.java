package com.kduytran.cards.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class Cards extends BaseEntity {

    @Id
    @GeneratedValue(generator = "native", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    private Long cardId;

    @Column
    private String mobileNumber;

    @Column
    private String cardNumber;

    @Column
    private String cardType;

    @Column
    private int totalLimit;

    @Column
    private int amountUsed;

    @Column
    private int availableAmount;

}
