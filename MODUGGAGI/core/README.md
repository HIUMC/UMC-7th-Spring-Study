# 스프링 기본

## OCP와 DIP (AppConfig 이용한 생성자 주입)

- OCP : Open/Closed Principle, 코드는 확장에 대해서는 열려있어야 하지만 변경에 대해서는 닫혀있어야 한다. ⇒ 기존의 코드를 변경하지 않고 기능을 확장할 수 있다.
- DIP : Dependency Inversion Principle, 고수준 모듈은 저수준 모듈의 구현에 의존해서는 안 되며, 저수준 모듈이 고수준 모듈에서 정의한 추상 타입에 의존해야 한다는 원칙. ⇒ 시스템의 결합도를 낮추고 유연성과 확장성을 높이고 변경에 대한 영향 최소화.
    - 고수준 모듈 : 시스템의 추상적인 정책이나 프로세스를 정의하며, 시스템의 전체적인 행동을 결정
    - 저수준 모듈 : 고수준 모듈이 제공하는 기능을 구현하기 위해 필요한 구체적인 작업을 수행하는 모듈

```java
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
```

- 현재 위 코드는 인터페이스인 `DiscountPolicy`와 구체 클래스인 `RateDiscountPolicy` 에도 의존하고 있다.
- 따라서 새로운 `DiscountPolicy`를 적용하고 싶으면 코드를 수정해야 한다.
    - 생성자 추가.
    - 의존관계 주입을 해준다 - 생성자 주입
- 변경된 새로운 코드
    
    ```java
    public class OrderServiceImpl implements OrderService {
    		
        private final MemberRepository memberRepository;
        private final DiscountPolicy discountPolicy;
    
        public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
            this.memberRepository = memberRepository;
            this.discountPolicy = discountPolicy;
        }
    }
    ```
    
    ```java
    public class AppConfig {
        public MemberService memberService() {
            return new MemberServiceImpl(new MemoryMemberRepository());
        }
    
        public OrderService orderService() {
            return new OrderServiceImpl(new MemoryMemberRepository(), 
    														        new FixDiscountPolicy());
        }
    }
    ```
    
    - `AppConfig`가 외부에서 생성자 주입을 통해 의존관계 주입을 해준다.
    - 따라서 구현 클래스인 `OrderServiceImpl`은 코드내에 오로지 인터페이스(추상,`memberRepository`, `discountPolicy`)에만 의존을 하게 된다.
- 이를 통해 관심사의 분리가 되었다.
    - 객체를 생성하고 연결하는 역할 (`AppConfig`)와 실행하는 역할(`OrderServiceImpl`, `MemberServiceImpl`)
    
    ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled.png)
    
    ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%201.png)
    

```java
public class MemberApp {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();
//        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());
    }
}
```

- 코드를 실행할 때 우선적으로 `appConfig`를 생성해주고 그렇게 `appConfig`를 통해 `memberService`를 만든다.
    - 이렇게 되면 구현 클래스가 아닌 추상 클래스에만 의존을 하게 된다.
    - 기존에는 `MemberServiceImpl` 이라는 구현 클래스를 사용했기 때문에 DIP 위반

```java
public class OrderApp {

    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        OrderService orderService = appConfig.orderService();
        MemberService memberService = appConfig.memberService();
//        MemberService memberService = new MemberServiceImpl(null);
						// ㄴ MemberServiceImpl이라는 구현 클래스에 의존, DIP 위반
//        OrderService orderService = new OrderServiceImpl(null,null);
						// ㄴ OrderServiceImpl이라는 구현 클래스에 의존, DIP 위반

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order = " + order);
    }
}
```

- `OrderApp`에서도 `memberService`와 `orderService`가 필요하면 `appConfig`를 통해 가져온다.
- 테스트 코드 수정
    
    ```java
    public class OrderServiceTest {
    
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();
    }
    ```
    
    ```java
    public class OrderServiceTest {
    		
        MemberService memberService;
        OrderService orderService;
    
        @BeforeEach
        public void beforeEach() {
            AppConfig appConfig = new AppConfig();
            memberService = appConfig.memberService();
            orderService = appConfig.orderService();
        }
    }
    ```
    
    - `@BeforeEach`는 테스트코드 실행 전 먼저 실행되는 메소드

---

## AppConfig 리팩토링

- 역할에 따른 구현을 한눈에 알아볼 수 있게 한다.

```java
public class AppConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), 
														        new FixDiscountPolicy());
    }
}
```

```java
public class AppConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    private MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
    }
}
```

- 메소드 명을 보고 바로 역할을 알 수 있다.
    - 이제 `MemoryMemberRepository`를 다른 저장소로 대체할 때 한 부분(`memberRepository()` 메소드 내용)만 변경하면 된다.
    - 역할과 구현 클래스가 한눈에 들어와서 애플리케이션 전체 구성이 어떻게 되어있는지 빠르게 파악할 수 있다
    - 만약 할인 정책을 변경하고 싶으면 `FixDiscountPolicy()`를 `RateDiscountPolicy()`로만 변경해주면 코드의 나머지 부분을 수정하지 않아도 된다.

![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%202.png)

