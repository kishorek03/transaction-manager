package com.transaction.repository;

import com.transaction.entity.Flavour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlavourRepository extends JpaRepository<Flavour, Long> {
}
