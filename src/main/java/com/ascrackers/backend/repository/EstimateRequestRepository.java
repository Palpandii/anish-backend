package com.ascrackers.backend.repository;

import com.ascrackers.backend.model.EstimateRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimateRequestRepository extends JpaRepository<EstimateRequest, Long> {
}