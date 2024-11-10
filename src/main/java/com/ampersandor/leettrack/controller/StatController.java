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


}