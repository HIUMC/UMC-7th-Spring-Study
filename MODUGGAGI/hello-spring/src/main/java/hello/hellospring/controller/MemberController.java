package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
//2

@Controller
public class MemberController {
    private final MemberService memberService;

    //하나만 생성하고 공용으로 사용하기 위해 new 로 생성하지 않는다.
    @Autowired //@Autowired는 DI를 해준다.
    public MemberController(MemberService memberService) {
        //MemberService는 @Service로 스프링빈에 등록, 구현체인 MemoryMemberRepository는 @Repository로 스프링빈에 등록
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String CreateForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new") //Post는 데이터를 폼에 넣어서 전달할때 사용 Get은 조회할 때 사용
    public String create(MemberForm form) {
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
