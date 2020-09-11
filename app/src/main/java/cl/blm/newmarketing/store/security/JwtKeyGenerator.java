package cl.blm.newmarketing.store.security;

import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtKeyGenerator {
  static Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
}
