package com.ampersandor.leettrack.controller;

import com.ampersandor.leettrack.common.MyLogger;
import com.ampersandor.leettrack.model.Stat;
import com.ampersandor.leettrack.service.MemberService;
import com.ampersandor.leettrack.service.StatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/stats")
public class StatController {

    private final StatService statService;
    private final MemberService memberService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @Autowired
    public StatController(StatService statService, MemberService memberService, ObjectProvider<MyLogger> myLoggerProvider) {
        this.statService = statService;
        this.memberService = memberService;
        this.myLoggerProvider = myLoggerProvider;
    }

    @GetMapping("/get/{memberId}")
    public ResponseEntity<List<Stat>> getCumulativeStats(
            @PathVariable("memberId") Long memberId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());


        return memberService.findOne(memberId)
                .map(member -> {
                    List<Stat> stats = statService.getStats(member, from, to);
                    myLogger.log("getCumulativeStats finished");
                    return ResponseEntity.ok(stats);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Stat>> getAll(HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        List<Stat> res = statService.getAll();
        myLogger.log("getAll finished");

        return ResponseEntity.ok(res);
    }

    @PostMapping("/update")
    public ResponseEntity<List<Stat>> updateStat(
            @RequestBody List<Long> memberIds,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());

        List<Stat> updatedStats = memberIds.stream()
                .map(memberService::findOne)
                .flatMap(optionalMember -> optionalMember
                        .map(member -> {
                            statService.updateStat(member);  // Update stats for each member
                            myLogger.log("Stats updated for memberId: " + member.getId());
                            return statService.getStats(member).stream();  // Get updated stats
                        })
                        .orElseGet(() -> {
                            myLogger.log("Member not found for memberId: " + memberIds);
                            return Stream.<Stat>of();
                        })
                )
                .toList();

        if (updatedStats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(updatedStats);    }

}