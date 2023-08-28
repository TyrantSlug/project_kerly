package kr.co.tj.newOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewOrderRepository extends JpaRepository<NewOrderEntity, Long>{

	Page<NewOrderEntity> findByUsername(String username, Pageable pageable);

	Page<NewOrderEntity> findById(Long id, Pageable pageable);

}
