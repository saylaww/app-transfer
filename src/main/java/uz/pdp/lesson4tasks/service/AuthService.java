package uz.pdp.lesson4tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson4tasks.dto.LoginDto;
import uz.pdp.lesson4tasks.dto.RegisterDto;
import uz.pdp.lesson4tasks.dto.Response;
import uz.pdp.lesson4tasks.entity.User;
import uz.pdp.lesson4tasks.repository.UserRepository;
import uz.pdp.lesson4tasks.security.JwtProvider;

@Service
public class AuthService implements UserDetailsService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtProvider jwtProvider;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Lazy AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }


    public Response register(RegisterDto registerDto) {
        final boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());

        if (existsByEmail) {
            return new Response("Email already exists", false);
        }

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        user.setEnabled(true);

        userRepository.save(user);

        return new Response("Registered Successfully", true);
    }

    public Response login(LoginDto loginDto) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            final String token = jwtProvider.generateToken(loginDto.getUsername());
            return new Response("Token", true, token);
        } catch (BadCredentialsException e) {
            return new Response("Login or password incorrect", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(s + " username not found"));
    }
}
