package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1= new Member();
        member1.setName("kim");

        Member member2= new Member();
        member2.setName("kim");


        // when
        memberService.join(member1);

        // then
        //IllegalStateException 예외가 발생하지 않으면 테스트 실패
        assertThrows(IllegalStateException.class, () ->
                memberService.join(member2));
    }

    @SpringBootTest
    @Transactional
    class OrderServiceTest {

        @Autowired
        EntityManager em;
        @Autowired
        OrderService orderService;
        @Autowired
        OrderRepository orderRepository;

        @Test
        public void 상품주문() throws Exception {
            // given
            Member member = new Member();
            member.setName("회원1");
            member.setAddress(new Address("서울", "경기", "123-123"));
            em.persist(member);

            Book book = new Book();
            book.setName("시골 JPA");
            book.setPrice(10000);
            book.setStockQuantity(10);
            em.persist(book);

            int orderCount = 2;

            // when
            Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

            // then
            Order getOrder = orderRepository.findOne(orderId);
            assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
            assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확 해야 한다.");
            assertEquals(10000 * 2, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이 다.");
            assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
        }


    }
}