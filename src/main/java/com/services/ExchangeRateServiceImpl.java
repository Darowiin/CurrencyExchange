package com.services;

import com.dao.ExchangeRateDao;
import com.dto.ExchangeRateDto;
import com.models.ExchangeRate;
import com.utils.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeRateServiceImpl(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    @Override
    public List<ExchangeRateDto> getAllExchangeRates() {
        return exchangeRateDao.getAll().stream()
                .map(Mapper::toExchangeRateDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ExchangeRateDto> getExchangeRateById(Long id) {
        return exchangeRateDao.findById(id)
                .map(Mapper::toExchangeRateDto);
    }

    @Override
    public Optional<ExchangeRateDto> getExchangeRateByCodes(String baseCode, String targetCode) {
        return exchangeRateDao.findByCodes(baseCode, targetCode)
                .map(Mapper::toExchangeRateDto);
    }

    @Override
    public void saveExchangeRate(ExchangeRateDto exchangeRateDto) {
        ExchangeRate exchangeRate = Mapper.toExchangeRate(exchangeRateDto);
        exchangeRateDao.save(exchangeRate);
    }

    @Override
    public void updateExchangeRate(ExchangeRateDto exchangeRateDto) {
        ExchangeRate exchangeRate = Mapper.toExchangeRate(exchangeRateDto);
        exchangeRateDao.update(exchangeRate);
    }
}
