package com.ampersandor.cotopia.repository;

import jakarta.persistence.EntityManager;
import com.ampersandor.cotopia.entity.CodingAccount;        
import java.util.List;
public class JpaCodingAccountRepository implements CodingAccountRepository {

    private final EntityManager em;

    public JpaCodingAccountRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<CodingAccount> findByUserId(Long userId) {
        return em.createQuery("select c from CodingAccount c where c.userId = :userId", CodingAccount.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void save(CodingAccount codingAccount) {
        em.persist(codingAccount);
    }

    @Override
    public void delete(Long codingAccountId) {
        em.remove(em.find(CodingAccount.class, codingAccountId));
    }

    @Override
    public void update(CodingAccount codingAccount) {
        em.merge(codingAccount);
    }

    @Override
    public List<CodingAccount> findAll() {
        return em.createQuery("select c from CodingAccount c", CodingAccount.class).getResultList();
    }
}
