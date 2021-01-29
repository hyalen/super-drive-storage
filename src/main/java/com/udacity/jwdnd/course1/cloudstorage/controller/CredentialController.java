package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @GetMapping(path = "/decrypt-password", produces = "application/json")
    @ResponseBody
    public String decryptCredentialPassword(@RequestParam Integer credentialId) {
        Credential credential = credentialService.getCredentialById(credentialId);
        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());

        return decryptedPassword;
    }

    @PostMapping("/upload-credential")
    public String uploadCredential (Authentication auth, @ModelAttribute("credentialForm") Credential credential, Model model) {
        String errorMsg = null;
        Integer userId = userService.getUser(auth.getName()).getUserId();
        Credential storedCredential = credentialService.getCredentialById(credential.getCredentialId());

        if (storedCredential == null) {
            try {
                int credentialUpload = credentialService.insert(credential, userId);
                if (credentialUpload < 0) {
                    errorMsg = "There was an error uploading this credential.";
                }
            } catch(Exception e) {
                errorMsg = "There was an error uploading this credential.";
            }
        } else {
            int insertedNote = credentialService.updateCredential(credential);

            if (insertedNote < 0) {
                errorMsg = "There was an error uploading this credential.";
            }
        }

        if (errorMsg == null) {
            model.addAttribute("successOperation", true);
        } else {
            model.addAttribute("errorOperation", errorMsg);
            return "error";
        }

        return "result";
    }

    @GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable(name = "credentialId") Integer credentialId, Model model) {
        String errorMsg = null;
        Integer deletedCredential = credentialService.deleteCredential(credentialId);

        if (deletedCredential < 0) {
            errorMsg = "There was an error uploading this note.";
        }

        if (errorMsg == null) {
            model.addAttribute("successOperation", true);
        } else {
            model.addAttribute("errorOperation", errorMsg);
        }

        return "result";
    }
}
