package com.kduytran.loans.controller;

import com.kduytran.loans.constants.LoansConstants;
import com.kduytran.loans.dto.ErrorResponseDto;
import com.kduytran.loans.dto.LoansContactInfoDto;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(
        name = "CRUD REST APIs for Loans in bank"
)
public class LoansController {

    private static final Logger logger = LoggerFactory.getLogger(LoansController.class);

    private final ILoansService iLoansService;
    private final Environment environment;

    private final LoansContactInfoDto contactInfoDto;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    public LoansController(final ILoansService iLoansService, final Environment environment, final LoansContactInfoDto contactInfoDto) {
        this.iLoansService = iLoansService;
        this.environment = environment;
        this.contactInfoDto = contactInfoDto;
    }

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
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestHeader("bank-correlation-id") String correlationId,
                                                     @RequestParam
                                                     @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                     String mobileNumber) {
        logger.debug("bank-correlation-id found: {}", correlationId);
        LoansDto loansDto = iLoansService.fetchLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }

    @Operation(
            summary = "Get build info",
            description = "REST API to get build info of Loans microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @Operation(
            summary = "Get Java version",
            description = "REST API to get Java version of Loans microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Get contact info",
            description = "REST API to get contact info of Loans microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/contact-info")
    public ResponseEntity<LoansContactInfoDto> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(contactInfoDto);
    }

}
