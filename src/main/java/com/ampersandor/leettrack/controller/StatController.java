package com.ampersandor.leettrack.controller;

import com.ampersandor.leettrack.model.Stat;
import com.ampersandor.leettrack.service.MemberService;
import com.ampersandor.leettrack.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatController {

    private StatService statService;
    private MemberService memberService;

    @Autowired
    public StatController(StatService statService, MemberService memberService) {
        this.statService = statService;
        this.memberService = memberService;
    }

    @GetMapping("/get/{memberId}")
    public ResponseEntity<List<Stat>> getCumulativeStats(
            @PathVariable("memberId") Long memberId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return memberService.findOne(memberId)
                .map(member -> {
                    List<Stat> stats = statService.getStats(member, from, to);
                    return ResponseEntity.ok(stats);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Stat>> getAll() {

        return ResponseEntity.ok(statService.getAll());
    }


}