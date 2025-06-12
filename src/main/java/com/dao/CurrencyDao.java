package com.dao;

import com.dto.CurrencyDto;
import com.models.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDao {

    Optional<Currency> findById(Long id);

    Optional<Currency> findByCode(String code);

    List<Currency> getAll();

    void save(Currency currency);

    void update(Currency currency);

    void delete(Long id);
}