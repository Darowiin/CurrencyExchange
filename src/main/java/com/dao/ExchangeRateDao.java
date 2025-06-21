package com.dao;

import com.models.ExchangeRate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateDao {
    Optional<ExchangeRate> findById(Long id);

    Optional<ExchangeRate> findByCodes(String baseCode, String targetCode);

    List<ExchangeRate> getAll();

    void save(ExchangeRate rate);

    void update(ExchangeRate rate);
}
