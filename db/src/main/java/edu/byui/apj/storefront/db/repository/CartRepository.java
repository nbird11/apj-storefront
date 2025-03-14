package edu.byui.apj.storefront.db.repository;

import edu.byui.apj.storefront.db.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin(origins = "*")
//@RepositoryRestResource(collectionResourceRel = "cart", path = "cart")
public interface CartRepository extends JpaRepository<Cart, String> {
}
