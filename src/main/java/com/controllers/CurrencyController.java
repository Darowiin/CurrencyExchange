package com.controllers;

import com.dao.CurrencyDaoImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.CurrencyService;
import com.services.CurrencyServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {
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
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String currencyCode = request.getPathInfo() != null ? request.getPathInfo().substring(1) : null;

            if (currencyCode != null) {
                var currency = currencyService.getCurrencyByCode(currencyCode);
                if (currency.isPresent()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    objectMapper.writeValue(response.getWriter(), currency.get());
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Currency code is required");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't connect to the database");
        }
    }
}
