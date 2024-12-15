package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.CodingAccount;
import com.ampersandor.cotopia.repository.CodingAccountRepository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public class CodingAccountServiceImpl implements CodingAccountService {

    private final CodingAccountRepository codingAccountRepository;

    public CodingAccountServiceImpl(CodingAccountRepository codingAccountRepository) {
        this.codingAccountRepository = codingAccountRepository;
    }

    @Override
    @Transactional
    public CodingAccount save(CodingAccount codingAccount) {
        return codingAccountRepository.save(codingAccount);
    }

    @Override
    @Transactional
    public void delete(Long codingAccountId) {
        codingAccountRepository.delete(codingAccountId);
    }

    @Override
    @Transactional
    public CodingAccount update(CodingAccount codingAccount) {
        return codingAccountRepository.update(codingAccount);
    }

    @Override
    public List<CodingAccount> findByUserId(Long userId) {
        return codingAccountRepository.findByUserId(userId);
    }

    @Override
    public List<CodingAccount> findAll() {
        return codingAccountRepository.findAll();
    }
}