- 사용 영역의 코드에는 손대지 않고 구성 영역의 코드만 수정해서 할인 정책의 수정이 가능하다.

---

## IoC, DI, 컨테이너

- IoC, 제어의 역전 (Inversion of Control) : 프로그램의 제어 흐름을 직접 관리하는 것이 아닌 외부에서 관리하는 것.
    - 프로그램의 제어의 흐름을 `AppConfig`가 담당 구현 클래스들은 자신의 로직만을 수행한다.
        - ex) `OrderServiceImpl`은 필요한 인터페이스들을 호출하지만 어떤 구현 객체들이 실행될 지 모른다.
    - 프레임워크 vs 라이브러리
        - 프레임워크가 내가 작성한 코드를 제어하고, 대신 실행하면 그것은 프레임워크가 맞다. (JUnit)
            - ex) 테스트코드 작성 시 `@Test`를 붙이면 JUnit이 실행과 제어권을 가져간다. 프레임 워크가 적절한 타이밍에 호출해서 실행한다.
                
                내가 작성한 코드는 단순히 로직을 만들었을 뿐
                
        - 반면에 내가 작성한 코드가 직접 제어의 흐름을 담당한다면 그것은 프레임워크가 아니라 라이브러리다.
        
        ![image.png](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/image.png)
        
- DI, 의존관계 주입 (Dependency Injection) :
    - 정적인 클래스 의존관계 : 클래스가 사용하는 import 코드만 보고 의존관계를 쉽게 판단 가능
    컴파일 시점에 의존관계가 결정된다.
        
        ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%203.png)
        
        `OrderServiceImpl`은 `MemberRepository`와 `DiscountPolicy` 에 의존한다는 것을 알 수 있다.
        
        하지만 실제로 어떤 객체(구현 클래스)가 `OrderServiceImpl` 에 주입될 지는 알 수 없다.
        
    - 동적인 객체 의존관계 : 애플리케이션 실행 시점(런타임 시점)에 실제 생성된 객체 인스턴스의 참조가 연결되는 의존관계
        
        ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%204.png)
        
        - 애플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결 되는 것을 의존관계 주입(DI)이라 한다.
        - 객체 인스턴스를 생성하고 그 참조값을 전달해서 연결된다.
        - 의존관계 주입을 사용하면 클라이언트 코드를 변경하지 않고 클라이언트가 호출하는 대상의 타입 인스턴스를 변경할 수 있다.
        - 의존관계 주입을 사용하면 정적인 클래스 의존관계를 변경하지 않고, 동적인 객체 인스턴스 의존관계를 쉽게 변경할 수 있다.
        
        ![image.png](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/image%201.png)
        
- IoC 컨테이너, DI 컨테이너
    - `AppConfig` 처럼 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것을 IoC 컨테이너 또는 DI 컨테이너라 한다.
    - 최근에는 주로 DI 컨테이너라고 한다.

---

## 스프링 전환

