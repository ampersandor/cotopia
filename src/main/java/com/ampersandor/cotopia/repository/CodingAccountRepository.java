package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.CodingAccount;
import java.util.List;
public interface CodingAccountRepository {
    List<CodingAccount> findByUserId(Long userId);
    CodingAccount save(CodingAccount codingAccount);
    void delete(Long codingAccountId);
    CodingAccount update(CodingAccount codingAccount);
    List<CodingAccount> findAll();
}
