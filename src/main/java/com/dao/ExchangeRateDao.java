package com.dao;

import com.dto.ExchangeRateDto;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateDao {
    Optional<ExchangeRateDto> findById(Long id);

    Optional<ExchangeRateDto> findByCodes(String baseCode, String targetCode);

    List<ExchangeRateDto> getAll();

    void save(ExchangeRateDto rate);

    void update(ExchangeRateDto rate);

    void delete(Long id);

}
