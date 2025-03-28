package edu.byui.apj.storefront.db.service;

import edu.byui.apj.storefront.db.model.*;
import edu.byui.apj.storefront.db.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    private CardOrder testOrder;
    private Cart testCart;
    private Customer testCustomer;
    private Address testAddress;
    private Item testItem;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        testCustomer.setEmail("john@example.com");
        testCustomer.setPhone("123-456-7890");

        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setAddressLine1("123 Main St");
        testAddress.setCity("Springfield");
        testAddress.setState("IL");
        testAddress.setZipCode("62701");
        testAddress.setCountry("USA");

        testCart = new Cart();
        testCart.setId("cart1");
        testCart.setPersonId("person1");
        testCart.setItems(new ArrayList<>());

        testItem = new Item();
        testItem.setId(1L);
        testItem.setCart(testCart);
        testItem.setCardId("card1");
        testItem.setName("Test Card");
        testItem.setPrice(10.0);
        testItem.setQuantity(2);
        testCart.getItems().add(testItem);

        testOrder = new CardOrder();
        testOrder.setId(1L);
        testOrder.setCustomer(testCustomer);
        testOrder.setCart(testCart);
        testOrder.setShippingAddress(testAddress);
        testOrder.setOrderDate(new Date());
        testOrder.setConfirmationSent(false);
        testOrder.setShipMethod("Standard");
        testOrder.setOrderNotes("Test order");
        testOrder.setSubtotal(20.0);
        testOrder.setTax(2.0);
        testOrder.setTotal(22.0);
    }

    @Test
    void saveOrder_Success() {
        when(cartService.getCart(anyString())).thenReturn(testCart);
        when(orderRepository.save(any(CardOrder.class))).thenReturn(testOrder);

        CardOrder result = orderService.saveOrder(testOrder);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCart()).isEqualTo(testCart);
        assertThat(result.getCustomer()).isEqualTo(testCustomer);
        assertThat(result.getShippingAddress()).isEqualTo(testAddress);
        assertThat(result.getTotal()).isEqualTo(22.0);
        
        verify(cartService).getCart(testCart.getId());
        verify(orderRepository).save(testOrder);
    }

    @Test
    void saveOrder_CartNotFound() {
        when(cartService.getCart(anyString())).thenThrow(new RuntimeException("Cart not found"));

        assertThatThrownBy(() -> orderService.saveOrder(testOrder))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Cart not found");

        verify(cartService).getCart(testCart.getId());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Optional<CardOrder> result = orderService.getOrder(1L);

        assertThat(result).isPresent();
        CardOrder order = result.get();
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(order.getCart().getItems()).hasSize(1);
        assertThat(order.getShippingAddress().getCity()).isEqualTo("Springfield");
        
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrder_NotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<CardOrder> result = orderService.getOrder(999L);

        assertThat(result).isEmpty();
        verify(orderRepository).findById(999L);
    }
} 