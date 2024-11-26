# @ModelAttribute(””) 에 대해

# **동작 원리** velog

---

- **[Spring] @ModelAttribute 동작 원리 :**[https://velog.io/@jmjmjmz732002/Springboot-ModelAttribute](https://velog.io/@jmjmjmz732002/Springboot-ModelAttribute)
- Parameter 레벨
    - `@ModelAttribute`를 사용할 때는 **객체의 각 필드에 접근해 데이터를 바인딩 할 수 있는 생성자나 setter가 필요**하다.
    - 만약 생성자/setter를 정의하지 않는다면 모델 객체에 바인딩하지 못한다.
- **결론**
    - 내부적으로 **리플렉션(`ReflectionUtils`)**을 사용하므로 setter만 만들어서 사용해도 되고, 적절한 생성자(매개 변수가 가장 적은 생성자)를 기준으로 매개변수의 이름을 파라미터 이름과 동일하게 하여 사용해도 된다.
    - 참고로 **`@ModelAttribute`를 생략**하여도 Spring MVC는 `ModelAttributeMethodProcessor`를 우선적으로 호출해 생성자를 선택하는 과정을 가진다고 한다.

# chatGPT

---

스프링 부트에서 `@ModelAttribute("")` 어노테이션은 주로 **뷰(View)**에 데이터를 전달하거나, **폼 데이터를 바인딩**하는 데 사용됩니다. 이 어노테이션은 코드 내에서 반복적인 작업을 줄여주고, 명시적으로 객체를 생성하여 뷰에 전달하는 과정을 단순화합니다. 그럼 어떤 코드를 줄여주는지 살펴보겠습니다.

### 1. **뷰에 데이터 전달을 간소화**

컨트롤러 메서드에서 `@ModelAttribute`를 사용하면, 해당 메서드의 리턴값을 자동으로 뷰에 전달합니다. 이렇게 하면 일일이 `Model` 객체를 통해 데이터를 추가하는 코드를 줄일 수 있습니다.

### 예시 (코드 축약 전)

```java
@GetMapping("/user")
public String showUserForm(Model model) {
    model.addAttribute("user", new User());  // 직접 데이터를 뷰에 넣음
    return "userForm";
}
```

### 예시 (코드 축약 후)

```java
@GetMapping("/user")
public String showUserForm() {
    return "userForm";
}

@ModelAttribute("user")
public User setUpUserForm() {
    return new User(); // user 객체를 뷰에 자동으로 추가
}
```

여기서 `@ModelAttribute("user")`는 메서드에서 리턴한 객체를 `user`라는 이름으로 뷰에 전달하는 역할을 합니다.

- 더 알아보기
    - 나 : 그러면 모델이 html에 사용되는 방식을 슬쩍 보고와서 적절한 모델을 골라서 매핑해주는건가?
    
    - chatGPT)
    - **자세히 설명하자면, Spring은 컨트롤러 메서드와 뷰 간의 데이터 흐름을 효율적으로 관리**하며, 뷰가 어떤 데이터를 사용할지 미리 정리하여 적절한 `Model` 데이터를 매핑해줍니다. 이를 좀 더 구체적으로 설명하겠습니다.
        
        ---
        
        ### 1. **Spring의 데이터 바인딩 과정**
        
        Spring이 모델 데이터를 뷰에 전달할 때는 다음과 같은 과정을 거칩니다:
        
        1. **요청 처리**:
            - 특정 URL로 들어온 요청에 따라 적절한 컨트롤러 핸들러 메서드(`@GetMapping`, `@PostMapping` 등)를 호출합니다.
        2. **모델 데이터 설정**:
            - 핸들러 메서드가 호출되기 전에 **`@ModelAttribute` 메서드가 실행**되어 `Model`에 데이터를 추가합니다.
            - 핸들러 메서드 내부에서도 필요에 따라 데이터를 `Model`에 추가할 수 있습니다.
        3. **뷰 렌더링 준비**:
            - 핸들러 메서드가 반환한 뷰 이름과 함께 `Model`에 담긴 데이터를 뷰로 전달합니다.
            - 예를 들어, `return "userForm"`이라면 `userForm.html`이 렌더링되고, `Model`에 담긴 데이터를 기반으로 템플릿 엔진(Thymeleaf, JSP 등)이 HTML을 생성합니다.
        
        ---
        
        ### 2. **뷰에서 적절한 모델 데이터를 선택**
        
        Spring은 단순히 모든 데이터를 뷰로 넘기는 것이 아니라, **뷰 템플릿에서 실제로 사용되는 데이터만 적절히 매핑**하도록 설계되어 있습니다.
        
        ### 예시: `userForm.html`과 `updateUserForm.html`에서 데이터 사용
        
        ```java
        @Controller
        public class UserController {
        
            @GetMapping("/user")
            public String showUserForm() {
                return "userForm";
            }
        
            @GetMapping("/updateUser")
            public String updateUserForm() {
                return "updateUserForm";
            }
        
            @ModelAttribute("user")
            public User setUpUserForm() {
                return new User(); // "userForm" 뷰에서 사용
            }
        
            @ModelAttribute("users")
            public List<User> setUpUsers() {
                List<User> users = new ArrayList<>();
                users.add(new User("Alice", "alice@example.com"));
                users.add(new User("Bob", "bob@example.com"));
                return users; // "updateUserForm" 뷰에서 사용
            }
        }
        ```
        
        ### `userForm.html`:
        
        ```html
        <!-- "user" 객체가 필요 -->
        <form action="/submitUser" method="post">
            <input type="text" name="name" th:value="${user.name}" placeholder="Name">
            <input type="email" name="email" th:value="${user.email}" placeholder="Email">
            <button type="submit">Submit</button>
        </form>
        ```
        
        ### `updateUserForm.html`:
        
        ```html
        <!-- "users" 리스트가 필요 -->
        <table>
            <tr>
                <th>Name</th>
                <th>Email</th>
            </tr>
            <tr th:each="user : ${users}">
                <td th:text="${user.name}"></td>
                <td th:text="${user.email}"></td>
            </tr>
        </table>
        ```
        
        ---
        
        ### 3. **Spring의 자동 매핑 원리**
        
        Spring은 요청 핸들러 메서드(`@GetMapping`)가 반환한 뷰 이름에 따라 **뷰 템플릿에서 사용 가능한 데이터만 매핑**합니다.
        
        - `Model`에 들어 있는 모든 데이터는 뷰 템플릿에 전달됩니다.
        - 하지만 뷰 템플릿에서 실제로 사용되지 않는 데이터는 무시됩니다.
            - 예를 들어, `updateUserForm.html`은 `users`를 참조하지만, `user`는 참조하지 않습니다. 따라서 `user`는 뷰에서 사용되지 않습니다.
        
        ---
        
        ### 4. **컨트롤러의 책임 분리**
        
        Spring은 다음과 같은 역할 분리를 통해 효율적으로 데이터를 전달합니다:
        
        1. **데이터 준비**:
            - `@ModelAttribute`나 핸들러 메서드 내부에서 필요한 데이터를 준비합니다.
        2. **뷰 결정**:
            - 핸들러 메서드는 **어떤 뷰를 렌더링할지 결정**합니다.
            - 예: `return "userForm"`은 `userForm.html` 템플릿을 렌더링하도록 지시.
        3. **뷰와 데이터의 연결**:
            - Spring은 자동으로 `Model` 데이터를 뷰에 전달하며, 템플릿 엔진이 데이터에 접근하여 HTML을 생성합니다.
        
        ---
        
    
    ### 결론: Spring은 "사용자 중심으로 모델 선택"
    
    Spring이 뷰 템플릿의 요구사항을 "슬쩍 보고" 결정한다기보다는:
    
    - 컨트롤러에서 `Model`에 데이터를 적재합니다.
    - 템플릿 엔진은 **필요한 데이터만** 참조하여 사용합니다.
    
    따라서 뷰 템플릿의 로직과 컨트롤러 로직을 분리하면서도, 데이터 전달이 자연스럽게 이루어집니다.
    

