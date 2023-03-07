package com.hig.hwangingyu.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hig.hwangingyu.domain.Member;
import com.hig.hwangingyu.service.MemberService;
import com.hig.hwangingyu.utils.HttpUtils;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Value("${captcha.secret}")
    private String secret; 
    
    public MemberController(MemberService memberService) 
    {
        this.memberService = memberService;

    }

    @GetMapping("fail") 
    @ResponseBody
    public void fail(){
      System.out.println("실패");
    }
    
    @PostMapping("member/register")
    public String register(Member memberForm,
    @RequestParam(value="cf-turnstile-response") String token,
    @Nullable @RequestParam(value="passwdcheck") String PasswordCheck,
    RedirectAttributes ra,
    Model model) {

        Map<String,String> att = new HashMap<>();
        att.put("secret",secret);
        att.put("response",token);
        String ret = "";
        try {
            ret = HttpUtils.HttpPost("https://challenges.cloudflare.com/turnstile/v0/siteverify", att);
        } catch( Exception e) {
            e.printStackTrace();
        }

        JsonParser json = JsonParserFactory.getJsonParser();
        Map<String,Object> parsed = json.parseMap(ret);
        String passwordRule = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        boolean registerReq = true;
        if(!(boolean)parsed.get("success")) {
            registerReq = false;
            ra.addFlashAttribute("error", "봇 검사 체크가 필요합니다.");
        } else if(PasswordCheck==null||!PasswordCheck.equals(memberForm.getPasswd())) {
            registerReq = false;
            ra.addFlashAttribute("error", "비밀번호 확인이 서로 같지 않습니다.");
        } else if(!PasswordCheck.matches(passwordRule)) {
            registerReq = false;
            ra.addFlashAttribute("error", "비밀번호는 숫자 영문 포함 8글자 이상입니다.");
        } else if(memberForm.getName()==""||memberForm.getPasswd()=="") {
            registerReq = false;
            ra.addFlashAttribute("error", "아이디 또는 비밀번호를 입력하지 않았습니다.");
        }
        
        if(registerReq) {
            
            Member member = new Member.Builder()
            .setName(memberForm.getName())
            .setPasswd(memberForm.getPasswd())
            .build();

            memberService.register(member);
        }

        return "redirect:/higlogin";
        

    }
    
    @GetMapping("login")
    public String login(@Nullable @RequestParam(value="passwordCheck") String error, Model model) {
        //model.addAttribute("error",error);
        return "higlogin.html";
    }


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
