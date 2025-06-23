package com.controllers;

import com.dao.CurrencyDaoImpl;
import com.dao.ExchangeRateDaoImpl;
import com.dto.ExchangeRateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.CurrencyServiceImpl;
import com.services.ExchangeRateService;
import com.services.ExchangeRateServiceImpl;
import com.services.CurrencyService;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {
    private ExchangeRateService exchangeRateService;
    private CurrencyService currencyService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        ServletContext context = getServletContext();

        ExchangeRateDaoImpl exchangeRateDao = new ExchangeRateDaoImpl(context);
        CurrencyDaoImpl currencyDao = new CurrencyDaoImpl(context);

        exchangeRateService = new ExchangeRateServiceImpl(exchangeRateDao);
        currencyService = new CurrencyServiceImpl(currencyDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<ExchangeRateDto> exchangeRates = exchangeRateService.getAllExchangeRates();

            request.setAttribute("exchangeRates", exchangeRates);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), exchangeRates);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't connect to the database");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String baseCurrencyCode = request.getParameter("baseCurrencyCode");
            String targetCurrencyCode = request.getParameter("targetCurrencyCode");
            String rate = request.getParameter("rate");

            if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
                return;
            }

            var baseCurrencyOpt = currencyService.getCurrencyByCode(baseCurrencyCode);
            var targetCurrencyOpt = currencyService.getCurrencyByCode(targetCurrencyCode);

            if (baseCurrencyOpt.isEmpty() || targetCurrencyOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "One or both currencies not found");
                return;
            }
            ExchangeRateDto newExchangeRate = new ExchangeRateDto();
            newExchangeRate.setBaseCurrency(baseCurrencyOpt.get());
            newExchangeRate.setTargetCurrency(targetCurrencyOpt.get());
            newExchangeRate.setRate(BigDecimal.valueOf(Double.parseDouble(rate)));

            Optional<ExchangeRateDto> existingRate = exchangeRateService.getExchangeRateByCodes(
                    baseCurrencyCode, targetCurrencyCode);

            if (existingRate.isPresent()) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "Exchange rate already exists");
                return;
            }

            exchangeRateService.saveExchangeRate(newExchangeRate);

            response.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(response.getWriter(), newExchangeRate);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't connect to the database");
        }
    }
}
