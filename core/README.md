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