- AppConfig 코드 변경
    
    ```java
    @Configuration
    public class AppConfig {
    
        @Bean
        public MemberService memberService() {
            return new MemberServiceImpl(memberRepository());
        }
    
        @Bean
        public MemberRepository memberRepository() {
            return new MemoryMemberRepository();
        }
    
        @Bean
        public OrderService orderService() {
            return new OrderServiceImpl(memberRepository(), discountPolicy());
        }
    
        @Bean
        public DiscountPolicy discountPolicy() {
    //        return new FixDiscountPolicy();
            return new RateDiscountPolicy();
        }
    }
    ```
    
    - [반환되는 객체들이 스프링빈에 메소드 이름으로 등록이 된다.](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)(링크 사진 참고)
        1. `MemberServiceImpl` 객체가 `memberService` 로 스프링빈에 등록
            - [이때의 빈 타입은 `MemberService`](https://www.notion.so/Java-392379f9d233424092fc4ed317b68910?pvs=21) (다형적 참조 - 메소드 타입(`MemberService`)을 구현한 클래스가 반환되는 객체(`MemberServiceImpl`))
        2. `MemoryMemberRepository` 객체가 `memberRepository` 로 스프링빈에 등록
            - [이때의 빈 타입은 `MemberRepository`](https://www.notion.so/Java-392379f9d233424092fc4ed317b68910?pvs=21) (다형적 참조)
        3. `OrderServiceImpl` 객체가 `orderService` 로 스프링빈에 등록
            - [이때의 빈 타입은 `OrderService`](https://www.notion.so/Java-392379f9d233424092fc4ed317b68910?pvs=21) (다형적 참조)
        4. `RateDiscountPolicy` 객체가 `discountPolicy` 로 스프링빈에 등록
            - [이때의 빈 타입은 `DiscountPolicy`](https://www.notion.so/Java-392379f9d233424092fc4ed317b68910?pvs=21) (다형적 참조)
    
    ```java
    public class OrderApp {
    
        public static void main(String[] args) {
    //        AppConfig appConfig = new AppConfig();
    //        OrderService orderService = appConfig.orderService();
    //        MemberService memberService = appConfig.memberService();
    
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
            MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
            OrderService orderService = applicationContext.getBean("orderService", OrderService.class);
    
            Long memberId = 1L;
            Member member = new Member(memberId, "memberA", Grade.VIP);
            memberService.join(member);
    
            Order order = orderService.createOrder(memberId, "itemA", 10000);
    
            System.out.println("order = " + order);
        }
    }
    ```
    
    - `.getBean()` : 1번째 인자는 내가 찾을 메소드 이름, 2번째 인자는 반환 타입
    - `ApplicationContext`를 스프링 컨테이너라고 한다.
    - 스프링 컨테이너는 `@Configuration` 이 붙은 `AppConfig` 를 설정(구성) 정보로 사용한다.
    - 여기서 `@Bean` 이라 적힌 메소드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다.

---

## 스프링 컨테이너

```java
ApplicationContext applicationContext = 
        new AnnotationConfigApplicationContext(AppConfig.class)
```

- `ApplicationContext`를 스프링 컨테이너라고 한다.
- `ApplicationContext`는 인터페이스다.
- `AnnotationConfigApplicationContext` 는 `ApplicationContext`의 구현 클래스이다.
    - 이 구현체는 애노테이션 기반(`@Bean`, `@Configuration`)의 자바 설정 클래스로 만든 것.
- 스프링 컨테이너를 생성할 때는 구성정보를 지정해주어야 한다. ⇒ 여기서는 `AppConfig.class`
    - 이렇게 설정 정보를 통해 전달한 클래스는 스프링에서 자동으로 컴포넌트 스캔의 대상으로 지정이 된다.
- 스프링 컨테이너는 `@Bean` 이 붙은 메소드를 스프링 빈으로 등록한다.
    
    ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%205.png)
    
- 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입한다.
    - 자동 의존관계 주입

---

## 스프링 컨테이너 이름 등록방법

|  | 빈 이름 | 빈 타입 |
| --- | --- | --- |
| `@Bean` 메소드 | 메소드 이름(예: `rateDiscountPolicy`) | 메소드의 반환 타입 (예: `RateDiscountPolicy`) |
| `@Component` 클래스 | 첫글자를 소문자화한 클래스 이름 (예: `rateDiscountPolicy`) | 클래스 그 자체(예: `RateDiscountPolicy`) |
- `@Bean` :
    - `@Configuration` 애노테이션이 달린 클래스 내부의 메소드에 사용
- `@Component` :
    - 클래스를 바로 스프링 빈으로 등록하려고 할 때 사용
    - 스프링이 자동으로 클래스를 찾아서 빈으로 등록(`@ComponentScan`)

```java
@Component
public class RateDiscountPolicy implements DiscountPolicy { }
```

- 이 코드에서 등록되는 빈의 이름은 클래스 이름인 `rateDiscountPolicy` 이고 빈의 타입은 클래스 그 자체인 `RateDiscountPolicy`이다.
- `RateDiscountPolicy`는 `DiscountPolicy` 인터페이스를 구현하고 있으므로 `rateDiscountPolicy` 빈을 참조할 때 `DiscountPolicy` 인터페이스를 통해 참조하는 것이 가능하다.

```java
@Bean
public DiscountPolicy discountPolicy() {
    return new RateDiscountPolicy();
}
```

- 이 코드에서 등록되는 빈의 이름은 메소드의 이름인 `discountPolicy`이고 빈의 타입은 반환 타입인 `RateDiscountPolicy`이다.
- `RateDiscountPolicy`는 `DiscountPolicy` 인터페이스를 구현하고 있으므로 `rateDiscountPolicy` 빈을 참조할 때 `DiscountPolicy` 인터페이스를 통해 참조하는 것이 가능하다.

---

## 컨테이너에 등록된 모든 빈 조회

```java
AnnotationConfigApplicationContext ac 
			= new AnnotationConfigApplicationContext(AppConfig.class);
```

- `AppConfig`를 설정정보로 사용
- 모든 빈 출력하기

    ```java
    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name = " + beanDefinitionName + " object = " + bean);
        }
    }
    ```

    - `.getBeanDefinitionNames()` 메소드는 등록된 모든 빈의 이름을 문자열 배열로 반환한다.
    - 그렇게 조회한 빈들의 이름을 통해 `.getBean()` 메소드로 타입을 조회한다.

      `.getBean()` 메소드는 모든 타입의 빈을 반환할 수 있어야 하므로 반환 타입이 `Object`이다.

- 애플리케이션 빈 출력하기

    ```java
    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
    
                //ROLE_APPLICATION : 직접 등록한 애플리케이션 빈
                //ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + " object = " + bean);
            }
        }
    }
    ```

    - `.getBeanDefinition()` 메소드를 통해 빈의 이름으로 [그 빈의 메타정보(`BeanDefinition`)](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)을 조회
        - `.getBeanDefinition()` 메소드는 `AnnotationConfigApplicationContext` 클래스의 메소드 이기 때문에 그보다 부모 클래스인 `ApplicationContext` 타입으로 `ac` 생성 시 사용이 불가능하다.
    - `.getRole()` 을 통해 그 빈이 `ROLE_APPLICATION`(직접 등록한 애플리케이션 빈)인지, `ROLE_INFRASTRUCTURE`(스프링이 내부에서 사용하는 빈)인지 찾고 `ROLE_APPLICATION` 인 빈들만 출력한다.

---

## 스프링 빈 조회

- 스프링 컨테이너에서 스프링 빈을 찾는 가장 기본적인 조회 방법

    ```java
    AnnotationConfigApplicationContext ac 
    			= new AnnotationConfigApplicationContext(AppConfig.class);
    ```

    - AppConfig의 설정 정보 이용
- `ac.getBean(빈이름, 타입)`

    ```java
    MemberService memberService = 
    										ac.getBean("memberService", MemberService.class);
    assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    ```

    - [스프링에서는 `MemberServiceImpl` 객체가 `MemberService` 타입의 빈으로 등록된다.](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)
    - 따라서 조회할때는 인터페이스 타입(`MemberService.class`)으로 조회하고
    - 검증할 때에는 실제 등록된 구현체가 맞는지 구체 클래스 타입(`MemberServiceImpl.class`)으로 조회한다.
- `ac.getBean(타입)`

    ```java
    MemberService memberService = ac.getBean(MemberService.class);
    assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    ```

    - [동일](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)
- 조회할 때는 구체 타입으로도 조회 가능

    ```java
    MemberService memberService = 
    										ac.getBean("memberService", MemberServiceImpl.class);
    ```

    - [동일](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)
- 조회 대상 스프링 빈이 없으면 예외 발생
    - 특정 예외 발생하는지 확인하기 위해서는 `assertThrows` 사용

        ```java
        assertThrows(NoSuchBeanDefinitionException.class,
                        () -> ac.getBean("xxxx", MemberService.class));
        ```

        - `ac.getBean("xxxx", MemberService.class)` 을 실행할 때 `NoSuchBeanDefinitionException` 예외가 발생하는지를 확인한다.
        - `xxxx` 라는 이름의 빈이 존재하지 않으면 `NoSuchBeanDefinitionException` 이 발생하고 이 예외를 `assertThrows`가 잡아 테스트를 통과 시킨다.

---

## 스프링 빈 조회 - 동일한 타입이 둘 이상

- 같은 타입의 스프링 빈이 둘 이상이면 오류 발생 → 빈 이름을 지정

```java
@Configuration
static class SameBeanConfig {

    @Bean
    public MemberRepository memberRepository1() {
        return new MemoryMemberRepository();
    }

    @Bean
    public MemberRepository memberRepository2() {
        return new MemoryMemberRepository();
    }
}
```

```java
AnnotationConfigApplicationContext ac 
			= new AnnotationConfigApplicationContext(SameBeanConfig.class);
```

- SameBeanConfig를 설정 정보로 사용
- 타입으로 조회 시 같은 타입이 둘 이상 있으면 오류 발생

    ```java
    @Test
    @DisplayName("타입으로 조회 시 같은 타입이 둘 이상 있으면, 중복 오류가 발생한다.")
    void findBeanByTypeDuplicate() {
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(MemberRepository.class));
    }
    ```

    - 같은 타입(`MemberRepository.class`)이 둘 이상 있을 경우 `NoUniqueBeanDefinitionException` 오류 발생
    - `assertThrows`로 오류 처리
- 타입으로 조회 시 같은 타입이 둘 이상 있으면, 빈 이름을 지정하면 된다.

    ```java
    @Test
    @DisplayName("타입으로 조회 시 같은 타입이 둘 이상 있으면, 빈 이름을 지정하면 된다.")
    void findBeanByName() {
        MemberRepository memberRepository = 
    							    ac.getBean("memberRepository1", MemberRepository.class);
        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }
    ```

    - 빈 이름을 지정해서 조회해 내가 원하는 빈을 조회
- 특정 타입 모두 조회

    ```java
    @Test
    @DisplayName("특정 타입을 모두 조회하기")
    void findAllBeanByType() {
        Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
        System.out.println("beansOfType = " + beansOfType);
        assertThat(beansOfType.size()).isEqualTo(2);
    }
    ```

    - `.getBeansOfType()` 을 사용해서 해당 타입의 모든 빈을 조회 ⇒ `Map(<키,값>)` 에 담는다.
    - `beansOfType.get(key)` 에서 `.get()` 메소드는 키(빈 이름)를 이용해 매핑된 값을 찾는다.

---

## 스프링 빈 조회 - 상속관계

- 부모 타입으로 조회하면, 자식 타입도 함께 조회한다.
- 따라서 자바 객체의 최고 부모인 `Object` 타입으로 조회하면, 모든 스프링 빈을 조회한다.

  ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled.png)


```java
@Configuration
static class TestConfig {
    @Bean
    public DiscountPolicy rateDiscountPolicy() {
        return new RateDiscountPolicy();
    }

    @Bean
    public DiscountPolicy fixDiscountPolicy() {
        return new FixDiscountPolicy();
    }
}
```

```java
AnnotationConfigApplicationContext ac 
			= new AnnotationConfigApplicationContext(TestConfig.class);
```

- TestConfig를 설정 정보로 사용
- 부모 타입으로 조회 시, 자식 타입이 둘 이상 있으면, 오류 발생

    ```java
    @Test
    @DisplayName("부모 타입으로 조회 시, 자식이 둘 이상 있으면, 중복 오류가 발생한다.")
    void findBeanByParentTypeDuplicate() {
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(DiscountPolicy.class));
    }
    ```

    - 현재 `DiscountPolicy` 타입으로 등록된 빈이 2개(`RateDiscountPolicy`, `FixDiscountPolicy`)
    - `ac.getBean(DiscountPolicy.class)` 로 타입으로 조회하면 2개의 빈이 조회되어 오류 발생(`NoUniqueBeanDefinitionException`)
    - `assertThrows`로 예외 처리
- 부모 타입으로 조회 시, 자식이 둘 이상 있으면, 빈 이름을 지정하면 된다.

    ```java
    @Test
    @DisplayName("부모 타입으로 조회 시, 자식이 둘 이상 있으면, 빈 이름을 지정하면 된다.")
    void findBeanByParentTypeBeanName() {
        DiscountPolicy rateDiscountPolicy 
    					    = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }
    ```

    - 자식이 2개 이상 있는 경우도 이름을 지정해서 조회하면 오류가 발생하지 않는다.
    - [조회할때 타입은 `DiscountPolicy`로 조회하고 검증할때에는 실제 등록된 구현체가 맞는지 구체 클래스 타입(`RateDiscountPolicy`)으로 검증](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)
- 특정 하위 타입으로 조회

    ```java
    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType() {
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }
    ```

    - 구체 클래스 타입으로 조회 시 오류 없이 바로 조회 가능
- 부모 타입으로 모두 조회하기

    ```java
    @Test
    @DisplayName("부모 타입으로 모두 조회하기")
    void findAllBeanByParentType() {
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        assertThat(beansOfType.size()).isEqualTo(2);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
    }
    ```

    - `.getBeansOfType()` 은 특정 타입의 모든 빈을 조회할 때 사용
    - `.getBeansOfType()` 로 모든 빈 조회 후 `Map` 에 key는 빈의 이름, 값은 빈의 타입으로 저장
    - [`.get()` 메소드는 특정 키로 값을 조회할 때 사용](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)

    ```java
    @Test
    @DisplayName("부모 타입으로 모두 조회하기 - Object")
    void findAllBeanByObjectType() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
    }
    ```

    - `Object` 클래스는 자바 객체의 최고 부모 타입이기 때문에 모든 스프링 빈이 조회된다.

---

## BeanFactory와 ApplicationContext

- `BeanFactory`
    - 스프링 컨테이너의 최상위 인터페이스
        - 스프링 빈을 관리하고 조회하는 역할 - `.getBean()` 제공 및 대부분의 기능 제공
- `ApplicationContext`
    - `BeanFactory`의 기능을 모두 상속 받아서 제공
    - `ApplicationContext`는 `BeanFactory`뿐만 아니라 수많은 인터페이스를 상속받고 있다.

      ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%201.png)

- `BeanFactory`와 `ApplicationContext`를 스프링 컨테이너 라고 한다.
- 보통 부가 기능이 포함된 `ApplicationContext`를 사용

---

## XML 설정 (참고)

![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%202.png)

- `resources` 폴더에 `appConfig.xml` 파일 생성한다.

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="memberService" class="hello.core.member.MemberServiceImpl">
        <constructor-arg name="memberRepository" ref="memberRepository"/>
    </bean>

    <bean id="memberRepository" class="hello.core.member.MemoryMemberRepository"/>

    <bean id="orderService" class="hello.core.order.OrderServiceImpl">
        <constructor-arg name="memberRepository" ref="memberRepository"/>
        <constructor-arg name="discountPolicy" ref="discountPolicy"/>
    </bean>

    <bean id="discountPolicy" class="hello.core.discount.RateDiscountPolicy"/>

</beans>
```

- 이 형태는 [애노테이션 기반 자바 코드](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)로 설정한 형태와 유사하다.

```java
@Test
void xmlAppContext() {
    ApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");
    MemberService memberService = ac.getBean("memberService", MemberService.class);
    assertThat(memberService).isInstanceOf(MemberService.class);
}
```

- `AnnotationConfigApplicationContext`와 테스트 방식이 같다.

---

## 스프링 빈 설정 메타 정보 - BeanDefinition

- `BeanDefinition` 이라는 추상화(인터페이스)를 통해 역할과 구현을 나눈다.
    - XML을 읽어서 `BeanDefinition` 을 만든다.
    - 자바 코드를 읽어서 `BeanDefinition` 을 만든다. - 팩토리 메소드를 사용해서 등록
    - 스프링 컨테이너는 자바 코드인지 ,XML인지 몰라도 오직 `BeanDefinition` 만 알면 된다.
- `BeanDefinition` 을 빈 설정 메타 정보라고 한다.
    - `@Bean`, `<bean>` 당 각각 하나씩 메타 정보가 생성된다.
- 스프링 컨테이너는 이 메타정보를 기반으로 스프링 빈을 생성한다.

  ![BeanDefinition은 인터페이스이다.](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%203.png)

  BeanDefinition은 인터페이스이다.

- 지금까지 사용해온 `AnnotationConfigApplicationContext`는 `AnnotatedBeanDefinitionReader` 를 사용해서 `AppConfig.class` 를 읽고 `BeanDefinition`을 생성한다.
- XML 도 마찬가지…

⇒ 새로운 형식의 설정 정보가 추가되면 `XXXBeanDefinitionReader` 를 만들어서 `BeanDefinition`을 생성

![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%204.png)

```java
AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
//GenericXmlApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");

@Test
@DisplayName("빈 설정 메타정보 확인")
void findApplicationBean() {
    String[] beanDefinitionNames = ac.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
        BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

        if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
            System.out.println("beanDefinitionName = " + beanDefinitionName + " beanDefinition = " + beanDefinition);
        }
    }
}
```

결과

![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%205.png)

`AnnotationConfigApplicationContext`로 스프링 빈을 등록할 때에는 팩토리 빈을 통해 등록된다.

![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%206.png)

이처럼 팩토리 빈 이름을 통해 실제 팩토리 메소드 이름을 통해 생성된다. ?????

- 스프링은 다양한 형태의 설정 정보를 `BeanDefinition`으로 추상화해서 사용한다.

---

## 싱글톤

- 스프링 없는 순수한 DI 컨테이너인 AppConfig

    ```java
    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();
        //1. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();
    
        //2. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();
    
        //참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);
    
        //memberService1 != memberService2
        Assertions.assertThat(memberService1).isNotSameAs(memberService2);
    }
    ```

  ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%207.png)

    - `memberService1` 과 `memberService2` 는 서로 다른 참조값을 가진 다른 객체이다.
    - `assertThat`으로 확인

      ⇒ 참조값이 다름을 확인할 때에는 `.isNotSameAs()` 사용, Java의 `!=` 연산자와 유사

      ⇒ 객체의 값이 다름을 확인할 때에는 `.isNotEqualTo()` 사용, java의 [`.equals()`](https://www.notion.so/Java-392379f9d233424092fc4ed317b68910?pvs=21) 와 유사

- 싱글톤 패턴 (별로 좋은 방법 아님)
    - 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴
        - 객체 인스턴스를 2개 이상 생성하지 못하게 해야한다. ⇒ private 생성자로 생성을 막는다.

    ```java
    public class SingletonService {
    
        private static final SingletonService instance = new SingletonService();
    
        public static SingletonService getInstance() {
            return instance;
        }
    
        private SingletonService() {
        }
    }
    ```

    - static에 객체 인스턴스를 1개 생성 ⇒ 필요시에는 오직 `getInstance()` 메소드 통해서만 조회 가능, static이기 때문에 항상 같은 인스턴스 반환
    - 생성자를 private으로 설정해 외부에서 new 키워드로 객체 인스턴스 생성하는것을 막는다.

    ```java
    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonServiceTest() {
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();
    
        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);
    
        assertThat(singletonService1).isSameAs(singletonService2);
    }
    ```

  ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%208.png)

    - 서로 동일한 객체임을 확인 가능하다.
- 싱글톤 컨테이너(스프링 컨테이너)
    - 스프링 컨테이너는 자동으로 객체 인스턴스를 싱글톤으로 관리한다.
        - [컨테이너는 객체를 하나만 생성해서 관리한다.](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)
        - Cf) 요청할 때마다 새로운 객체를 생성해서 반환하는 기능도 제공.
    - 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라고 한다.

    ```java
    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {
        ApplicationContext ac 
    				    = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);
    
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);
    
        assertThat(memberService1).isSameAs(memberService2);
    }
    ```

  ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%209.png)

    - [`AppConfig`](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)에 싱글톤 패턴과 관련된 코드가 들어있지 않아도 싱글톤으로 관리된다.

---

## 싱글톤 - 주의점

- 싱글톤 방식은 객체 인스턴스를 하나만 생성해서 공유하는 방식이기 때문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안된다.
- 무상태(stateless)로 설계해야 한다.
    - 특정 클라이언트에 의존적인 필드가 있으면 안된다.
    - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
    - 되도록 읽기만 가능해야 한다.
    - 필드 대신에 자바에서 공유되지 않는 지역변수, 파라미터, ThreadLocal을 사용해야 한다.

```java
public class StatefulService {

