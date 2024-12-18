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

import com.ampersandor.cotopia.common.MyLogger;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coding-accounts")
public class CodingAccountController {

    private final CodingAccountService codingAccountService;
    private final UserService userService;
    private final AuthService authService;
    private final MyLogger myLogger;
    @GetMapping("/{userId}")
    public ResponseEntity<List<CodingAccountDTO.Response>> getCodingAccountByUserId(@PathVariable(name = "userId") Long userId) {
        myLogger.log("getCodingAccountByUserId started");
        List<CodingAccount> codingAccounts = codingAccountService.findByUserId(userId);
        myLogger.log("getCodingAccountByUserId finished");
        return ResponseEntity.ok(codingAccounts.stream().map(CodingAccountDTO.Response::from).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<CodingAccountDTO.Response> createCodingAccount(
        @RequestBody CodingAccountDTO.CreateRequest codingAccount, 
        @CookieValue(name ="jwt") String token) {
        myLogger.log("createCodingAccount started");
        Long userId = authService.getUserIdFromToken(token);
        User user = userService.findOne(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        CodingAccount createdCodingAccount = codingAccountService.save(codingAccount.toEntity(user));
        myLogger.log("createCodingAccount finished");
        return ResponseEntity.ok(CodingAccountDTO.Response.from(createdCodingAccount));
    }

    @PutMapping("/{codingAccountId}")
    public ResponseEntity<CodingAccountDTO.Response> updateCodingAccount(
        @PathVariable(name = "codingAccountId") Long codingAccountId, 
        @RequestBody CodingAccountDTO.UpdateRequest codingAccount, 
        @CookieValue(name ="jwt") String token) {
        myLogger.log("updateCodingAccount started");
        Long userId = authService.getUserIdFromToken(token);
        User user = userService.findOne(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        CodingAccount updatedCodingAccount = codingAccountService.update(codingAccount.toEntity(user));
        myLogger.log("updateCodingAccount finished");
        return ResponseEntity.ok(CodingAccountDTO.Response.from(updatedCodingAccount));
    }

    @DeleteMapping("/{codingAccountId}")
    public ResponseEntity<Void> deleteCodingAccount(@PathVariable(name = "codingAccountId") Long codingAccountId) {
        myLogger.log("deleteCodingAccount started");
        codingAccountService.delete(codingAccountId);
        myLogger.log("deleteCodingAccount finished");
        return ResponseEntity.noContent().build();
    }
}