package hello.hello_spring.service;

import hello.hello_spring.aop.TimeTraceAop;
import hello.hello_spring.repository.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/*./SpringConfig*/

@Configuration
public class SpringConfig{

    private final MemberRepository memberRepository;

    /**
     * spring data jpa가 알아서 SpringDataJpaMemberRepository를 스프링 빈에 등록해서 그냥 쓰면 됨
     */
    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository);
    }

    /*@Bean
    public MemberRepository memberRepository(){
        return new JpaMemberRepository(em);
    }*/

//    @Bean
//    public TimeTraceAop timeTraceAop(){
//        return new TimeTraceAop();
//    }
}
