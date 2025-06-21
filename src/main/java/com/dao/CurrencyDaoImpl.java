package com.dao;

import com.models.Currency;
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

public class CurrencyDaoImpl implements CurrencyDao {

    public CurrencyDaoImpl(ServletContext context) {
        DatabaseConnection.init(context);
    }

    private Optional<Currency> getCurrency(Currency currency, String sql) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("Code"));
                currency.setFullName(resultSet.getString("FullName"));
                currency.setSign(resultSet.getString("Sign"));
                return Optional.of(currency);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении валюты: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Currency> findById(Long id) {
        Currency currency = new Currency();
        String sql = "SELECT * FROM CURRENCIES WHERE id = " + id;
        return getCurrency(currency, sql);
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        Currency currency = new Currency();
        String sql = "SELECT * FROM CURRENCIES WHERE Code = '" + code + "'";
        return getCurrency(currency, sql);
    }

    @Override
    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        String sql = "SELECT * FROM CURRENCIES";
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("Code"));
                currency.setFullName(resultSet.getString("FullName"));
                currency.setSign(resultSet.getString("Sign"));
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка валют: " + e.getMessage(), e);
        }
    }

    @Override
    public void save(Currency currency) {
        String sql = "INSERT INTO CURRENCIES (Code, FullName, Sign) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении валюты: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Currency currency) {
        String sql = "UPDATE CURRENCIES SET Code=?, FullName=?, Sign=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.setLong(4, currency.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении валюты: " + e.getMessage(), e);
        }
    }
}