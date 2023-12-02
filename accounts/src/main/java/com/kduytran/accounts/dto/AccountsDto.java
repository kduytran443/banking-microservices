package com.kduytran.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountsDto {

    @Schema(
            description = "Account number of the Account", example = "1011011023"
    )
    @NotEmpty(message = "Account number cannot be null or empty")
    @Pattern(regexp = "(^\\w{10}$)", message = "Account number must be 10 digits")
    private Long accountNumber;

    @Schema(
            description = "Account Type of the Account", example = "saving"
    )
    @NotEmpty(message = "Account Type cannot be null or empty")
    private String accountType;

    @Schema(
            description = "Branch address of the Account", example = "123 Main Street, New York"
    )
    @NotEmpty(message = "Branch address cannot be null or empty")
    private String branchAddress;
}
