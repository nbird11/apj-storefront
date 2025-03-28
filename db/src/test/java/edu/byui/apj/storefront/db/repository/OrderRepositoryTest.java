package edu.byui.apj.storefront.db.repository;

import edu.byui.apj.storefront.db.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Customer customer1;
    private Customer customer2;
    private Cart cart1;
    private Cart cart2;
    private CardOrder order1;
    private CardOrder order2;
    private Address address1;
    private Address address2;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        // Clear previous test data
        orderRepository.deleteAll();
        
        // Create customers
        customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setEmail("john@example.com");
        customer1.setPhone("123-456-7890");
        entityManager.persist(customer1);

        customer2 = new Customer();
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setEmail("jane@example.com");
        customer2.setPhone("987-654-3210");
        entityManager.persist(customer2);

        // Create addresses
        address1 = new Address();
        address1.setAddressLine1("123 Main St");
        address1.setCity("Springfield");
        address1.setState("IL");
        address1.setZipCode("62701");
        address1.setCountry("USA");
        entityManager.persist(address1);

        address2 = new Address();
        address2.setAddressLine1("456 Oak Ave");
        address2.setCity("Chicago");
        address2.setState("IL");
        address2.setZipCode("60601");
        address2.setCountry("USA");
        entityManager.persist(address2);

        // Create carts with items
        cart1 = new Cart();
        cart1.setId("cart1");
        cart1.setPersonId("person1");
        cart1.setItems(new ArrayList<>());
        entityManager.persist(cart1);

        item1 = new Item();
        item1.setId(1L);
        item1.setCart(cart1);
        item1.setCardId("card1");
        item1.setName("Test Card 1");
        item1.setPrice(10.0);
        item1.setQuantity(2);
        cart1.getItems().add(item1);
        entityManager.persist(item1);

        cart2 = new Cart();
        cart2.setId("cart2");
        cart2.setPersonId("person2");
        cart2.setItems(new ArrayList<>());
        entityManager.persist(cart2);

        item2 = new Item();
        item2.setId(2L);
        item2.setCart(cart2);
        item2.setCardId("card2");
        item2.setName("Test Card 2");
        item2.setPrice(15.0);
        item2.setQuantity(1);
        cart2.getItems().add(item2);
        entityManager.persist(item2);

        // Create orders
        order1 = new CardOrder();
        order1.setCustomer(customer1);
        order1.setCart(cart1);
        order1.setShippingAddress(address1);
        order1.setOrderDate(new Date());
        order1.setConfirmationSent(false);
        order1.setShipMethod("Standard");
        order1.setOrderNotes("Test order 1");
        order1.setSubtotal(100.0);
        order1.setTax(10.0);
        order1.setTotal(110.0);
        entityManager.persist(order1);

        order2 = new CardOrder();
        order2.setCustomer(customer2);
        order2.setCart(cart2);
        order2.setShippingAddress(address2);
        order2.setOrderDate(new Date());
        order2.setConfirmationSent(true);
        order2.setShipMethod("Express");
        order2.setOrderNotes("Test order 2");
        order2.setSubtotal(200.0);
        order2.setTax(20.0);
        order2.setTotal(220.0);
        entityManager.persist(order2);

        entityManager.flush();
    }

    @Test
    void testSaveAndFindById() {
        // Save a new order
        CardOrder savedOrder = orderRepository.save(order1);
        
        // Verify basic properties
        assertThat(savedOrder).isNotNull();
        Long savedOrderId = savedOrder.getId();
        assertThat(savedOrderId).isNotNull();
        assertThat(savedOrder.getCustomer()).isEqualTo(customer1);
        assertThat(savedOrder.getCart()).isEqualTo(cart1);
        assertThat(savedOrder.getShippingAddress()).isEqualTo(address1);
        
        // Verify relationships
        assertThat(savedOrder.getCart().getItems()).hasSize(1);
        assertThat(savedOrder.getCart().getItems().get(0).getName()).isEqualTo("Test Card 1");
    }

    @Test
    void testFindAll() {
        List<CardOrder> orders = orderRepository.findAll();
        assertThat(orders).hasSize(2);
        
        // Verify order details
        CardOrder firstOrder = orders.stream()
            .filter(o -> o.getCustomer().getFirstName().equals("John"))
            .findFirst()
            .orElseThrow();
        
        assertThat(firstOrder.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(firstOrder.getCustomer().getLastName()).isEqualTo("Doe");
        assertThat(firstOrder.getCart().getItems()).hasSize(1);
    }

    @Test
    void testDelete() {
        // Get the ID before deleting
        Long orderIdToDelete = order1.getId();
        
        // Store cart info before deletion
        int itemsCount = order2.getCart().getItems().size();
        
        // Delete an order
        orderRepository.deleteById(orderIdToDelete);
        entityManager.flush();
        entityManager.clear(); // Clear persistence context to ensure fresh data
        
        // Verify the order is deleted
        Optional<CardOrder> deletedOrder = orderRepository.findById(orderIdToDelete);
        assertThat(deletedOrder).isEmpty();
        
        // Verify only one order remains
        List<CardOrder> remainingOrders = orderRepository.findAll();
        assertThat(remainingOrders).hasSize(1);
        
        // Compare by ID instead of the whole object to avoid circular reference issues
        assertThat(remainingOrders.get(0).getId()).isEqualTo(order2.getId());
        
        // Verify the second cart and its items still exist
        Cart remainingCart = entityManager.find(Cart.class, order2.getCart().getId());
        assertThat(remainingCart).isNotNull();
        assertThat(remainingCart.getItems()).hasSize(itemsCount);
    }

    @Test
    void testUpdateOrder() {
        // Get ID for later retrieval
        Long orderIdToUpdate = order1.getId();
        
        // Update order properties
        order1.setShipMethod("Express");
        order1.setOrderNotes("Updated notes");
        order1.setConfirmationSent(true);
        
        // Save the updated order
        orderRepository.save(order1);
        entityManager.flush();
        
        // Verify the updates
        CardOrder retrieved = orderRepository.findById(orderIdToUpdate).orElseThrow();
        assertThat(retrieved.getShipMethod()).isEqualTo("Express");
        assertThat(retrieved.getOrderNotes()).isEqualTo("Updated notes");
        assertThat(retrieved.isConfirmationSent()).isTrue();
        
        // Verify relationships remain unchanged
        assertThat(retrieved.getCart()).isEqualTo(cart1);
        assertThat(retrieved.getCustomer()).isEqualTo(customer1);
        assertThat(retrieved.getShippingAddress()).isEqualTo(address1);
    }
} 