    private int price; //상태를 유지하는 필드
    
		public void order(String name, int price) {
				System.out.println("name = " + name + " price = " + price);
				this.price = price; //여기가 문제!
		}
		
		public int getPrice() {
				return price;
		}
}
```

```java
@Test
void statefulServiceSingleton() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
    StatefulService statefulService1 = ac.getBean(StatefulService.class);
    StatefulService statefulService2 = ac.getBean(StatefulService.class);

    //ThreadA : A사용자 10000원 주문
    int userAPrice = statefulService1.order("userA", 10000);
    //ThreadB : B사용자 20000원 주문
    int userBPrice = statefulService1.order("userB", 20000);

    //ThreadA: A사용자가 주문 금액 조회
    int price = statefulService1.getPrice();
    System.out.println("price = " + userAPrice);
}

static class TestConfig {

    @Bean
    public StatefulService statefulService() {
        return new StatefulService();
    }
}
```

- 이 경우 `price` 의 값이 `ThreadA`에서 10,000으로 저장이 되었다가 `ThreadB`에서 20,000으로 값이 덮어씌워진다 ⇒ 싱글톤 방식으로 객체를 1개만 생성하기 때문.
- 그래서 `ThreadA`에서 사용자가 주문 금액을 조회 시 10,000이 아닌 20,000이 조회된다.

---

## @Configuration

- [`AppConfig`](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21) 의 코드를 보면
    - `memberService()` 메소드가 `memberRepository()`를 호출하고 `new`를 통해 `MemoryMemberRepository()` 를 호출하고
    - `memberRepository()` 메소드는 자기자신 호출 시 `new MemoryMemberRepository()`를 호출하고
    - `orderService()` 메소드도 `memberRepository()`를 호출하고 `new`를 통해 `MemoryMemberRepository()` 를 호출한다.
    - 이렇게 호출을 할 경우 계속해서 새로운 `MemoryMemberRepository()` 가 호출이 되면서 싱글톤이 깨지는 것 처럼 보인다.
- 하지만 실제로 호출을 해보고 `memberService()`의 `MemoryMemberRepository`와 `orderService()`의 `MemoryMemberRepository`, `memberRepository`의 `MemoryMemberRepository`의 참조값을 비교해보면 전부 같은 `memberRepository`의 객체 인스턴스, `MemoryMemberRepository`인 것을 알 수 있다.

  또한 `memberRepository()` 메소드는 3번 호출 되는 것이 아닌 1번만 호출되는 것도 알 수 있다.

  ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%2010.png)

- 이러한 것이 가능한 이유는 [`AppConfig`](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)의 `@Configuration` 때문이다.
    - 스프링 컨테이너는 스프링 빈이 싱글톤이 보장되도록 해주어야 한다.
    - 이를 위해 스프링은 클래스의 바이트코드를 조작하는 라이브러리를 사용한다.

    ```java
    @Test
    void configurationDeep() {
        ApplicationContext ac 
    			    = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);
    
        System.out.println("bean = " + bean.getClass());
    }
    ```

    - `AnnotationConfigApplicationContext` 에 파라미터로 넘긴 값은 스프링 빈으로 등록된다. 그래서 `AppConfig` 도 스프링 빈이 된다.
    - 이때 등록된 스프링 빈, `AppConfig` 를 조회해보면 다음과 같다.

      ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%2011.png)

    - 클래스명에 SpringCGLIB이 붙으며 순수 클래스가 아닌 것을 알 수 있다.
    - 스프링은 `@Configuration`이 붙은 클래스는 `CGLIB`라는 바이트코드 조작 라이브러리를 사용해서 `AppConfig` 클래스를 상속받은 임의의 다른 클래스(`AppConfig@CGLIB`)를 만들고, 그 다른 클래스를 스프링 빈으로 등록하게 된다.

      ![AppConfig@CGLIB은 AppConfig의 자식 타입](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%2012.png)

      AppConfig@CGLIB은 AppConfig의 자식 타입

    - 그리고 이렇게 등록된 `AppConfig@CGLIB` 이 싱글톤이 보장되도록 한다.

        ```java
        @Bean
        public MemberRepository memberRepository() {
        
        		if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) {
        				return 스프링 컨테이너에서 찾아서 반환;
        		} else { //스프링 컨테이너에 없으면 
        				기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록
        				return 반환
        		}
        }
        ```

        - `@Bean`이 붙은 메소드마다 이미 스프링 빈이 존재하면 해당 스프링 빈을 스프링 컨테이너에서 찾아서 반환하고, 그렇지 않다면 새로 생성해서 스프링 빈을 등록한다.
        - 이를 통해 싱글톤이 보장된다.
- `@Bean`만 사용해도 스프링 빈으로 등록은 되지만 싱글톤은 보장되지 않는다.

  ⇒ 따라서 스프링 설정정보는 항상 `@Configuration` 으로 등록해야 한다.


---

## 컴포넌트 스캔(@ConponentScan)

- 자동으로 스프링 빈을 등록하는 방법
- 의존관계 주입은 `@Autowired`를 통해 가능

```java
@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
                                                    classes = Configuration.class)
)
public class AutoAppConfig {

}
```

- 컴포넌트 스캔은 `@Component` 애노테이션이 붙은 클래스를 자동으로 스캔해서 스프링 빈으로 등록한다.
- 그래서 우리가 등록할 각 클래스 위에 `@Component` 를 붙여주어야 한다.

    ```java
    @Component
    public class RateDiscountPolicy implements DiscountPolicy {}
    ```

    ```java
    @Component
    public class MemoryMemberRepository implements MemberRepository {}
    ```

    ```java
    @Component
    public class MemberServiceImpl implements MemberService {
    
        private final MemberRepository memberRepository;
    
        @Autowired //ac.getBean(MemberRepository.class)
        public MemberServiceImpl(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }
    }
    ```

    ```java
    @Component
    public class OrderServiceImpl implements OrderService {
    
        private final MemberRepository memberRepository;
        private final DiscountPolicy discountPolicy;
    
        @Autowired
        public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
            this.memberRepository = memberRepository;
            this.discountPolicy = discountPolicy;
        }
    ]
    ```

  ![Untitled](%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%20%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%8B%E1%85%B3%E1%86%B7%201168b8b35dba802fb721c0c2aa3d0da1/Untitled%2013.png)

    - `@ComponentScan`은 `@Component`가 붙은 모든 클래스를 스프링 빈으로 등록한다.
    - 등록한 스프링 빈은 `@Configuration`으로 등록할 때와 다르게 빈 이름이 클래스 이름을 사용하고 앞글자만 소문자로해서 등록이 된다.
        - 빈 이름을 직접 지정하고 싶으면 `@Component("memberService2")` 와 같이 사용
    - [위 코드들](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)과 같이 생성자에 `@Autowired`를 지정하면 스프링 컨테이너가 자동으로 해당하는 스프링 빈을 찾아서 의존관계 주입을 해준다.
        - 기본 조회 전략은 타입이 같은 빈을 찾아서 주입
        - `ac.getBean(MemberRepository.class)`와 같다고 보면 된다.
        - 생성자에 [파라미터가 많은 경우](https://www.notion.so/88f187286cf0473f9c2de718baa950c1?pvs=21)에도 가능하다.

---

## 컴포넌트 스캔의 탐색 위치

```java
@ComponentScan(
        basePackages = "hello.core",
}
```

- `basePackages`를 이용해 탐색할 패키지의 시작 위치를 지정할 수 있다.
    - `basePackages = {"hello.core", "hello.service"}` ⇒ 여러 탐색 위치 지정도 가능
    - `basePackageClasses` : 지정한 클래스의 패키지를 탐색 시작 위치로 지정한다.
        - ex) `basePackagesClasses = AutoAppConfig.class`
- 지정하지 않으면 `@ComponentScan` 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다.
- 패키지 위치를 지정하지 않고 설정 정보 클래스의 위치를 프로젝트 최상단에 두기
- 스프링 부트를 사용하면 스프링 부트의 대표 시작정보인 `@SpringbootApplication` 를 이 프로젝트 시작 루트 위치에 두는 것이 관례이고 이 설정 안에 `@ComponentScan` 이 들어있다.

---

## 컴포넌트 스캔의 기본 대상

- `@Component` : 컴포넌트 스캔에서 사용
- `@Controller` : 스프링 MVC 컨트롤러에서 사용, 스프링 MVC 컨트롤러로 인식
- `@Service` : 스프링 비즈니스 로직에서 사용, 특별한 처리X, 개발자들이 핵심 비즈니스 로직이 여기 있겠구나하고 비즈니스 계층을 인식하는데 도움이 된다.
- `@Repository`  : 스프링 데이터 접근 계층에서 사용, 데이터 계층의 예외를 스프링 예외로 변환???
- `@Configuration` : 스프링 설정 정보에서 사용, 스프링 빈이 싱글톤을 유지하도록 추가 처리

```java
@Component
public @interface Controller {
}

