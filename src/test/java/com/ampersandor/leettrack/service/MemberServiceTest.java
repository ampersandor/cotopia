package com.ampersandor.leettrack.service;

import com.ampersandor.leettrack.model.Member;
import com.ampersandor.leettrack.repository.MemoryMemberRepository;
import com.ampersandor.leettrack.repository.MemoryStatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberServiceTest {
    MemberService memberService;
    MemoryMemberRepository memberRepository;
    StatService statFetchService;

    @BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        statFetchService = new StatServiceImpl(new MemoryStatRepository(), new StatDataFetcherImpl(new RestTemplate()));
        memberService = new MemberServiceImpl(memberRepository, statFetchService);
    }

    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void join(){
        //given
        Member member = new Member( "DongHunKim", "ampersandor");
        member.setId(1L);

        //when
        memberService.join(member);
        Member foundMember = memberService.findOne(1L).get();

        //then
        assertThat(member).isEqualTo(foundMember);
    }

    @Test
    void duplicated_join(){
        //given
        Member member1 = new Member("DongHunKim", "ampersandor");
        Member member2 = new Member("Hacker", "ampersandor");
        member1.setId(1L);
        member2.setId(2L);

        //when
        memberService.join(member1);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("Existing Leetcode Id.");
    }
}