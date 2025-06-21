package com.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class AddParamsToHeader extends HttpServletRequestWrapper {

    public AddParamsToHeader(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        return (header != null) ? header : super.getParameter(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        names.addAll(Collections.list(super.getParameterNames()));
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        Enumeration<String> headers = super.getHeaders(name);
        if (headers != null && headers.hasMoreElements()) {
            return headers;
        }

        String param = getParameter(name);
        if (param != null) {
            return Collections.enumeration(Collections.singletonList(param));
        }

        return Collections.enumeration(Collections.emptyList());
    }
}
