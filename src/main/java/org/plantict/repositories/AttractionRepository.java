package org.plantict.repositories;

import org.plantict.entities.AttractionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttractionRepository extends JpaRepository<AttractionEntity, UUID> {

    Optional<AttractionEntity> findByName(@Param("name") String name);
}
