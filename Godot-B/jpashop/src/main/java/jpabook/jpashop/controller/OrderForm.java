package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderForm {

    private Long memberId;
    private Long itemId;
}
