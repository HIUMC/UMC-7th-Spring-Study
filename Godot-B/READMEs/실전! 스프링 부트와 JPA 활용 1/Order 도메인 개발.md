# Order 도메인 개발

제일 중요한 도메인

- 비즈니스 로직들이 얽혀서 돌아가는 걸 JPA나 엔티티를 가지고 어떻게 풀어내는지 알아보자!
    - 트랜잭션 스크립트 패턴
    - **도메인 모델 패턴 중심 강의**

# 엔티티 메서드

---

## 생성 메서드

---

- 주문 생성은 복잡하다 = 오더만 생성해서 될 게 아니라 오더 아이템, 딜리버리 등등 여러개 연관관계가 들어가고 복잡!
    - 따라서 엔티티에 생성 메서드를 작성하자. ⇒ 응집 good

```java
@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    ...
    
    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
}
```

[…(가변 인자)가 제공하는 편의성](Order%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%20140d916bc5a880a2bf97c37968fe6226/%E2%80%A6(%E1%84%80%E1%85%A1%E1%84%87%E1%85%A7%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%8C%E1%85%A1)%E1%84%80%E1%85%A1%20%E1%84%8C%E1%85%A6%E1%84%80%E1%85%A9%E1%86%BC%E1%84%92%E1%85%A1%E1%84%82%E1%85%B3%E1%86%AB%20%E1%84%91%E1%85%A7%E1%86%AB%E1%84%8B%E1%85%B4%E1%84%89%E1%85%A5%E1%86%BC%20140d916bc5a880978ec1fcfcb7022953.md)

### OrderItem도 생성 메서드를 넣어두자.

```java
package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class OrderItem {

   ...
   
    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }
}

```

## 비즈니스 로직

---

### 주문 취소_cancel()

---

```java
@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    ...
    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
```

### orderItem의 cancel()을 구현해야 한다. (재고수량 원복)

```java
@Entity
@Getter @Setter
public class OrderItem {

    ...

    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    }
}
```

## 조회 로직

---

### 전체 주문 가격 조회_getTotalPrice()

---

```java
@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    ...
    
    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
```

![image.png](Order%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%20140d916bc5a880a2bf97c37968fe6226/image.png)

- 자바8 내용) 이렇게도 표현이 가능하다.

### orderItem의 getTotalPrice()를 구현해야 한다. (왜❓ 각 orderItem마다 주문 수량이 있으므로❗)

```java
@Entity
@Getter @Setter
public class OrderItem {

    ...

    //==조회 로직==//
    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
```

# OrderRepository

---

## save(), findOne(), 그리고 findAll() : 동적 쿼리

```java
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

//    public List<Order> findAll(OrderSearch orderSearch) {}
}
```

- findAll() : 동적 쿼리 개념의 검색 기능
    - 한 강의로 쪼개서 나중에 설명하겠다!
    
    [주문 검색 기능 개발](Order%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%20140d916bc5a880a2bf97c37968fe6226/%E1%84%8C%E1%85%AE%E1%84%86%E1%85%AE%E1%86%AB%20%E1%84%80%E1%85%A5%E1%86%B7%E1%84%89%E1%85%A2%E1%86%A8%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%20141d916bc5a880d3807dddfe7aac1a2d.md)
    

# OrderService

---

## 주문_order()

---

```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }
}
```

### Cascade 옵션에 관해

---

- `orderRepository.save(order);`를 통해 order 객체만 persist 해줘도,
- `Cascade`옵션에 의해 `OrderItem`과`Delivery`도 persist가 일어난다!

![image.png](Order%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%20140d916bc5a880a2bf97c37968fe6226/image%201.png)

- Cascade 옵션을 어디까지 허용해야 하나?

⇒ 해당 케이스는 이 2가지 조건이 다 충족하므로 가능함. (강의록 메모)

- 딱 Order만 Delivery를 사용하고, Order만 OrderItem을 사용함
    
    (영한 : 참조하는 게 딱 주인이 프라이빗 오너인 경우에만 써야돼요. 예를 들어서 Delivery가 되게 중요해서, 다른 데에서도 참조하고 갖다 쓰는 경우 cascade 막 쓰시면 안돼요. OrderItem도 마찬가지.)
    
- persist해야되는 Life Cycle도 완전히 똑같음
    
    (영한: 라이프사이클에 대해서 동일하게 관리를 할 때에는 이것들이 의미가 있습니다.)
    

영한: 개념이 명확히 이해가 안간다면, 안 쓰는게 안전하다. 이후 코드 리팩토링해서 쓸 것을 권장한다.

### `@NoArgsConstructor(access = AccessLevel.*PROTECTED*)` 에 관해

---

```java
//주문상품 생성
OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
```

- **생성 메서드의 존재를 모른다면 누군가는 주문 로직의 위 코드를 아래처럼 작성할 수도 있다.**

```java
//주문상품 생성
OrderItem orderItem = new OrderItem();
orderItem.setItem(item);
orderItem.set ...
...
```

**위와 같은 상황**(로직이 일관성 없이 여러 메서드 곳곳마다 분산되는 등) **을 막기 위해** protected 생성자를 만들어둔다. 그리고 이것을 위해 존재하는 어노테이션이 `@NoArgsConstructor(access = AccessLevel.*PROTECTED*)` !!

```java
@Entity
@Getter @Setter
public class OrderItem {

    ...

		protected OrderItem() {
		}
		
    ...
}
```

```java
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    ...
		
}
```

## 주문 취소_cancelOrder()

---

```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }
}
```

### 도메인 모델 패턴의 강점이 바로 여기서 나온다!

---

- 주문 서비스의 주문과 주문 취소 메서드를 보면 **비즈니스 로직 대부분이 엔티티에 있다.** **서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할**을 한다. 이처럼 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 [도메인 모델 패턴](http://martinfowler.com/eaaCatalog/domainModel.html) 이라 한다.
- 반대로 엔티티에는 비즈니스 로직이 거의 없고 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 [트랜잭션 스크립트 패턴](http://martinfowler.com/eaaCatalog/transactionScript.html) 이라 한다.
    - 도메인 모델 패턴이 항상 옳다 == (**X**)
        - 한 프로젝트 안에서도 트랜잭션 스크립트 패턴과 도메인 모델 패턴이 공존할 수 있음.
            
            → 뭐가 더 유지 보수하기 쉬운지를 고민해보면 됨.
            

## 검색_findOrders()

---

```java
    //검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
```

- OrderRepository의 findAll() 에 의존 : 동적 쿼리 개념의 검색 기능
    - 한 강의로 쪼개서 나중에 설명하겠다!
    - [주문 검색 기능 개발](Order%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%20140d916bc5a880a2bf97c37968fe6226/%E1%84%8C%E1%85%AE%E1%84%86%E1%85%AE%E1%86%AB%20%E1%84%80%E1%85%A5%E1%86%B7%E1%84%89%E1%85%A2%E1%86%A8%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%20141d916bc5a880d3807dddfe7aac1a2d.md)

[주문 기능 테스트](Order%20%E1%84%83%E1%85%A9%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%80%E1%85%A2%E1%84%87%E1%85%A1%E1%86%AF%20140d916bc5a880a2bf97c37968fe6226/%E1%84%8C%E1%85%AE%E1%84%86%E1%85%AE%E1%86%AB%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%20%E1%84%90%E1%85%A6%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%20141d916bc5a88016a98fd20062e84630.md)