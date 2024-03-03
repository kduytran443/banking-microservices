package com.kduytran.accounts.controller;

import com.kduytran.accounts.constants.AccountsConstants;
import com.kduytran.accounts.dto.AccountsContactInfoDto;
import com.kduytran.accounts.dto.CustomerDto;
import com.kduytran.accounts.dto.ErrorResponseDto;
import com.kduytran.accounts.dto.ResponseDto;
import com.kduytran.accounts.service.IAccountsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@Tag(
        name = "CRUD REST APIs for Accounts in bank"
)
public class AccountsController {

    private final IAccountsService iAccountsService;

    @Value("${build.version}")
    private String buildVersion;

    private final Environment environment;
    private final AccountsContactInfoDto accountsContactInfoDto;
    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    @Autowired
    public AccountsController(final IAccountsService iAccountsService, final Environment environment, final AccountsContactInfoDto accountsContactInfoDto) {
        this.iAccountsService = iAccountsService;
        this.environment = environment;
        this.accountsContactInfoDto = accountsContactInfoDto;
    }

    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new Customer & Account inside the bank"
    )
    @ApiResponse(
            responseCode = AccountsConstants.STATUS_201,
            description = "HTTP Status CREATED"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        iAccountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Get Account Details REST API",
            description = "REST API to get Customer & Account details inside the bank"
    )
    @ApiResponse(
            responseCode = AccountsConstants.STATUS_200,
            description = "HTTP Status OK"
    )
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                           @Pattern(regexp = "(^\\w{10}$)", message = "Mobile number must be 10 digits")
                                                           String mobileNumber) {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @Operation(
            summary = "Update Account REST API",
            description = "REST API to update Customer & Account inside the bank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_200,
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_417,
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Account REST API",
            description = "REST API to delete Customer & Account inside the bank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_200,
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_417,
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                            @Pattern(regexp = "(^\\w{10}$)", message = "Mobile number must be 10 digits")
                                                            String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(
            summary = "Get build information",
            description = "REST API to get build info of Accounts microservices"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallBack")
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        logger.debug("getBuildInfo() method is invoked");
        throw new NullPointerException();
        // return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    public ResponseEntity<String> getBuildInfoFallBack(Throwable throwable) {
        logger.debug("getBuildInfoFallBack() method is invoked");
        final String DEFAULT_BUILD_INFO = "1.0";
        return ResponseEntity.status(HttpStatus.OK).body(DEFAULT_BUILD_INFO);
    }

    @Operation(
            summary = "Get Java version",
            description = "REST API to get Java version of Accounts microservices"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @RateLimiter(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
    }


    public ResponseEntity<String> getJavaVersionFallback(Throwable throwable) {
        return ResponseEntity.status(HttpStatus.OK).body("Java 17");
    }

    @Operation(
            summary = "Get contact info",
            description = "REST API to get contract info of Accounts microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(accountsContactInfoDto);
    }

}
