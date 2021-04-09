package main.security;

import main.exception.UserNotFoundException;
import main.model.User;
import main.repo.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Qualifier("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

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
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        } catch (UserNotFoundException ex) {
            user = new User();
        }
        return user;
    }
}
