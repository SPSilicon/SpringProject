package com.hig.hwangingyu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import com.hig.hwangingyu.domain.Member;
import com.hig.hwangingyu.service.MemberService;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {this.memberService = memberService;}

    @GetMapping("fail") 
    @ResponseBody
    public void fail(){
      System.out.println("실패");
    }

    @GetMapping("login")
    public String login() {
        return "login.html";
    }
    /*
    @PostMapping("member/register")
    public String register(Member memberForm, Model model) {
        
        System.out.println(memberForm.toString());
        Member member = new Member.Builder()
                        .setName(memberForm.getName())
                        .setPasswd(memberForm.getPasswd())
                        .build();

        memberService.register(member);

        return "redirect:/signin";
        

    }
     */
    
    /*
    @PostMapping("member/login")
    public String login(Member memberForm, Model model) {

        Member member = new Member.Builder()
                        .setName(memberForm.getName())
                        .setPasswd(memberForm.getPasswd())
                        .build();
        
        boolean success = memberService.login(member);
        
        
        if(success){
            return "redirect:/";
        }
        else{
            model.addAttribute("success", success);
            return "redirect:/login";
        }
    }
    */
    
}
