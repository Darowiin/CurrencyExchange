package com.services;

import com.dto.ExchangeRateDto;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateService {
    List<ExchangeRateDto> getAllExchangeRates();

    Optional<ExchangeRateDto> getExchangeRateById(Long id);

    Optional<ExchangeRateDto> getExchangeRateByCodes(String baseCode, String targetCode);

    void saveExchangeRate(ExchangeRateDto exchangeRateDto);

    void updateExchangeRate(ExchangeRateDto exchangeRateDto);
}
