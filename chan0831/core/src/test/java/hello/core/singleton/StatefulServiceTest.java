package hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //userA 가 10000원 주문
        int a = statefulService1.order("userA", 10000);
        //userB 가 20000원 주문
        int b = statefulService2.order("userB", 20000);

        //ThreadA : userA 주문 금액 조회
        //int price =  statefulService1.();
        System.out.println("price = " + a);

        //Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig{

        @Bean
        public  StatefulService statefulService() {
            return new StatefulService();
        }
    }

}