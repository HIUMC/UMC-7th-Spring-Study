# Member 도메인 개발

# MemberRepository

---

- `@Repository` 는 `@SpringBootApplication` 의 컴포넌트 스캔의 대상이 되어 Spring Bean으로 등록
- `@PersistenceContext` 어노테이션이 있으면 Spring이 생성한 JPA의 Entity Manager를 주입을 해준다. [하지만 RequiredArgsConstructor 를 사용하자](Member%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%2013fd916bc5a8808983bdc7c3753ba720.md)

![image.png](Member%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%2013fd916bc5a8808983bdc7c3753ba720/image.png)

### save()와 findOne()

---

```java
    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
```

## JPQL

---

*자세한 것은 기본편*

기능적으로 SQL과 동일하다. (결국 SQL로 번역이 돼야 하기 때문에)

SQL과의 차이점

- SQL은 테이블을 대상으로 쿼리를 작성
- JPQL은 entity-객체를 대상으로 쿼리를 작성
    - 예 : from의 대상이 테이블이 아니라 **엔티티**

### findALL()

---

```java
public List<Member> findAll() {

    List<Member> result = em.createQuery("select m from Member m", Member.class)
											      .getResultList();
    return result;
}
```

- 여기서 List 필드 이름에 **Ctrl + Alt + N** 리팩토링을 하면 줄여짐.

```java
public List<Member> findAll() {
    return em.createQuery("select m from Member m", Member.class)
             .getResultList();
}
```

### findByName()

---

```java
public List<Member> findByName(String name) {
    return em.createQuery("select m from Member m where m.name = :name", Member.class)
            .setParameter("name", name)
            .getResultList();
}
```

# MemberService

---

## @Transactional

---

- 기본적으로 **모든 데이터 변경과 관련된 로직들**은 Transaction 안에서 실행이 돼야한다.
    - 읽기는 `@Transactional(readOnly=true)` 옵션을 주면 최적화된다.
- **default는** readOnly=**false**임. 아래 용법 주목 (읽기 로직이 많으므로 이렇게 한다)
    
    ```java
    @Service
    @Transactional(readOnly = true)
    public class MemberService {
      
        ... ...
    
        //회원 가입
        @Transactional
        public Long join(Member member) {
            ...
        }
    
        //회원 전체 조회
        public List<Member> findMembers() {
            ...
        }
    
        public Member findOne(Long memberId) {
            ...
        }
    }
    
    ```
    
- `@Transactional` 은 spring이 제공하는 어노테이션을 쓰자. (쓸 수 있는 옵션 많음)

### 회원 가입

---

```java
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
```

참고 : 실무에서는 검증 로직이 있어도 멀티 쓰레드 상황 (예 : 두 명이 정말 동시에 같은 이름으로 등록이 되는 경우 등) 을 고려해서 회원 테이블의 회원명 칼럼에 유니크 제약 조건을 추가하는 것이 안전하다.

### 회원 조회

---

```java
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

```

## `@RequiredArgsConstructor`

---

- 코어편 내용:  `@RequiredArgsConstructor` 는  `@AllArgsConstructor` 와 다르게, final 필드에만 생성자 주입이 되도록 한다.
- JPA 내용 :
    - 스프링 부트에서 `@Autowired`부터 `@RequiredArgsConstructor` 는 Entity Manager의 주입에 대해서도 해결해 줌!
    - 따라서 MemberRepository에서 [@PersistenceContext 를 쓰며 final 도 없던 것](Member%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%2013fd916bc5a8808983bdc7c3753ba720.md) ,이 → 이렇게 가능
    
    ![image.png](Member%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%2013fd916bc5a8808983bdc7c3753ba720/image%201.png)
    

[회원 기능 테스트](Member%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%2013fd916bc5a8808983bdc7c3753ba720/%E1%84%92%E1%85%AC%E1%84%8B%E1%85%AF%E1%86%AB%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%20%E1%84%90%E1%85%A6%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%2013fd916bc5a8805a9974e15fc3034f28.md)