package com.kduytran.loans.controller;

import com.kduytran.loans.constants.LoansConstants;
import com.kduytran.loans.dto.ErrorResponseDto;
import com.kduytran.loans.dto.LoansDto;
import com.kduytran.loans.dto.ResponseDto;
import com.kduytran.loans.service.ILoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for Loans in bank"
)
public class LoansController {

    private final ILoansService iLoansService;

    @Operation(
            summary = "Create Loan REST API",
            description = "REST API to create new Loan inside the bank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_201,
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_500,
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam
                                                      @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                      String mobileNumber) {
        iLoansService.createLoan(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update Loan REST API",
            description = "REST API to update Loan inside the bank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_200,
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_417,
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_500,
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoan(@Valid @RequestBody LoansDto loansDto) {
        boolean isUpdated = iLoansService.updateLoan(loansDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Loan REST API",
            description = "REST API to delete Loan inside the bank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_200,
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_417,
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_500,
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoan(@RequestParam
                                                      @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                      String mobileNumber) {
        boolean isDeleted = iLoansService.deleteLoan(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(
            summary = "Fetch Loan Details REST API",
            description = "REST API to fetch Loan Details based on mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_200,
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_417,
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = LoansConstants.STATUS_500,
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
    })
    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoan(@RequestParam
                                                  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                  String mobileNumber) {
        LoansDto loansDto = iLoansService.fetchLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }

}
