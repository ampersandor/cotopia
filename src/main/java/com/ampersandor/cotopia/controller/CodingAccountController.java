package com.ampersandor.cotopia.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ampersandor.cotopia.entity.CodingAccount;
import com.ampersandor.cotopia.service.CodingAccountService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coding-accounts")
public class CodingAccountController {

    private final CodingAccountService codingAccountService;

    @Autowired
    public CodingAccountController(CodingAccountService codingAccountService) {
        this.codingAccountService = codingAccountService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CodingAccount>> getCodingAccountByUserId(@PathVariable Long userId) {
        List<CodingAccount> codingAccounts = codingAccountService.findByUserId(userId);
        return ResponseEntity.ok(codingAccounts);
    }

    @PostMapping
    public ResponseEntity<CodingAccount> createCodingAccount(@RequestBody CodingAccount codingAccount) {
        codingAccountService.save(codingAccount);
        return ResponseEntity.ok(codingAccount);
    }

    @PutMapping("/{codingAccountId}")
    public ResponseEntity<CodingAccount> updateCodingAccount(@PathVariable Long codingAccountId, @RequestBody CodingAccount codingAccount) {
        codingAccountService.update(codingAccount);
        return ResponseEntity.ok(codingAccount);
    }

    @DeleteMapping("/{codingAccountId}")
    public ResponseEntity<Void> deleteCodingAccount(@PathVariable Long codingAccountId) {
        codingAccountService.delete(codingAccountId);
        return ResponseEntity.noContent().build();
    }
}