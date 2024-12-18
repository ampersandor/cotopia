package com.ampersandor.cotopia.repository;

import jakarta.persistence.EntityManager;
import com.ampersandor.cotopia.entity.CodingAccount;        
import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
@Repository
@RequiredArgsConstructor
public class JpaCodingAccountRepository implements CodingAccountRepository {

    private final EntityManager em;


    @Override
    public List<CodingAccount> findByUserId(Long userId) {
        return em.createQuery("select c from CodingAccount c where c.userId = :userId", CodingAccount.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public CodingAccount save(CodingAccount codingAccount) {
        em.persist(codingAccount);
        return codingAccount;
    }

    @Override
    public void delete(Long codingAccountId) {
        em.remove(em.find(CodingAccount.class, codingAccountId));
    }

    @Override
    public CodingAccount update(CodingAccount codingAccount) {
        em.merge(codingAccount);
        return codingAccount;
    }

    @Override
    public List<CodingAccount> findAll() {
        return em.createQuery("select c from CodingAccount c", CodingAccount.class).getResultList();
    }
}
