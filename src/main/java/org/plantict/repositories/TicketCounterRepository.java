package org.plantict.repositories;

import org.plantict.entities.TicketCounterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketCounterRepository extends JpaRepository<TicketCounterEntity, Long> {
}
