package com.services;

import com.dto.CurrencyDto;
import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    List<CurrencyDto> getAllCurrencies();

    Optional<CurrencyDto> getCurrencyById(Long id);

    Optional<CurrencyDto> getCurrencyByCode(String code);

    void saveCurrency(CurrencyDto currencyDto);

    void updateCurrency(CurrencyDto currencyDto);
}