### 2. **폼 데이터 바인딩 간소화**

사용자가 폼에 입력한 데이터를 객체에 자동으로 바인딩해주는 코드도 줄여줍니다.

### 예시 (코드 축약 전)

```java
@PostMapping("/submit")
public String submitForm(@RequestParam String name, @RequestParam String email) {
    UserForm userForm = new UserForm();
    userForm.setName(name);
    userForm.setEmail(email);
    // 폼 데이터 처리
    return "result";
}
```

### 예시 (코드 축약 후)

```java
@PostMapping("/submit")
public String submitForm(@ModelAttribute UserForm userform) {
    // 스프링이 자동으로 폼 데이터를 User 객체에 바인딩해줌
    return "result";
}
```

`@ModelAttribute`를 사용하면 폼 필드들이 자동으로 `UserForm` 객체에 바인딩되어, 수동으로 값을 설정하는 코드를 줄일 수 있습니다.

### 요약

`@ModelAttribute`는 다음과 같은 코드 작업을 줄여줍니다:

1. **데이터를 뷰로 전달할 때** `Model` 객체를 사용하여 데이터를 일일이 추가하는 코드를 줄여줍니다.
2. **폼 데이터를 객체에 바인딩할 때** 수동으로 바인딩하는 코드를 생략할 수 있습니다.

이 어노테이션을 적절히 사용하면 중복된 코드를 최소화하고, 코드 가독성을 높일 수 있습니다.