package mate.academy.repository.order;

import java.util.List;
import mate.academy.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId, Pageable pageable);
}
