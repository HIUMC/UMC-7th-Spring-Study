package hello.core.beandefinition;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanDefinitionTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    //ApplicationContext ac = new ~~~ 는 getBeanDefinition을 못하기 때문에 쓰지 않았다.
    //평소에 빈 데피니션을 꺼내 쓸 일 이 거의 없기 때문에 기능이 없음.
    //어노테이션을 통하면 팩토리 빈을 통해 정보가 등록된다. 정보가 직접적으로 드러나지 않는다.
    //반대로 이전에 쓰던 xml 방식에서는 빈에 대한 정보가 명확하게 드러난다.
    @Test
    @DisplayName("빈 설정 메타정보 확인")
    void findApplicationBean(){
        String[] beanDefinitionNames = ac. getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if(beanDefinition.getRole()==BeanDefinition.ROLE_APPLICATION){
                System.out.println("beanDefinitionName = "+ beanDefinitionName + " beanDefinition= " + beanDefinition);
            }
        }
    }
}
