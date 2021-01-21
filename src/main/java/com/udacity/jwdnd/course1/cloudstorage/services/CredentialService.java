package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CredentialService {
    CredentialMapper credentialMapper;
    EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public Integer insert(Credential credential, Integer userId) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        Credential newCredential = new Credential(
            null,
            credential.getUrl(),
            credential.getUsername(),
            encodedKey,
            encryptedPassword,
            userId
        );

        return credentialMapper.insert(newCredential);
    }

    public Map<String, String> encryptPassword (String password) {
        Map<String, String> encryptedValues = new HashMap<>();

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        encryptedValues.put("encryptedPassword", encryptedPassword);
        encryptedValues.put("encodedKey", encodedKey);

        return encryptedValues;
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }

    public Integer updateCredential (Credential credential) {
        Map<String, String> encryptedValues = encryptPassword(credential.getPassword());
        credential.setKey(encryptedValues.get("encodedKey"));
        credential.setPassword(encryptedValues.get("encryptedPassword"));

        return credentialMapper.updateCredential(credential);
    }

    public List<Credential> getCredentials(Integer userId) {
        return credentialMapper.getCredentialsForUser(userId);
    }
}
