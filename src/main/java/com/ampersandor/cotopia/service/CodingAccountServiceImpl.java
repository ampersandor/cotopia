package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.CodingAccount;
import com.ampersandor.cotopia.repository.CodingAccountRepository;

import java.util.List;

public class CodingAccountServiceImpl implements CodingAccountService {

    private final CodingAccountRepository codingAccountRepository;

    public CodingAccountServiceImpl(CodingAccountRepository codingAccountRepository) {
        this.codingAccountRepository = codingAccountRepository;
    }

    @Override
    public void save(CodingAccount codingAccount) {
        codingAccountRepository.save(codingAccount);
    }

    @Override
    public void delete(CodingAccount codingAccount) {
        codingAccountRepository.delete(codingAccount);
    }

    @Override
    public void update(CodingAccount codingAccount) {
        codingAccountRepository.update(codingAccount);
    }

    @Override
    public CodingAccount findByUserId(Long userId) {
        return codingAccountRepository.findByUserId(userId);
    }

    @Override
    public List<CodingAccount> findAll() {
        return codingAccountRepository.findAll();
    }
}