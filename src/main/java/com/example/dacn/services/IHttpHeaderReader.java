package com.example.dacn.services;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface IHttpHeaderReader {
    String getTokenFromHeader(HttpServletRequest request);

}
