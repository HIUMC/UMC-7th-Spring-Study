package hello.hello_spring;

import hello.hello_spring.aop.TimeTraceAop;
import hello.hello_spring.repository.JdbcMemberRepository;
import hello.hello_spring.repository.JpaMemberRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import hello.hello_spring.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import javax.sql.DataSource;

//직접 스프링 빈으로 등록하는 방법!
@Configuration
public class SpringConfig {

    /*private final DataSource dataSource;
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    } -> jdcbmember레포 하면서 쓴거 */

   /* EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    } -> jpaMemberRepository 쓸때*/


    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);

    }

    /*@Bean
    public MemberRepository memberRepository() {
        //return new MemoryMemberRepository();
        //return new JdbcMemberRepository(dataSource);
        //return new JdbcTemplateMemberRepository(dataSource);
        //return new JpaMemberRepository(em );
    }*/

    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }

}
