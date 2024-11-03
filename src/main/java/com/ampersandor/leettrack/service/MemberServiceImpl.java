package com.ampersandor.leettrack.service;

import com.ampersandor.leettrack.model.Member;
import com.ampersandor.leettrack.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final StatService statService;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, StatService statService) {
        this.memberRepository = memberRepository;
        this.statService = statService;
    }

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
        System.out.println("stat updated");
        return member.getId();
    }

    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

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
