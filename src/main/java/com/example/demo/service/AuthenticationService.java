package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Role;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CartRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    EmailService emailService;
    @Autowired
    EmailResetPasswordService emailResetPasswordService;
    @Autowired
    CartRepository cartRepository;
    public AccountResponse register(RegisterRequest registerRequest) {
        Account newAcc = modelMapper.map(registerRequest, Account.class);
        try {
              newAcc.setCreatedAt(new Date());
              newAcc.setRole(Role.CUSTOMER);
              newAcc.setPassword(passwordEncoder.encode(newAcc.getPassword()));
              Account newAccount =  accountRepository.save(newAcc);
            Account savedAcc = accountRepository.save(newAcc);
            Cart cart = new Cart();
            cart.setAccount(savedAcc);
            cart.setCartItems(null);

            savedAcc.setCart(cart);
            cartRepository.save(cart);
              AccountResponse accountResponse = modelMapper.map(newAccount, AccountResponse.class);
              EmailDetails emailDetails = new EmailDetails();
              emailDetails.setReceiver(newAccount);
              emailDetails.setSubject("Welcome to Sport's shop! We're  Excited to Have You");
              emailDetails.setLink("https://www.facebook.com/quang.phuc.762048");
              emailService.sendMail(emailDetails);
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

            return accountResponseAfterLogin;
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid email or password!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findAccountByEmail(email);
    }

    public Account getCurrentAccount() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return account;
    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        Account account = accountRepository.findAccountByEmail(forgotPasswordRequest.getEmail());
        if(account == null || account.getIsDeleted()){
           throw new NotFoundException("Account not found!");
        }else{
            String token = tokenService.generateToken(account);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setReceiver(account);
            emailDetails.setSubject("Reset your password!!");
            emailDetails.setLink("https://www.facebook.com/quang.phuc.762048/?token=" + token);
            emailResetPasswordService.sendMailResetPassword(emailDetails);
        }

    }
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
           Account account = getCurrentAccount();
           account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
           accountRepository.save(account);
    }
}
