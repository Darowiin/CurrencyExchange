package com.services;

import com.dao.CurrencyDao;
import com.dto.CurrencyDto;
import com.models.Currency;
import com.utils.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyDao currencyDao;

    public CurrencyServiceImpl(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    @Override
    public List<CurrencyDto> getAllCurrencies() {
        return currencyDao.getAll().stream()
                .map(Mapper::toCurrencyDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CurrencyDto> getCurrencyById(Long id) {
        return currencyDao.findById(id)
                .map(Mapper::toCurrencyDto);
    }

    @Override
    public Optional<CurrencyDto> getCurrencyByCode(String code) {
        return currencyDao.findByCode(code)
                .map(Mapper::toCurrencyDto);
    }

    @Override
    public void saveCurrency(CurrencyDto currencyDto) {
        Currency currency = Mapper.toCurrency(currencyDto);
        currencyDao.save(currency);
    }

    @Override
    public void updateCurrency(CurrencyDto currencyDto) {
        Currency currency = Mapper.toCurrency(currencyDto);
        currencyDao.update(currency);
    }
}
