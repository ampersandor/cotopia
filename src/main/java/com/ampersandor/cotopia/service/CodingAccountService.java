package com.ampersandor.cotopia.service;

import java.util.List;
import com.ampersandor.cotopia.entity.CodingAccount;

public interface CodingAccountService {
    CodingAccount save(CodingAccount codingAccount);
    void delete(Long codingAccountId);
    CodingAccount update(CodingAccount codingAccount);
    List<CodingAccount> findByUserId(Long userId);
    List<CodingAccount> findAll();
}