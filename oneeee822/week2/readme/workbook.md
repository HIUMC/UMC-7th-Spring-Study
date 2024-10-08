# JOIN

두 개의 테이블을 서로 묶어서 하나의 결과를 만들어 내는 것

두 테이블의 조인을 위해서는 기본키(PRIMARY KEY, PK)와 외래키(FOREIGN KEY, FK) 관계로 맺어져야 함. → **일대다 관계**

## **INNER JOIN(내부 조인)**

---

- 두 테이블을 조인할 때, 두 테이블에 모두 지정한 열의 데이터가 있어야 함

```sql
SELECT <열 목록>
FROM <첫 번째 테이블>
    INNER JOIN <두 번째 테이블>
    ON <조인 조건>
[WHERE 검색 조건]

#INNER JOIN을 JOIN이라고만 써도 INNER JOIN으로 인식
```

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/70176f61-0732-4bf3-9546-0935c3382e84/f227cab7-a963-4b3d-9e9c-ce903d3a6309/image.png)

## **OUTER JOIN(외부 조인)**

---

- 두 테이블을 조인할 때, 1개의 테이블에만 데이터가 있어도 결과가 나옴

```sql
SELECT <열 목록>
FROM <첫 번째 테이블(LEFT 테이블)>
    <LEFT | RIGHT | FULL> OUTER JOIN <두 번째 테이블(RIGHT 테이블)>
     ON <조인 조건>
[WHERE 검색 조건]
```

### **LEFT** OUTER JOIN

- 왼쪽 테이블의 모든 값이 출력되는 조인

**INNER 조인 vs LEFT 조인**

INNER 조인의 경우 양측 모두에 존재하는 것만 결과로 만듦

LEFT 조인은 좌측 테이블 중 조인 불가능한 것들도 모두 결과로 만듦

이때, 조인 불가능한 우측 테이블은 값은 NULL로 채워짐

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/70176f61-0732-4bf3-9546-0935c3382e84/65c14749-3ae8-4a61-9389-34e0b235b476/image.png)

### **RIGHT** OUTER JOIN

- 오른쪽 테이블의 모든 값이 출력되는 조인

### **FULL** OUTER JOIN

- 왼쪽 외부 조인과 오른쪽 외부 조인이 합쳐진 것, 좌측,우측 상관없이 데이터가 있는 것은 모두 가져오고 없는 것은 모두 Null 이 되는 것

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/70176f61-0732-4bf3-9546-0935c3382e84/6b11d7b5-98db-4c74-a70e-697003c59e06/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/70176f61-0732-4bf3-9546-0935c3382e84/ee454331-897e-4c6a-8817-d4469c1c30ad/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/70176f61-0732-4bf3-9546-0935c3382e84/93951b9a-1b13-4600-bf19-c57db0c46e0e/image.png)

## **CROSS JOIN(상호 조인)**

---

- 한쪽 테이블의 모든 행과 다른 쪽 테이블의 모든 행을 조인하는 기능
- 상호 조인 결과의 전체 행 개수는 두 테이블의 각 행의 개수를 곱한 수 → **카티션 곱(CARTESIAN PRODUCT)**

```sql
SELECT *
FROM <첫 번째 테이블>
    CROSS JOIN <두 번째 테이블>
    
//표준 SQL과는 달리 MySQL에서는JOIN, INNER JOIN, CROSS JOIN이 모두 같은 의미로 사용
```

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/70176f61-0732-4bf3-9546-0935c3382e84/7209918b-723b-4cdc-8b1a-ef797a3225ee/image.png)

## **SELF JOIN(자체 조인)**

---

- 자신이 자신과 조인한다는 의미로, 1개의 테이블을 사용

```sql
SELECT <열 목록>
FROM <테이블> 별칭A
    INNER JOIN <테이블> 별칭B
[WHERE 검색 조건]
```

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/70176f61-0732-4bf3-9546-0935c3382e84/6b61d1ba-bff4-490b-a93b-99dcdbb46f88/image.png)

## UNION

---

- 여러 개의 SELECT문의 결과를 하나의 테이블이나 결과 집합으로 표현할 때 사용
- 각각의 SELECT문으로 선택된 필드의 개수와 타입, 필드의 순서는 모두 같아야 함
- 기본 집합 쿼리에는 (DISTINCT)중복제거가 자동으로 포함되어있음

```sql
SELECT 필드이름 FROM 테이블이름
UNION
SELECT 필드이름 FROM 테이블이름
```

### UNION ALL

- UNION은 DISTINCT 자동 포함이라 중복되는 레코드 제거 → 중복되는 레코드까지 모두 출력하고 싶을 때 ALL 키워드 사용

```sql
SELECT 필드이름 FROM 테이블이름
UNION ALL
SELECT 필드이름 FROM 테이블이름
```

## EXCLUSIVE LEFT JOIN

---

- 어느특정 테이블에 있는 레코드만 가져오는 것
- 만약 테이블 두 개를 JOIN한다면 둘 중 한가지 테이블에만 있는 데이터를 가져옴
- 기존의LEFT JOIN과 Where절의 조건을 함께 사용하여 만드는 JOIN 문법

```sql
SELECT * FROM table1 A 
LEFT JOIN table2 B
ON A.ID_SEQ = B.ID_SEQ 
WHERE B.ID_SEQ IS NULL

-- 조인한 B 테이블의 값이 null만 출력하라는 말은, 조인이 안된 A 레코드 나머지값만 출력하라는 말Copy
```

- 어느 **특정 테이블에 있는 레코드만** 가져오는 것

!https://blog.kakaocdn.net/dn/tRnYW/btrj7RW8PjU/FHO8jU0UvBVorNQcq7yok0/img.png

