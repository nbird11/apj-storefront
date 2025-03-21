package edu.byui.apj.storefront.db.repository;

import edu.byui.apj.storefront.db.model.CardOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CardOrder, Long> {
}
