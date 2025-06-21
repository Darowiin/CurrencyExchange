package com.dao;

import com.models.Currency;
import com.models.ExchangeRate;
import com.utils.DatabaseConnection;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoImpl implements ExchangeRateDao {

    private final CurrencyDao currencyDao;

    public ExchangeRateDaoImpl(ServletContext context) {
        DatabaseConnection.init(context);
        this.currencyDao = new CurrencyDaoImpl(context);
    }

    private Optional<ExchangeRate> getExchangeRate(ExchangeRate exchangeRate, String sql) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                exchangeRate.setId(resultSet.getLong("id"));

                Long baseCurrencyId = resultSet.getLong("BaseCurrencyId");
                Long targetCurrencyId = resultSet.getLong("TargetCurrencyId");

                Optional<Currency> baseCurrency = currencyDao.findById(baseCurrencyId);
                Optional<Currency> targetCurrency = currencyDao.findById(targetCurrencyId);

                baseCurrency.ifPresent(exchangeRate::setBaseCurrency);
                targetCurrency.ifPresent(exchangeRate::setTargetCurrency);

                exchangeRate.setRate(resultSet.getBigDecimal("Rate"));
                return Optional.of(exchangeRate);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении курса валют: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
        ExchangeRate exchangeRate = new ExchangeRate();
        String sql = "SELECT * FROM EXCHANGERATES WHERE id = " + id;
        return getExchangeRate(exchangeRate, sql);
    }

    @Override
    public Optional<ExchangeRate> findByCodes(String baseCode, String targetCode) {
        Optional<Currency> baseCurrency = currencyDao.findByCode(baseCode);
        Optional<Currency> targetCurrency = currencyDao.findByCode(targetCode);

        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRate exchangeRate = new ExchangeRate();
        String sql = "SELECT * FROM EXCHANGERATES WHERE BaseCurrencyId = " + baseCurrency.get().getId()
                + " AND TargetCurrencyId = " + targetCurrency.get().getId();
        return getExchangeRate(exchangeRate, sql);
    }

    @Override
    public List<ExchangeRate> getAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sql = "SELECT * FROM EXCHANGERATES";
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setId(resultSet.getLong("id"));

                Long baseCurrencyId = resultSet.getLong("BaseCurrencyId");
                Long targetCurrencyId = resultSet.getLong("TargetCurrencyId");

                // Получение объектов Currency из базы данных по их ID
                Optional<Currency> baseCurrency = currencyDao.findById(baseCurrencyId);
                Optional<Currency> targetCurrency = currencyDao.findById(targetCurrencyId);

                baseCurrency.ifPresent(exchangeRate::setBaseCurrency);
                targetCurrency.ifPresent(exchangeRate::setTargetCurrency);

                exchangeRate.setRate(resultSet.getBigDecimal("Rate"));
                exchangeRates.add(exchangeRate);
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка курсов валют: " + e.getMessage(), e);
        }
    }

    @Override
    public void save(ExchangeRate rate) {
        String sql = "INSERT INTO EXCHANGERATES (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, rate.getBaseCurrency().getId());
            statement.setLong(2, rate.getTargetCurrency().getId());
            statement.setBigDecimal(3, rate.getRate());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении курса валют: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(ExchangeRate rate) {
        String sql = "UPDATE EXCHANGERATES SET BaseCurrencyId = ?, TargetCurrencyId = ?, Rate = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, rate.getBaseCurrency().getId());
            statement.setLong(2, rate.getTargetCurrency().getId());
            statement.setBigDecimal(3, rate.getRate());
            statement.setLong(4, rate.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении курса валют: " + e.getMessage(), e);
        }
    }
}