# **JOIN에서 중복된 레코드 제거하기**

## **Distinct 사용**

- mysql에서 지원하는 distinct 문법을 사용
- **레코드 수가 많은 경우 성능 느림**

## JOIN 전에 중복을 제거하기

- 조인할 테이블에서브쿼리의 inline view를 사용해 distinct 하고 조인

```sql
select A.name, A.countryCode 
from city A 
left join ( select distinct name, Code from country ) as B
-- 조인할 테이블에 먼저 distinct로 중복을 제거한 select문을 서브쿼리로 불러와 임시테이블로 만든뒤 조인한다
on A.countrycode = B.CodeCopy
```

- 일반적으로 레코드 수가 많은 경우 JOIN 전에 중복을 제거해서 1:1 JOIN으로 바꾸는 것이 훨씬 빠름



출처:

[https://hongong.hanbit.co.kr/sql-기본-문법-joininner-outer-cross-self-join/](https://hongong.hanbit.co.kr/sql-%EA%B8%B0%EB%B3%B8-%EB%AC%B8%EB%B2%95-joininner-outer-cross-self-join/)

[https://inpa.tistory.com/entry/MYSQL-📚-JOIN-조인-그림으로-알기쉽게-정리](https://inpa.tistory.com/entry/MYSQL-%F0%9F%93%9A-JOIN-%EC%A1%B0%EC%9D%B8-%EA%B7%B8%EB%A6%BC%EC%9C%BC%EB%A1%9C-%EC%95%8C%EA%B8%B0%EC%89%BD%EA%B2%8C-%EC%A0%95%EB%A6%AC)

https://velog.io/@wijoonwu/JOIN

# 서브쿼리

- 하나의 SQL문 안에 포함되어 있는 또 다른 SQL문
- 메인쿼리(부모쿼리, 외부쿼리) : 서브쿼리를 포함하고 있는 쿼리
- 서브쿼리(자식쿼리, 내부쿼리)

메인쿼리가 서브쿼리를 포함하는 종속적인 관계

- 조인은 모든 테이블이 대등한 관계에 있음 → 모든 테이블의 칼럼을 어느 위치에서라도 자유롭게 사용할 수 있음
- 서브쿼리는 메인쿼리의 컬럼을 모두 사용할 수 있음 메인 쿼리는 서브 쿼리의 칼럼을 사용할 수 없음. Java의 상속과 똑같은 개념

- 조인은 집합간의 곱의 관계
    - 1:1 관계 테이블이 조인하면 (1*1) 레벨의 집합 생성, M:N 관계가 조인하면 MN 레발의 집합 생성
- 서브쿼리는 서브쿼리 레발과 상관없이 항상 메인쿼리 레벨로 결과 집합이 생성

### 주의할 점

- SELECT문으로만 작성할 수 있음
- 서브쿼리를 괄호로 감싸서 사용
- 서브쿼리는 단일 행 또는 복수 행 비교 연산자와 함께 사용 가능
- 서브쿼리에서는 ORDER BY를  사용하지 못함

### 사용 가능한 곳

- SELECT
    - 스칼라 서브쿼리
    - 하나의 컬럼처럼 사용
    - NULL값을 리턴할 수 있음
- FROM
    - FROM절에서 사용되는 서브쿼리를 **인라인 뷰**라고함.
    - 뷰처럼 결과가 동적으로 생성된 테이블로 사용. 임시적인 뷰이기 때문에 데이터베이스에 저장되지 않음
    - 인라인 뷰의 칼럼은 자유롭게 참조 가능 → 인라인 뷰로 동적으로 생성된 테이블이기 때문
    - 무조건 AS 별칭을 지정해 주어야 함
- WHERE
    - 일반 서브쿼리
    - 하나의 변수처럼 사용
- HAVING
- ORDER BY
- INSERT문의 VALUES
- UPDATE문의 SET

## 단일행 서브쿼리

- 조건값을 select로 특정할 때
- 서브쿼리 실행 결과가 항상 1건 이하.
- 단일행 서브쿼리는 단일 행 비교 연산자( =, <. > <=, >=, <>) 와 함께 사용

```sql
select name, height 
from userTbl
where height > (select height from userTbl where name in ('김경호'));
```

## 다중행 서브쿼리

- IN, ANY, ALL, EXISTS 등의 연산자로 얻은 서브쿼리 결과 여러개의 행을 반환
    - ***IN*** - 서브쿼리의 결과에 존재하는 임의의 값과 동일한 조건을 의미
    - 조건에 값이 여러개 들어올 땐 **any**사용
        - any는 in과 동일한 의미 (or을 의미)

    ```sql
    select name, height 
    from userTbl
    where height = any(select height from userTbl where addr in ('경남'));
    ```

    - all은 도출된 모든 조건값에 대해 만족할 때
        - and를 의미

    ```sql
    select * 
    from city
    where population > all( select population from city where district = 'New York' );
    ```

    - ***EXISTS*** - 서브쿼리의 결과를 만족하는 값이 존재하는지 여부를 확인하는 조건을 의미, 조건을 만족하는 건이 여러 건이더라도 1건만 찾으면 더 이상 검색하지 않음

## 다중컬럼 서브쿼리

- 서브쿼리의 실행 결과로 여러 컬럼을 반환
- 메인쿼리의 조건절에 여러 컬럼을 동시에 비교할 수 있음
- 서브쿼리와 메인쿼리에서 비교하고자 하는 컬럼 개수와 컬럼의 위치가 동일해야 함

출처 : https://snowple.tistory.com/360, https://inpa.tistory.com/entry/MYSQL-%F0%9F%93%9A-%EC%84%9C%EB%B8%8C%EC%BF%BC%EB%A6%AC-%EC%A0%95%EB%A6%AC