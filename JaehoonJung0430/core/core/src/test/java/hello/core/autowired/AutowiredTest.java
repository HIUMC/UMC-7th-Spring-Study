package hello.core.autowired;

import hello.core.member.Member;
import io.micrometer.common.lang.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class AutowiredTest {
    
    @Test
    void AutowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }
    
    static class TestBean {
        
        @Autowired(required = false)
        public void setNoBean1(Member neBean1) {
            System.out.println("neBean1 = " + neBean1);
        }

        @Autowired
        public void setNoBean2(@Nullable Member neBean2) {
            System.out.println("neBean2 = " + neBean2);
        }
        
        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3 = " + noBean3);
        }
    }
    
}