@Component
public @interface Service {
}

@Component
public @interface Configuration {
}
```

---

## 컴포넌트 스캔 필터

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {
}
```

```java
@MyIncludeComponent
public class BeanA {
}
```

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
```

```java
@MyExcludeComponent
public class BeanB {
}
```

- 내가 직접 애노테이션을 생성 - `@MyIncludeComponent` , `@MyExcludeComponent`
- 설정 정보 코드

    ```java
    @Configuration
    @ComponentScan(
            includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )
    static class ComponentFilterAppConfig {
    }
    ```

    - `includeFilters`에 `MyIncludeComponent`를 추가해 `BeanA`가 스프링 빈으로 등록된다.
    - `excludeFilters`에 `MyExcludeComponent`를 추가해 `BeanB`가 스프링 빈으로 등록되지 않는다.
    - `FilterType`
        - `ANNOTATION` : 기본값, 애노테이션을 인식해서 동작한다.
        - `ASSIGNABLE_TYPE` : 지정한 타입과 자식 타입을 인식해서 동작한다.
            - 만약 `BeanA`도 스프링빈 등록에서 제외하고 싶다면,

                ```java
                excludeFilters = {
                    @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class),
                    @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BeanA.class)
                }
                ```

              와 같이 `ASSIGNABLE_TYPE`을 이용해서 제외 특정 클래스인 `BeanA`를 제외 가능하다.


---

## 컴포넌트 스캔의 중복 등록과 충돌

1. 자동 빈 등록 vs 자동 빈 등록
    - 컴포넌트 스캔에 의해 자동으로 스프링 빈이 등록되는데, 그 이름이 같은 경우 스프링은 오류를 발생시킨다.
    - `ConflictingBeanDefinitionException` 발생
2. 자동 빈 등록 vs 수동 빈 등록
    - 수동 등록 빈이 우선권을 가진다.
        - 수동 등록 빈이 자동 등록 빈을 오버라이딩 해버린다.
    - 하지만 스프링 부트에서는 수동 빈 등록과 자동 빈 등록 충돌이 일어나면 오류가 발생하도록 기본 값이 변경 되었다.
        - 충돌되는 상황에서 스프링 부트인 `CoreApplication`을 실행해보면 오류를 볼 수 있다.

            ```java
            public class AutoAppConfig {
                @Bean(name = "memoryMemberRepository")
                MemberRepository memberRepository() {
                    return new MemoryMemberRepository();
                }
            }
            ```


---