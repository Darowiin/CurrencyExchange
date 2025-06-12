package com.utils;

import com.dto.CurrencyDto;
import com.dto.ExchangeRateDto;
import com.models.Currency;
import com.models.ExchangeRate;

public class Mapper {

    public static CurrencyDto toCurrencyDto(Currency currency) {
        if (currency == null) {
            return null;
        }

        CurrencyDto dto = new CurrencyDto();
        dto.setId(currency.getId());
        dto.setCode(currency.getCode());
        dto.setFullName(currency.getFullName());
        dto.setSign(currency.getSign());

        return dto;
    }

    public static Currency toCurrency(CurrencyDto dto) {
        if (dto == null) {
            return null;
        }

        Currency currency = new Currency();
        currency.setId(dto.getId());
        currency.setCode(dto.getCode());
        currency.setFullName(dto.getFullName());
        currency.setSign(dto.getSign());

        return currency;
    }

    public static ExchangeRateDto toExchangeRateDto(ExchangeRate exchangeRate) {
        if (exchangeRate == null) {
            return null;
        }

        ExchangeRateDto dto = new ExchangeRateDto();
        dto.setId(exchangeRate.getId());
        dto.setBaseCurrency(toCurrencyDto(exchangeRate.getBaseCurrency()));
        dto.setTargetCurrency(toCurrencyDto(exchangeRate.getTargetCurrency()));
        dto.setRate(exchangeRate.getRate());

        return dto;
    }

    public static ExchangeRate toExchangeRate(ExchangeRateDto dto) {
        if (dto == null) {
            return null;
        }

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(dto.getId());
        exchangeRate.setBaseCurrency(toCurrency(dto.getBaseCurrency()));
        exchangeRate.setTargetCurrency(toCurrency(dto.getTargetCurrency()));
        exchangeRate.setRate(dto.getRate());

        return exchangeRate;
    }
}
