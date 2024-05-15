package com.nordnet.orderbook.repositories;

import com.nordnet.orderbook.models.Order;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {

}