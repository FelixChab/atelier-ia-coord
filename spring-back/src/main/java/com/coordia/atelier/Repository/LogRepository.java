package com.coordia.atelier.Repository;

import com.coordia.atelier.Entity.InteractionLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LogRepository extends CrudRepository<InteractionLog, UUID> {

    @Query("SELECT l FROM InteractionLog l ORDER BY l.timestamp DESC")
    List<InteractionLog> findAllByOrderByTimestampDesc();

    List<InteractionLog> findBySuccessTrue();

    List<InteractionLog> findBySuccessFalse();

    long countBySuccessTrue();

    long countBySuccessFalse();
}