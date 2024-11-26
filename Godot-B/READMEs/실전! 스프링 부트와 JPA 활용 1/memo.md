# memo

1. [NoArgsConstructor](Order%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%20140d916bc5a880a2bf97c37968fe6226.md)
2. 

- `Assert.assertEquals`와 `Assertions.assertEquals` 가 차이가 있다!
    - 강의에서 `Assert.assertEquals` 로 string message를 띄우는 것을 확인할 수 있다.
        - string message를 띄우는 방법을 AssertJ 라이브러리의 `Assertions.assertThat()`에서 사용하는 방법
            
            `Assert.assertEquals()`에서 `message`를 사용하는 방식은 테스트 실패 시 메시지를 출력하기 위해 사용됩니다. 예를 들어:
            
            ```java
            Assert.assertEquals("Failure message: values do not match", expected, actual);
            
            ```
            
            이 방식과 비슷하게 `Assertions.assertThat()` (AssertJ 라이브러리)에서도 실패 메시지를 출력하려면 **`describedAs()`** 또는 **`overridingErrorMessage()`** 메서드를 활용할 수 있습니다.
            
            ### 방법 1: `describedAs()`
            
            `describedAs()` 메서드를 사용해 메시지를 지정합니다.
            
            ```java
            Assertions.assertThat(actual)
                      .describedAs("Failure message: values do not match")
                      .isEqualTo(expected);
            
            ```
            
            ### 방법 2: `overridingErrorMessage()`
            
            `overridingErrorMessage()`를 사용해 더 세밀한 메시지를 지정할 수도 있습니다.
            
            ```java
            Assertions.assertThat(actual)
                      .overridingErrorMessage("Expected value: %s, but got: %s", expected, actual)
                      .isEqualTo(expected);
            
            ```
            
            ### 차이점
            
            - `describedAs()`는 간단한 메시지를 출력할 때 적합합니다.
            - `overridingErrorMessage()`는 출력 메시지를 커스터마이징할 때 더 유용합니다. 예를 들어, `%s`를 사용하여 동적인 값을 포함할 수 있습니다.
            
            이 두 가지 방법으로 원하는 방식으로 메시지를 표시할 수 있습니다. 상황에 따라 적합한 방식을 선택하면 됩니다.
            
            **Q1. describedAs와 overridingErrorMessage의 내부 구현 차이는 무엇인가요?**
            
            **Q2. AssertJ를 사용하지 않고 JUnit5에서 유사한 사용자 정의 메시지를 출력하는 방법은 무엇인가요?**
            
            **Q3. Assertions.assertThat()과 Assert.assertEquals()의 주요 차이점은 무엇인가요?**
            
    - `Assertions.assertEquals`가 JUnit5부터는 권장되지 않는 이유
        
        네, **`Assertions.assertEquals()`**는 JUnit5에서도 사용할 수 있지만, 권장되지 않는 방식은 아닙니다. JUnit5는 기본적으로 **`assertEquals`** 같은 메서드를 제공하지만, 더 나은 가독성과 확장성을 위해 AssertJ와 같은 서드파티 검증 라이브러리 사용이 많이 권장됩니다.
        
        ### 왜 JUnit5의 `Assertions.assertEquals()`가 덜 권장되는가?
        
        1. **가독성 부족**
            
            `Assertions.assertEquals(expected, actual)`의 인수 순서가 직관적이지 않을 수 있습니다. 반면 AssertJ는 `assertThat(actual).isEqualTo(expected)` 형태로 더 읽기 쉽고 가독성이 높습니다.
            
            ```java
            Assertions.assertEquals("expectedValue", actualValue); // JUnit5
            assertThat(actualValue).isEqualTo("expectedValue");   // AssertJ (더 직관적)
            
            ```
            
        2. **제한적인 API**
            
            JUnit5의 기본 Assertions API는 비교적 단순한 데이터 타입(문자열, 숫자 등)이나 기본 조건(동일성, null 확인 등)을 검증하는 데 적합합니다. AssertJ는 컬렉션, 예외, 스트림, 날짜 등을 검증하기 위한 훨씬 더 풍부한 API를 제공합니다.
            
        3. **체이닝 불가능**
            
            JUnit5의 `assertEquals`는 단일 검증을 처리할 뿐이며, 추가적인 조건을 체이닝으로 표현할 수 없습니다. AssertJ는 유연한 체이닝을 통해 여러 조건을 자연스럽게 연결할 수 있습니다.
            
            ```java
            // JUnit5
            Assertions.assertEquals("expectedValue", actualValue);
            Assertions.assertTrue(actualValue.startsWith("expected"));
            
            // AssertJ
            assertThat(actualValue)
                .isEqualTo("expectedValue")
                .startsWith("expected");
            
            ```
            
        4. **더 좋은 오류 메시지**
            
            AssertJ는 실패 시 훨씬 더 구체적이고 유용한 디버깅 메시지를 제공합니다. JUnit5의 오류 메시지는 상대적으로 제한적입니다.
            
        
        ---
        
        ### 권장 방식
        
        JUnit5 기반 프로젝트에서 다음과 같은 방식이 선호됩니다:
        
        1. **단순한 테스트**에서는 JUnit5 `Assertions`를 사용.
        2. **복잡한 검증**이나 **가독성을 중시**한다면 AssertJ와 같은 라이브러리 활용.
        
        ### 결론
        
        JUnit5의 `Assertions.assertEquals()`는 여전히 사용할 수 있지만, 더 직관적이고 가독성이 높은 AssertJ와 같은 도구가 현대적인 Java 테스트에서 더 선호됩니다. 특히 대규모 프로젝트나 복잡한 테스트 시 AssertJ가 더욱 강력한 대안으로 자리 잡고 있습니다.
        
        ---
        
        **Q1. AssertJ가 제공하는 추가적인 검증 기능은 무엇인가요?**
        
        **Q2. JUnit5의 기본 Assertions를 효율적으로 사용하는 경우는 언제인가요?**
        
        **Q3. AssertJ와 JUnit5 Assertions를 혼용해서 사용하는 것이 적합한 경우는 어떤 경우인가요?**
        
    
    ### 왜 강의에서 AssertJ가 언급되지 않았을까?
    
    강의에서 AssertJ를 언급하지 않은 이유는 다음과 같을 수 있습니다:
    
    1. **기초 중심의 강의**
        - 강의가 JUnit의 기본적인 기능(JUnit4에서 제공하는 `Assert` API)에 집중하고 있을 가능성이 큽니다.
        - AssertJ는 서드파티 라이브러리이므로, 초보자 강의에서는 이를 생략하고 기본 기능만 다루는 경우가 많습니다.
    2. **JUnit4 기반 테스트**
        - JUnit5가 나오기 전에는 JUnit4와 기본 Assertions만으로 많은 테스트가 작성되었습니다. 따라서 강의가 JUnit5 이전의 작성 방식을 중심으로 설명하고 있을 수 있습니다.
    3. **AssertJ에 대한 추가 학습 기대**
        - AssertJ는 표준 라이브러리가 아니기 때문에, 강의에서 이를 다루지 않고 학습자가 별도로 익히도록 할 수도 있습니다.

- 또한 SpringBoot 2.4부터는 @SpringBootTest 만 있어도 된다.
- @SpringBootTest
    
    `@SpringBootTest`는 Spring Boot에서 제공하는 통합 테스트를 위한 애노테이션입니다. 이 애노테이션을 사용하면 애플리케이션의 **전체 Spring Context**를 로드하여 통합 테스트를 수행할 수 있습니다. 실제 애플리케이션 환경과 최대한 유사한 상태에서 테스트를 실행하기 때문에, 단위 테스트보다 포괄적인 테스트를 진행할 때 유용합니다.
    

```java
~~@RunWith(SpringRunner.class)~~ // JUnit5면, 빼도 됨 !
@SpringBootTest
@Transactional
public class OrderServiceTest {
	...
}
```