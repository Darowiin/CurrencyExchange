package com.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dto.CurrencyDto;
import com.services.CurrencyService;
import com.services.CurrencyServiceImpl;
import com.dao.CurrencyDaoImpl;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesController extends HttpServlet {
    private CurrencyService currencyService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        ServletContext context = getServletContext();
        CurrencyDaoImpl currencyDao = new CurrencyDaoImpl(context);
        currencyService = new CurrencyServiceImpl(currencyDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<CurrencyDto> currencies = currencyService.getAllCurrencies();

            request.setAttribute("currencies", currencies);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), currencies);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't connect to the database");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String currencyName = request.getParameter("name");
            String currencyCode = request.getParameter("code");
            String currencySign = request.getParameter("sign");

            if (currencyCode != null && currencyName != null && currencySign != null) {
                CurrencyDto newCurrency = new CurrencyDto();
                newCurrency.setCode(currencyCode);
                newCurrency.setName(currencyName);
                newCurrency.setSign(currencySign);
                if (currencyService.getCurrencyByCode(currencyCode).isPresent()) {
                    response.sendError(HttpServletResponse.SC_CONFLICT, "Currency with this code already exists");
                    return;
                }
                currencyService.saveCurrency(newCurrency);

                response.setStatus(HttpServletResponse.SC_CREATED);
                objectMapper.writeValue(response.getWriter(), newCurrency);
            }
            else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Currency code, name and sign are required");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't connect to the database");
        }
    }
}
