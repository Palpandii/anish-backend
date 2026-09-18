package com.ascrackers.backend.repository;

import com.ascrackers.backend.model.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
}
