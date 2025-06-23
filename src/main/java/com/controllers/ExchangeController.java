package com.controllers;

import com.dao.ExchangeRateDaoImpl;
import com.dto.ExchangeRateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.ExchangeRateService;
import com.services.ExchangeRateServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/exchange")
public class ExchangeController extends HttpServlet {
    private ExchangeRateService exchangeRateService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        ServletContext context = getServletContext();

        ExchangeRateDaoImpl exchangeRateDao = new ExchangeRateDaoImpl(context);
        exchangeRateService = new ExchangeRateServiceImpl(exchangeRateDao);
    }

    private Map<String, Object> createResponseData(BigDecimal amount, BigDecimal exchangedAmount, Object exchangeRate) {
        Map<String, Object> responseData = new HashMap<>();

        responseData.put("exchangeRate", exchangeRate);
        responseData.put("amount", amount);
        responseData.put("convertedAmount", exchangedAmount);

        return responseData;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String from = request.getParameter("from");
            String to = request.getParameter("to");
            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));

            if (from == null || to == null || amount == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters.");
                return;
            }

            var exchangeOpt = exchangeRateService.getExchangeRateByCodes(from, to);
            var exchangeReverseOpt = exchangeRateService.getExchangeRateByCodes(to, from);
            var exchangeUsdFromOpt = exchangeRateService.getExchangeRateByCodes("USD",from);
            var exchangeUsdToOpt = exchangeRateService.getExchangeRateByCodes("USD", to);

            if (exchangeOpt.isPresent()) {
                var exchangeRate = exchangeOpt.get();
                BigDecimal exchangedAmount = exchangeRate.getRate().multiply(amount);

                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getWriter(), createResponseData(amount, exchangedAmount, exchangeRate));
            } else if (exchangeReverseOpt.isPresent()) {
                var exchangeRate = exchangeReverseOpt.get();
                BigDecimal exchangedAmount = amount.divide(exchangeRate.getRate(), 10, RoundingMode.HALF_UP);

                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getWriter(), createResponseData(amount, exchangedAmount, exchangeRate));
            } else if (exchangeUsdFromOpt.isPresent() && exchangeUsdToOpt.isPresent()) {
                var exchangeRateFrom = exchangeUsdFromOpt.get();
                var exchangeRateTo = exchangeUsdToOpt.get();

                BigDecimal rate = exchangeRateTo.getRate().divide(exchangeRateFrom.getRate(), 10, RoundingMode.HALF_UP);
                BigDecimal exchangedAmount = amount.multiply(rate);

                ExchangeRateDto exchangeRate = new ExchangeRateDto();
                exchangeRate.setBaseCurrency(exchangeRateFrom.getTargetCurrency());
                exchangeRate.setTargetCurrency(exchangeRateTo.getTargetCurrency());
                exchangeRate.setRate(rate);

                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getWriter(), createResponseData(amount, exchangedAmount, exchangeRate));

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found.");
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't connect to the database.");
        }
    }
}