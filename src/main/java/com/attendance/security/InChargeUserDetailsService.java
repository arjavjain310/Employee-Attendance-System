package com.attendance.security;

import com.attendance.entity.InCharge;
import com.attendance.service.InChargeService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class InChargeUserDetailsService implements UserDetailsService {

    private final InChargeService inChargeService;

    public InChargeUserDetailsService(InChargeService inChargeService) {
        this.inChargeService = inChargeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        InCharge inCharge = inChargeService.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new User(
                inCharge.getUserId(),
                inCharge.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_INCHARGE"))
        );
    }
}
