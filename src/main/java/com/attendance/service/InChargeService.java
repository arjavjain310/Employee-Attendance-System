package com.attendance.service;

import com.attendance.entity.InCharge;
import com.attendance.repository.InChargeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InChargeService {

    private final InChargeRepository inChargeRepository;

    public InChargeService(InChargeRepository inChargeRepository) {
        this.inChargeRepository = inChargeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<InCharge> findByUserId(String userId) {
        return inChargeRepository.findByUserIdAndActiveTrue(userId);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserId(String userId) {
        return inChargeRepository.existsByUserId(userId);
    }

    @Transactional
    public InCharge register(InCharge inCharge) {
        return inChargeRepository.save(inCharge);
    }
}
