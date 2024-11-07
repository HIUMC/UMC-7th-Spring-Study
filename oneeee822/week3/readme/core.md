# **스프링 컨테이너와 스프링 빈**

## 스프링 컨테이너

ApplicationContext를 스프링 컨테이너라고 하며 이는 인터페이스이다.

- XML을 기반으로 만들 수 있고, 애노테이션 기반의 자바 설정 클래스로 만들 수 있음
    - `AppConfig` 를 사용했던 방식이 애노테이션 기반의 자바 설정 클래스로 스프링 컨테이너를 만든 것

```java
//스프링 컨테이너 생성
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
```

이 클래스는 `ApplicationContext` 인터페이스의 구현체

스프링 컨테이너를 생성할 때는 구성 정보를 지정해주어야 함 → 여기서는 `AppConfig.class` 를 구성 정보로 지정

## 스프링 빈

스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 사용해서 스프링 빈을 등록함

### 빈이름

메서드 이름을 사용, 직접 부여 가능

- `@Bean(name="memberService2")`

**빈 이름은 항상 다른 이름을 부여해야함**. 같은 이름을 부여하면, 다른 빈이 무시되거나, 기존 빈을 덮어버리거나 설정에 따라 오류가 발생

## 스프링 빈 조회

**상속 관계**부모 타입으로 조회하면, 자식 타입도 함께 조회

→ 모든 자바 객체의 최고 부모인 `Object` 타입으로 조회하면, 모든 스프링 빈을 조회

## BeanFactory**와** ApplicationContext

### BeanFactory

스프링 컨테이너의 최상위 인터페이스
스프링 빈을 관리하고 조회하는 역할을 담당

`getBean()` 을 제공

## **ApplicationContext**

BeanFactory 기능을 모두 상속받아서 제공


### 부가기능

- **메시지소스를 활용한 국제화 기능**
  예를 들어서 한국에서 들어오면 한국어로, 영어권에서 들어오면 영어로 출력
- **환경변수**
  로컬, 개발, 운영등을 구분해서 처리
- **애플리케이션 이벤트**
  이벤트를 발행하고 구독하는 모델을 편리하게 지원
- **편리한 리소스 조회**
  파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회

# **싱글톤 컨테이너**

## **싱글톤 패턴**

클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴
→ 객체 인스턴스를 2개 이상 생성하지 못하도록 막아야 함

private 생성자를 사용해서 외부에서 임의로 new 키워드를 사용하지 못하도록 막음

### **문제점**

싱글톤 패턴을 구현하는 코드 자체가 많이 들어감
의존관계상 클라이언트가 구체 클래스에 의존 → DIP를 위반
클라이언트가 구체 클래스에 의존 → OCP 원칙을 위반할 가능성
테스트하기 어려움
내부 속성을 변경하거나 초기화 하기 어려움
private 생성자로 자식 클래스를 만들기 어려움

—> 유연성이 떨어짐

## **싱글톤 컨테이너**

싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리.

스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리.

컨테이너는 객체를 하나만 생성해서 관리, 스프링 컨테이너는 싱글톤 컨테이너 역할

싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라 함.

### 주의점

**무상태(stateless)**로 설계해야 함 → 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문

가급적 읽기만 가능해야 함

## @Configuration**과 싱글톤**

```java
 @Test
 void configurationDeep() {
      ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
			//AppConfig도 스프링 빈으로 등록된다.
			AppConfig bean = ac.getBean(AppConfig.class);
		  System.out.println("bean = " + bean.getClass());
		  //출력: bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$bd479d70
}
```

AnnotationConfigApplicationContext에 파라미터로 넘긴 값은 스프링 빈으로 등록 → AppConfig도 스프링 빈이 됨

스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다

른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록한 것

# **컴포넌트 스캔과 의존관계 자동 주입 시작하기**

스프링 빈을 등록할 때, 자바 코드의 @Bean이나 XML의 <bean> 등을 통해서 설정 정보에 직접 등
록할 스프링 빈을 나열 → 귀찮

- 컴포넌트 스캔 기능

  스프링은 설정 정보가 없어도 자동으로 스프링 빈을 등록

- @Autowired
  의존관계도 자동으로 주입

## 컴포넌트 스캔

컴포넌트 스캔을 사용하려면 먼저 `@ComponentScan` 을 설정 정보에 붙여줘야함.

컴포넌트 스캔을 사용하면 `@Configuration` 이 붙은 설정 정보도 자동으로 등록.

컴포넌트 스캔은 이름 그대로 `@Component` 애노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록.

`@Configuration` 이 컴포넌트 스캔의 대상이 된 이유도 `@Configuration` 소스코드를 열어보면 `@Component` 애노테이션이 붙어있기 때문

## 컴포넌트 스캔과 자동 의존관계 주입 동작방식

### 컴포넌트 스캔

@ComponentScan은 @Component가 붙은 모든 클래스를 스프링 빈으로 등록.
이때 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용.

**빈 이름 기본 전략:** MemberServiceImpl 클래스 memberServiceImpl

- **빈 이름 직접 지정:** 만약 스프링 빈의 이름을 직접 지정하고 싶으면
  @Component("memberService2") 이런식으로 이름을 부여하면 됨

### **@Autowired** 의존관계  자동 주입

생성자에 `@Autowired` 를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입 → 기본 조회 전략은 타입이 같은 빈을 찾아서 주입

## **컴포넌트 스캔 기본 대상**

@Component : 컴포넌트 스캔에서 사용
@Controller : 스프링 MVC 컨트롤러에서 사용
@Service : 스프링 비즈니스 로직에서 사용

@Repository : 스프링 데이터 접근 계층에서 사용
@Configuration : 스프링 설정 정보에서 사용

## **중복 등록과 충돌**

### 자동빈등록 vs 자동빈등록

컴포넌트 스캔에 의해 자동으로 스프링 빈이 등록되는데, 그 이름이 같은 경우 스프링은 오류를 발생.
`ConflictingBeanDefinitionException` 예외 발생

### 수동빈등록 vs 자동빈등록

수동 빈 등록이 우선권을 가짐 (수동 빈이 자동 빈을 오버라이딩)

최근 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생하도록 기본 값을 바꿈