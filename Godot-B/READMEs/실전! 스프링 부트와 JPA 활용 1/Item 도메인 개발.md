# Item 도메인 개발

# 엔티티 메서드

---

## 비즈니스 로직

---

- 재고를 넣고 빼는 로직
    - 현재 스톡 수량 정보를 사용하기 때문에,  핵심 비즈니스 로직을 엔티티에 직접 넣었음.
        
        item 서비스에 비즈니스 로직을 넣지 않고, 데이터를 가지고 있는 쪽에 비즈니스 메소드가 있는게 가장 좋다. ⇒ 응집력이 있음
        

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
```

### NotEnoughStockException 예외 만들기

---

- RuntimeException 예외를 상속받아서 오버라이딩

```java
package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

```

# ItemRepository

---

## save()

---

```java
@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }
}
```

### persist()

---

신규 등록

### merge()

---

업데이트 (와 비슷한 개념)

→ 나중에 더 구체적으로 설명

## findOne()과 findALL()

---

- 단건 조회는 find로 가능하지만
- 여러 개 찾는 것은 JPQL을 작성해야한다.

```java
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i ", Item.class)
                .getResultList();
    }
```

# ItemServce

---

- ItemRepository를 단순 위임하는 형태

```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    
    private final ItemRepository itemRepository;
    
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }
    
    public List<Item> findItems() {
        return itemRepository.findAll();
    }
    
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
```

- 이런 경우에는 컨트롤러가 굳이 서비스 거칠 필요 없이 리포지토리 접근하게 설계해도 괜찮음.

---

테스트는 이전 Member 기능과 유사하므로 생략