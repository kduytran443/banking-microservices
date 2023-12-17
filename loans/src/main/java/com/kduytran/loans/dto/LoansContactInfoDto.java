package com.kduytran.loans.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("loans")
public record LoansContactInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {}
