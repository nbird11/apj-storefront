package edu.byui.apj.storefront.db.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

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

        testItem = new Item();
        testItem.setId(itemId);
        testItem.setCardId("test-card-1");
        testItem.setName("Test Card");
        testItem.setPrice(10.0);
        testItem.setQuantity(1);
    }

    @Test
    void getCartNoOrder_Success() throws Exception {
        List<Cart> expectedCarts = Arrays.asList(testCart);
        when(cartService.getCartsWithoutOrders()).thenReturn(expectedCarts);

        mockMvc.perform(get("/cart/noorder"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(cartId))
                .andExpect(jsonPath("$[0].personId").value("test-person-1"));

        verify(cartService).getCartsWithoutOrders();
    }

    @Test
    void getCart_Success() throws Exception {
        when(cartService.getCart(anyString())).thenReturn(testCart);

        mockMvc.perform(get("/cart/{cartId}", cartId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.personId").value("test-person-1"));

        verify(cartService).getCart(cartId);
    }

    @Test
    void saveCart_Success() throws Exception {
        when(cartService.saveCart(any(Cart.class))).thenReturn(testCart);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCart)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.personId").value("test-person-1"));

        verify(cartService).saveCart(any(Cart.class));
    }

    @Test
    void addItemToCart_Success() throws Exception {
        when(cartService.addItemToCart(anyString(), any(Item.class))).thenReturn(testCart);

        mockMvc.perform(post("/cart/{cartId}/item", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cartId));

        verify(cartService).addItemToCart(cartId, testItem);
    }

    @Test
    void removeCart_Success() throws Exception {
        doNothing().when(cartService).removeCart(anyString());

        mockMvc.perform(delete("/cart/{cartId}", cartId))
                .andExpect(status().isOk());

        verify(cartService).removeCart(cartId);
    }

    @Test
    void removeItemFromCart_Success() throws Exception {
        when(cartService.removeItemFromCart(anyString(), anyLong())).thenReturn(testCart);

        mockMvc.perform(delete("/cart/{cartId}/item/{itemId}", cartId, itemId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cartId));

        verify(cartService).removeItemFromCart(cartId, itemId);
    }

    @Test
    void updateItemInCart_Success() throws Exception {
        when(cartService.updateCartItem(anyString(), any(Item.class))).thenReturn(testCart);

        mockMvc.perform(put("/cart/{cartId}/item", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cartId));

        verify(cartService).updateCartItem(cartId, testItem);
    }
} 