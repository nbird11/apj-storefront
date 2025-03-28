package edu.byui.apj.storefront.db.service;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private Cart testCart;
    private Item testItem;
    private String cartId;
    private Long itemId;

    @BeforeEach
    void setUp() {
        cartId = "test-cart-1";
        itemId = 1L;

        testCart = new Cart();
        testCart.setId(cartId);
        testCart.setPersonId("test-person-1");
        testCart.setItems(new ArrayList<>());

        testItem = new Item();
        testItem.setId(itemId);
        testItem.setCardId("test-card-1");
        testItem.setName("Test Card");
        testItem.setPrice(10.0);
        testItem.setQuantity(1);
    }

    @Test
    void addItemToCart_Success() {
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.addItemToCart(cartId, testItem);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cartId);
        assertThat(result.getItems()).contains(testItem);
        verify(cartRepository).findById(cartId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addItemToCart_CartNotFound() {
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addItemToCart(cartId, testItem))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cart not found");
    }

    @Test
    void removeItemFromCart_Success() {
        testCart.getItems().add(testItem);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.removeItemFromCart(cartId, itemId);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
        verify(cartRepository).findById(cartId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void updateCartItem_Success() {
        testCart.getItems().add(testItem);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.updateCartItem(cartId, testItem);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).contains(testItem);
        verify(cartRepository).findById(cartId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void getCart_Success() {
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(testCart));

        Cart result = cartService.getCart(cartId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cartId);
        verify(cartRepository).findById(cartId);
    }

    @Test
    void getCart_NotFound() {
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCart(cartId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cart not found");
    }

    @Test
    void saveCart_Success() {
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.saveCart(testCart);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cartId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void getCartsWithoutOrders_Success() {
        List<Cart> expectedCarts = Arrays.asList(testCart);
        when(cartRepository.findCartsWithoutOrders()).thenReturn(expectedCarts);

        List<Cart> result = cartService.getCartsWithoutOrders();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result).contains(testCart);
        verify(cartRepository).findCartsWithoutOrders();
    }

    @Test
    void removeCart_Success() {
        doNothing().when(cartRepository).deleteById(cartId);

        cartService.removeCart(cartId);

        verify(cartRepository).deleteById(cartId);
    }
} 