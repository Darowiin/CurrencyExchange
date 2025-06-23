package com.controllers;

import com.dao.ExchangeRateDaoImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.ExchangeRateService;
import com.services.ExchangeRateServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {
    private ExchangeRateService exchangeRateService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        ServletContext context = getServletContext();

        ExchangeRateDaoImpl exchangeRateDao = new ExchangeRateDaoImpl(context);
        exchangeRateService = new ExchangeRateServiceImpl(exchangeRateDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo().substring(1);
            String baseCurrencyCode = pathInfo.substring(0, 3);
            String targetCurrencyCode = pathInfo.substring(3);

            if (baseCurrencyCode == null || targetCurrencyCode == null || baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency codes");
                return;
            }
            var exchangeRate = exchangeRateService.getExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate.isPresent()) {
                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getWriter(), exchangeRate.get());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't connect to the database");
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(request.getParameter("rate")));

            String pathInfo = request.getPathInfo().substring(1);
            String baseCurrencyCode = pathInfo.substring(0, 3);
            String targetCurrencyCode = pathInfo.substring(3);

            if (baseCurrencyCode == null || targetCurrencyCode == null || baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency codes");
                return;
            }
            var exchangeRate = exchangeRateService.getExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate.isPresent()) {
                exchangeRate.get().setRate(rate);
                exchangeRateService.updateExchangeRate(exchangeRate.get());

                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getWriter(), exchangeRate.get());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't connect to the database");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        }
        this.doPatch(req, resp);
    }
}
