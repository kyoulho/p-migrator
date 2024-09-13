package io.playce.migrator.prepare;

public interface IScmCredentialService {
    String validateCredential(String url, String id, String pw);
    String validateCredential(String url, String prvKey);
}

