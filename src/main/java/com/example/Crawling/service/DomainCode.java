package com.example.Crawling.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DomainCode {
    FMKOREA("FMKOREA"),
    ILBE("ILBE");
    private final String domainName;
}
