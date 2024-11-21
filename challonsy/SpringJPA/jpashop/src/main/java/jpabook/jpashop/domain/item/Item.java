package jpabook.jpashop.domain.item;


import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.excption.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="dtype")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name ="item_id")
    private Long id;

    @ManyToMany(mappedBy="items")
    private List<Category> categories = new ArrayList<>();

    private String name;
    private int price;
    private int stockQuantity;


    //비즈니스 로직//
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity =restStock;
    }
}
