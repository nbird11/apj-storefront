package edu.byui.apj.storefront.db.repository;

import edu.byui.apj.storefront.db.model.CardOrder;
import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    private Cart cart1;
    private Cart cart2;
    private Item item1;
    private Item item2;
    private CardOrder order1;

    @BeforeEach
    void setUp() {
        // Create test carts
        cart1 = new Cart();
        cart1.setId("cart1");
        cart1.setPersonId("person1");
        cart1.setItems(new ArrayList<>());
        entityManager.persist(cart1);

        cart2 = new Cart();
        cart2.setId("cart2");
        cart2.setPersonId("person2");
        cart2.setItems(new ArrayList<>());
        entityManager.persist(cart2);

        // Create test items
        item1 = new Item();
        item1.setId(1L);
        item1.setCart(cart1);
        item1.setCardId("card1");
        item1.setName("Test Card 1");
        item1.setPrice(10.0);
        item1.setQuantity(1);
        cart1.getItems().add(item1);
        entityManager.persist(item1);

        item2 = new Item();
        item2.setId(2L);
        item2.setCart(cart1);
        item2.setCardId("card2");
        item2.setName("Test Card 2");
        item2.setPrice(20.0);
        item2.setQuantity(2);
        cart1.getItems().add(item2);
        entityManager.persist(item2);

        // Create an order for cart1
        order1 = new CardOrder();
        order1.setCart(cart1);
        entityManager.persist(order1);

        entityManager.flush();
    }

    @Test
    void testSaveAndFindById() {
        // Save a new cart
        Cart savedCart = cartRepository.save(cart1);
        
        // Verify basic properties
        assertThat(savedCart).isNotNull();
        assertThat(savedCart.getId()).isEqualTo("cart1");
        assertThat(savedCart.getPersonId()).isEqualTo("person1");
        
        // Verify items relationship
        assertThat(savedCart.getItems()).hasSize(2);
        assertThat(savedCart.getItems()).contains(item1, item2);
        
        // Verify bidirectional relationship
        assertThat(savedCart.getItems().get(0).getCart()).isEqualTo(savedCart);
        assertThat(savedCart.getItems().get(1).getCart()).isEqualTo(savedCart);
    }

    @Test
    void testFindAll() {
        List<Cart> carts = cartRepository.findAll();
        assertThat(carts).hasSize(2);
        assertThat(carts).contains(cart1, cart2);
        
        // Verify the carts are distinct
        Cart firstCart = carts.stream().filter(c -> c.getId().equals("cart1")).findFirst().orElseThrow();
        Cart secondCart = carts.stream().filter(c -> c.getId().equals("cart2")).findFirst().orElseThrow();
        
        assertThat(firstCart.getItems()).hasSize(2);
        assertThat(secondCart.getItems()).isEmpty();
    }

    @Test
    void testFindCartsWithoutOrders() {
        List<Cart> cartsWithoutOrders = cartRepository.findCartsWithoutOrders();
        assertThat(cartsWithoutOrders).hasSize(1);
        assertThat(cartsWithoutOrders).contains(cart2);
        assertThat(cartsWithoutOrders).doesNotContain(cart1);
    }

    @Test
    void testDelete() {
        // First, remove the order associated with cart1
        entityManager.remove(order1);
        entityManager.flush();

        // Then delete the cart
        cartRepository.deleteById("cart1");
        entityManager.flush();

        // Verify the cart is deleted
        Optional<Cart> deletedCart = cartRepository.findById("cart1");
        assertThat(deletedCart).isEmpty();
        
        // Verify only one cart remains
        List<Cart> remainingCarts = cartRepository.findAll();
        assertThat(remainingCarts).hasSize(1);
        assertThat(remainingCarts).contains(cart2);
        
        // Verify items were also deleted (due to CascadeType.ALL)
        List<Item> items = entityManager.getEntityManager()
            .createQuery("SELECT i FROM Item i", Item.class)
            .getResultList();
        assertThat(items).isEmpty();
    }

    @Test
    void testFindById_NonExistent() {
        Optional<Cart> nonExistent = cartRepository.findById("non-existent-id");
        assertThat(nonExistent).isEmpty();
    }

    @Test
    void testUpdateCart() {
        // Modify cart properties
        cart1.setPersonId("updated-person");
        
        // Add a new item
        Item newItem = new Item();
        newItem.setId(3L);
        newItem.setCart(cart1);
        newItem.setCardId("card3");
        newItem.setName("Test Card 3");
        newItem.setPrice(30.0);
        newItem.setQuantity(3);
        cart1.getItems().add(newItem);
        entityManager.persist(newItem);
        
        // Save the updated cart
        cartRepository.save(cart1);
        entityManager.flush();
        
        // Verify the updates
        Cart retrieved = cartRepository.findById("cart1").orElseThrow();
        assertThat(retrieved.getPersonId()).isEqualTo("updated-person");
        assertThat(retrieved.getItems()).hasSize(3);
        assertThat(retrieved.getItems()).contains(item1, item2, newItem);
        
        // Verify all items still reference this cart
        assertThat(retrieved.getItems()).allMatch(item -> item.getCart().equals(retrieved));
    }
} 