package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class SingletonTest {


    @Test
    @DisplayName("스프링 없는 순수 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();
        //1.호출할때마다 객체 생성
        MemberService memberService1 = appConfig.memberService();
        //2.호출할 때마다 객체 생성
        MemberService memberService2 = appConfig.memberService();

        System.out.println("memberService1: " + memberService1);
        System.out.println("memberService2: " + memberService2);

        assertThat(memberService1).isNotSameAs(memberService2);
    }

    @Test
    @DisplayName("싱글톤 패턴 적용 객체 사용")
    void singletonServiceTest(){
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        System.out.println("singletonService1: " + singletonService1);
        System.out.println("singletonService2: " + singletonService2);

        assertThat(singletonService1).isSameAs(singletonService2);

    }

    @Test
    @DisplayName("스프링 컨테이너와 실글톤")
    void springContainer() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        //1.호출할때마다 객체 생성
        MemberService memberService1 = ac.getBean(MemberService.class);
        //2.호출할 때마다 객체 생성
        MemberService memberService2 = ac.getBean(MemberService.class);

        System.out.println("memberService1: " + memberService1);
        System.out.println("memberService2: " + memberService2);

        assertThat(memberService1).isSameAs(memberService2);
    }
}
