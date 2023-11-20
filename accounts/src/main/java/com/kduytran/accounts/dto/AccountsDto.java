package com.kduytran.accounts.dto;

import lombok.Data;

@Data
public class AccountsDto {
    private Long accountNumber;
    private String mobileNumber;
    private String branchAddress;
    private String accountType;
}
