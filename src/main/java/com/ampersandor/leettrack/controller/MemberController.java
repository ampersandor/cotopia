package com.ampersandor.leettrack.controller;

import com.ampersandor.leettrack.common.MyLogger;
import com.ampersandor.leettrack.model.Member;
import com.ampersandor.leettrack.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    // 연결
    @Autowired
    public MemberController(MemberService memberService, ObjectProvider<MyLogger> myLoggerProvider) {
        this.memberService = memberService;
        this.myLoggerProvider = myLoggerProvider;
    }

    @PostMapping("/create/new")
    public ResponseEntity<String> create(@RequestBody MemberForm form, HttpServletRequest request){
        MyLogger myLogger = myLoggerProvider.getObject();
        Member member = new Member(form.getName(), form.getUsername());
        myLogger.setRequestURL(request.getRequestURI());
        try{
            memberService.join(member);
            myLogger.log("Member created successfully: " + member.getUsername());
            return ResponseEntity.ok("Successfully Joined");
        } catch (IllegalStateException e) {
            myLogger.log("Failed to create member: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch(Exception e){
            myLogger.log("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Member>> getAllMembers(HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());

        List<Member> members = memberService.findMembers();
        myLogger.log("Members fetched successfully: " + members.size() + " number of members");
        return ResponseEntity.ok(members);
    }

}
