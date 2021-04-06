package main.security;

import main.exception.UserNotFoundException;
import main.model.User;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Qualifier("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);

        return new SecurityUser(
                user.getEmail(),
                user.getPassword(),
                user.getRole().getGrantedAuthorities()
        );

//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                true, true, true, true,
//                user.getRole().getGrantedAuthorities()
//        );
    }

    public User getUserFromContextHolder() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user;
        try {
            user = userService.getUserByEmail(email);
        } catch (UserNotFoundException ex) {
            user = new User();
        }
        return user;
    }
}
