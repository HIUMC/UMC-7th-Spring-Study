package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice); //member 확인만 하고 discountPolicy 에 할인관련 계산 넘겨버림

        return new Order(memberId, itemName, itemPrice, discountPrice); //최종가격으로 바뀐 Order 객체를 return
    }
//테스트용
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
