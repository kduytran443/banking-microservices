package com.kduytran.accounts.controller;

import com.kduytran.accounts.dto.CustomerDto;
import com.kduytran.accounts.dto.ResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AccountsController {

    @PostMapping("")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.status()
    }

}
