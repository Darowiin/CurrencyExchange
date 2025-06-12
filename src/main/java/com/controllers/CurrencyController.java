package com.controllers;

import com.dao.CurrencyDao;
import com.dao.CurrencyDaoImpl;
import com.models.Currency;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrencyController extends HttpServlet {
    private CurrencyDaoImpl currencyDao;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        currencyDao = new CurrencyDaoImpl(context);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> currencies = currencyDao.getAll();

        req.setAttribute("currencies", currencies);
        req.getRequestDispatcher("/WEB-INF/views/currencies.jsp").forward(req, resp);
    }
}
