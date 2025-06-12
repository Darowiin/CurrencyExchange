package com.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeRateDto {
    Long id;

    CurrencyDto baseCurrency;
    CurrencyDto targetCurrency;
    BigDecimal rate;
}
