package com.ampersandor.leettrack.controller;

import com.ampersandor.leettrack.model.Member;
import com.ampersandor.leettrack.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    // 연결
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/create/new")
    public ResponseEntity<String> create(@RequestBody MemberForm form){
        Member member = new Member(form.getName(), form.getUsername());
        try{
            memberService.join(member);
            return ResponseEntity.ok("Successfully Joined");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findMembers();
        return ResponseEntity.ok(members);
    }

}
