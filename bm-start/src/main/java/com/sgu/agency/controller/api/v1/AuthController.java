package com.sgu.agency.controller.api.v1;

import com.sgu.agency.configuration.security.jwt.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    JwtProvider jwtProvider;


    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController() {
    }
}

