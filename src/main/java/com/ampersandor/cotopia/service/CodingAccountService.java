package com.ampersandor.cotopia.service;

import java.util.List;
import com.ampersandor.cotopia.entity.CodingAccount;

public interface CodingAccountService {
    void save(CodingAccount codingAccount);
    void delete(CodingAccount codingAccount);
    void update(CodingAccount codingAccount);
    CodingAccount findByUserId(Long userId);
    List<CodingAccount> findAll();
}
