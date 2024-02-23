package com.kduytran.accounts.controller;

import com.kduytran.accounts.dto.CustomerDetailsDto;
import com.kduytran.accounts.service.ICustomersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@Tag(
        name = "CRUD REST APIs for Customer Details in bank"
)
public class CustomersController {

    private static final Logger logger = LoggerFactory.getLogger(CustomersController.class);

    private final ICustomersService iCustomersService;

    @Autowired
    public CustomersController(ICustomersService iCustomersService) {
        this.iCustomersService = iCustomersService;
    }

    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestHeader("bank-correlation-id") String correlationId,
            @RequestParam @Pattern(regexp = "(^\\w{10}$)",
            message = "Mobile number must be 10 digits") String mobileNumber) {
        logger.debug("bank-correlation-id found: {}", correlationId);

        CustomerDetailsDto customerDetailsDto = iCustomersService.fetchCustomerDetails(mobileNumber, correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(customerDetailsDto);
    }

}
