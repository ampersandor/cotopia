package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Member;
import com.ampersandor.cotopia.repository.MemberRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final StatService statService;

    public MemberServiceImpl(MemberRepository memberRepository, StatService statService) {
        this.memberRepository = memberRepository;
        this.statService = statService;
    }

    @Transactional
    @Override
    public Long join(Member member) {
        this.validateDuplicatedMember(member);
        memberRepository.save(member);
        try{
            this.statService.updateStat(member);
        }
        catch (Exception e){
            memberRepository.delete(member);
            throw e;
        }
        return member.getId();
    }

    @Transactional
    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    @Override
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    private void validateDuplicatedMember(Member member){
        memberRepository.findByUsername(member.getUsername())
                .ifPresent(m -> {
                    throw new IllegalStateException("Existing Leetcode Id.");
                });
    }

}
