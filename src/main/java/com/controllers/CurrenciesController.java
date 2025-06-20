package com.controllers;

import com.dto.CurrencyDto;
import com.services.CurrencyService;
import com.services.CurrencyServiceImpl;
import com.dao.CurrencyDaoImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesController extends HttpServlet {
    private CurrencyService currencyService;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        CurrencyDaoImpl currencyDao = new CurrencyDaoImpl(context);
        currencyService = new CurrencyServiceImpl(currencyDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyDto> currencies = currencyService.getAllCurrencies();

        req.setAttribute("currencies", currencies);
        req.getRequestDispatcher("/WEB-INF/views/currencies.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyCode = req.getParameter("currencyCode");
        String currencyName = req.getParameter("currencyName");
        String currencySign = req.getParameter("currencySign");

        if (currencyCode != null && currencyName != null) {
            CurrencyDto newCurrency = new CurrencyDto();
            newCurrency.setCode(currencyCode);
            newCurrency.setFullName(currencyName);
            newCurrency.setSign(currencySign);
            currencyService.saveCurrency(newCurrency);
        }

        resp.sendRedirect(req.getContextPath() + "/currencies");
    }
}
