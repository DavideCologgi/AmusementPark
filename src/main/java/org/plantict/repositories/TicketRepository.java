package org.plantict.repositories;

import org.plantict.entities.AttractionEntity;
import org.plantict.entities.TicketEntity;
import org.plantict.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    Optional<TicketEntity>  findByNumber(@Param("number") Long number);

    List<TicketEntity>      findByAttractionAndUser(AttractionEntity attraction, UserEntity user);
}
