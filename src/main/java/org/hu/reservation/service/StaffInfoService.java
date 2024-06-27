package org.hu.reservation.service;

import org.hu.reservation.entity.StaffInfo;
import org.hu.reservation.entity.StaffInfoDetails;
import org.hu.reservation.repository.StaffInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffInfoService implements UserDetailsService {

    @Autowired
    private StaffInfoRepository repository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<StaffInfo> staffInfo = repository.findByName(username);

        return staffInfo.map(StaffInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public int addStaffMemeber(StaffInfo staffInfo) {
        staffInfo.setPassword(encoder.encode(staffInfo.getPassword()));
        return repository.save(staffInfo).getId();
    }

    public PasswordEncoder getEncoder() {
        return encoder;
    }
}
