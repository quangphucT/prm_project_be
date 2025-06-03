package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.exception.DuplicationException;
import com.example.demo.model.AccResponseAfterLogin;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;
    public AccountResponse register(RegisterRequest registerRequest) {
        Account newAcc = modelMapper.map(registerRequest, Account.class);
        try {
              newAcc.setCreatedAt(new Date());
              newAcc.setPassword(passwordEncoder.encode(newAcc.getPassword()));
              accountRepository.save(newAcc);
              AccountResponse accountResponse = modelMapper.map(newAcc, AccountResponse.class);
              accountResponse.setMessage("Account created");
              return accountResponse;
        } catch (Exception e) {
            if(e.getMessage().contains(newAcc.getEmail())){
                throw new DuplicationException("Email already in use!");
            }else{
                throw new DuplicationException("Phone already in use!");
            }
        }
    }
    public AccResponseAfterLogin login(LoginRequest loginRequest) {
        try {
          Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
            Account account = (Account) authentication.getPrincipal();

            AccResponseAfterLogin accountResponseAfterLogin = modelMapper.map(account, AccResponseAfterLogin.class);
            accountResponseAfterLogin.setToken(tokenService.generateToken(account));
            accountResponseAfterLogin.setMessage("Login successful!");
            return accountResponseAfterLogin;
        } catch (Exception e) {
            throw new EntityNotFoundException("Invalid email or password!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findAccountByEmail(email);
    }
}
