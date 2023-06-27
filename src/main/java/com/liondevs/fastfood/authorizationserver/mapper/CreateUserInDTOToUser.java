package com.liondevs.fastfood.authorizationserver.mapper;

import com.liondevs.fastfood.authorizationserver.auth.dto.RegisterRequest;
import com.liondevs.fastfood.authorizationserver.constants.Constants;
import com.liondevs.fastfood.authorizationserver.persistence.entity.User;
import com.liondevs.fastfood.authorizationserver.persistence.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class CreateUserInDTOToUser  implements iMapper<RegisterRequest, User>{
    private final PasswordEncoder passwordEncoder;
    @Override
    public User map(RegisterRequest in) {
        User user = new User();
        user.setFirstName(in.getFirstName());
        user.setLastName(in.getLastName());
        user.setEmail(in.getEmail());
        user.setPhone(in.getPhone());
        user.setPassword(passwordEncoder.encode(in.getPassword()));
        if(in.getPlaceId() !=  null)user.setPlaceId(in.getPlaceId());
        user.setRole(assignRole(in.getFrom()));
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(LocalDateTime.now());
        return user;
    }

    public Role assignRole(String from ){
        switch (from){
            case Constants.APP_HANDLER_PLACES -> {
                return Role.ADMIN_PLACE;
            }
            case Constants.SUPER_APP -> {
                return Role.SUPER_ADMIN;
            }
            default -> {
                return Role.USER;
            }
        }

    }
}
