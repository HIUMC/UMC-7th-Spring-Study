package hello.core.Member;

import hello.core.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    MemberService memberService;
    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }
}

