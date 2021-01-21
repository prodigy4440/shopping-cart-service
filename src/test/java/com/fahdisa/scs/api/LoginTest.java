package com.fahdisa.scs.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LoginTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializestoJson() throws  Exception{
        final Login login = new Login();
        login.setUsername("username");
        login.setPassword("secrete");
        assertTrue(MAPPER.writeValueAsString(login).contains("secret"));
    }

}