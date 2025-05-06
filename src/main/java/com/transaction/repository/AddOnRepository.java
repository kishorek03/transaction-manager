package com.transaction.repository;

import com.transaction.entity.AddOn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddOnRepository extends JpaRepository<AddOn, Long> {
}
