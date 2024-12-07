package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Member;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class StatScheduler {

    private final StatService statService;
    private final MemberService memberService;

    public StatScheduler(StatService statService, MemberService memberService) {
        this.statService = statService;
        this.memberService = memberService;
    }

    @Scheduled(cron = "0 0 11 * * ?", zone = "Asia/Seoul")
    public void updateStat(){
        List<Member> members = memberService.findMembers();
        members.forEach(this.statService::updateStat);
    }

}
