package moe.hareru.mioe.security;

import moe.hareru.mioe.entity.UserEntity;
import moe.hareru.mioe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUserById(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("This username didn't exist.");
        }
        return new User(userEntity.getUserIdStr(), userEntity.getPassword(), userEntity.getRoleList());
    }

    public Long getLastRevokeMs(Long userId){
        return userService.getRevokeTimeMs(userId);
    }
}
