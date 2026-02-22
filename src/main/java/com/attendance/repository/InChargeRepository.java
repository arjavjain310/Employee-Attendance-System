package com.attendance.repository;

import com.attendance.entity.InCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InChargeRepository extends JpaRepository<InCharge, Long> {

    Optional<InCharge> findByUserId(String userId);

    Optional<InCharge> findByUserIdAndActiveTrue(String userId);

    boolean existsByUserId(String userId);
}
