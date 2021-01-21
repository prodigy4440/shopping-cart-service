package com.fahdisa.scs.core;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class KeyService {

    private Key key;

    public KeyService() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public Key getKey() {
        return this.key;
    }
}
