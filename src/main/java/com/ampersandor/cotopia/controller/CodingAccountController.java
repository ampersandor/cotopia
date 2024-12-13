package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.dto.CodingAccountDTO;
import com.ampersandor.cotopia.entity.CodingAccount;
import com.ampersandor.cotopia.entity.User;
import com.ampersandor.cotopia.service.CodingAccountService;
import com.ampersandor.cotopia.service.UserService;
import com.ampersandor.cotopia.service.AuthService;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coding-accounts")
public class CodingAccountController {

    private final CodingAccountService codingAccountService;
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CodingAccountDTO.Response>> getCodingAccountByUserId(@PathVariable Long userId) {
        List<CodingAccount> codingAccounts = codingAccountService.findByUserId(userId);
        return ResponseEntity.ok(codingAccounts.stream().map(CodingAccountDTO.Response::from).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<CodingAccountDTO.Response> createCodingAccount(@RequestBody CodingAccountDTO.CreateRequest codingAccount, @RequestHeader("Authorization") String token) {
        Long userId = authService.getUserIdFromToken(token);
        User user = userService.findOne(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        CodingAccount createdCodingAccount = codingAccountService.save(codingAccount.toEntity(user));
        return ResponseEntity.ok(CodingAccountDTO.Response.from(createdCodingAccount));
    }

    @PutMapping("/{codingAccountId}")
    public ResponseEntity<CodingAccountDTO.Response> updateCodingAccount(@PathVariable Long codingAccountId, @RequestBody CodingAccountDTO.UpdateRequest codingAccount, @RequestHeader("Authorization") String token) {
        Long userId = authService.getUserIdFromToken(token);
        User user = userService.findOne(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        CodingAccount updatedCodingAccount = codingAccountService.update(codingAccount.toEntity(user));
        return ResponseEntity.ok(CodingAccountDTO.Response.from(updatedCodingAccount));
    }

    @DeleteMapping("/{codingAccountId}")
    public ResponseEntity<Void> deleteCodingAccount(@PathVariable Long codingAccountId) {
        codingAccountService.delete(codingAccountId);
        return ResponseEntity.noContent().build();
    }
}