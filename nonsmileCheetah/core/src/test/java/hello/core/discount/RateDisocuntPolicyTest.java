package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("Vip는 10%할인이 적용되어야 한다")
    void vip_0(){
        Member member = new Member(1L, "memberVIP", Grade.VIP);
        int discount = discountPolicy.discount(member, 10000);
        Assertions.assertThat(discount).isEqualTo(1000);
    }
    @Test
    @DisplayName("Vip가 아니면 10%할인이 적용되지 않아야 한다")
    void vip_x(){
        Member member2 = new Member(2L, "memberBasic", Grade.BASIC);
        int discount = discountPolicy.discount(member2, 10000);
        Assertions.assertThat(discount).isEqualTo(0);
    }
}