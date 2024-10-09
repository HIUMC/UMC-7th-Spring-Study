package hello.hello_spring.controller;


import com.fasterxml.jackson.databind.annotation.JsonAppend;
import hello.hello_spring.domain.Member;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {
    private final MemberService memberService;

    //private final MemberService memberService = new MemberService(); -> 이렇게 할 필요가 없다! memberService 를 여기저기 서
    // 쓸건데, 굳이 계속 인스턴스 생성해서 뭐함? 그냥 컨테이너에 등록을 해버리자!


    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";

    }

    @PostMapping("/members/new")
    public String create (MemberForm form){
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";

    }
}
