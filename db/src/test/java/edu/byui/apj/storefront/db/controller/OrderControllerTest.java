package edu.byui.apj.storefront.db.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.byui.apj.storefront.db.model.*;
import edu.byui.apj.storefront.db.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void saveOrder_Success() throws Exception {
        when(orderService.saveOrder(any(CardOrder.class))).thenReturn(testOrder);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrder)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customer.firstName").value("John"))
                .andExpect(jsonPath("$.customer.lastName").value("Doe"))
                .andExpect(jsonPath("$.cart.id").value("cart1"))
                .andExpect(jsonPath("$.shippingAddress.addressLine1").value("123 Main St"))
                .andExpect(jsonPath("$.shipMethod").value("Standard"))
                .andExpect(jsonPath("$.total").value(22.0));

        verify(orderService).saveOrder(any(CardOrder.class));
    }

    @Test
    void saveOrder_InvalidData() throws Exception {
        testOrder.setTotal(null); // Invalid order, missing required field

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrder)))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).saveOrder(any(CardOrder.class));
    }

    @Test
    void getOrder_Success() throws Exception {
        when(orderService.getOrder(1L)).thenReturn(Optional.of(testOrder));

        mockMvc.perform(get("/order/{orderId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customer.firstName").value("John"))
                .andExpect(jsonPath("$.cart.items[0].name").value("Test Card"))
                .andExpect(jsonPath("$.total").value(22.0));

        verify(orderService).getOrder(1L);
    }

    @Test
    void getOrder_NotFound() throws Exception {
        when(orderService.getOrder(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/order/{orderId}", 999L))
                .andExpect(status().isNotFound());

        verify(orderService).getOrder(999L);
    }

    @Test
    void getOrder_InvalidId() throws Exception {
        mockMvc.perform(get("/order/{orderId}", "invalid-id"))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).getOrder(any());
    }
} 