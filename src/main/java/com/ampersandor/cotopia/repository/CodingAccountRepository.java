package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.CodingAccount;
import java.util.List;
public interface CodingAccountRepository {
    CodingAccount findByUserId(Long userId);
    void save(CodingAccount codingAccount);
    void delete(CodingAccount codingAccount);
    void update(CodingAccount codingAccount);
    List<CodingAccount> findAll();
}
