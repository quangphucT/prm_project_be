package com.example.demo.service;


import com.example.demo.entity.Account;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.UpdateAccountRequest;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
     public AccountResponse getAccountById(long id){
         Account acc = accountRepository.findAccountById(id);
         if(acc == null) {
             throw new NotFoundException("Account not found");
         }else{
             AccountResponse accountResponse = new AccountResponse();

             accountResponse.setId(id);
             accountResponse.setFirstName(acc.getFirstName());
             accountResponse.setLastName(acc.getLastName());
             accountResponse.setEmail(acc.getEmail());
             accountResponse.setPhone(acc.getPhone());
             accountResponse.setAvatar(acc.getAvatar());
             accountResponse.setCreatedAt(acc.getCreatedAt());
             return accountResponse;
         }
     }
     public AccountResponse updateAccount(long id, UpdateAccountRequest updateAccountRequest){
           Account acc = accountRepository.findAccountById(id);
           if(acc == null){
               throw new NotFoundException("Account not found");
           }else{
               acc.setFirstName(updateAccountRequest.getFirstName());
               acc.setLastName(updateAccountRequest.getLastName());
               acc.setPhone(updateAccountRequest.getPhone());
               acc.setAvatar(updateAccountRequest.getAvatar());
               Account updatedAcc = accountRepository.save(acc);
               AccountResponse accountResponse = new AccountResponse();

               accountResponse.setId(id);
               accountResponse.setFirstName(updatedAcc.getFirstName());
               accountResponse.setLastName(updatedAcc.getLastName());
               accountResponse.setEmail(updatedAcc.getEmail());
               accountResponse.setPhone(updatedAcc.getPhone());
               accountResponse.setAvatar(updatedAcc.getAvatar());
               accountResponse.setCreatedAt(updatedAcc.getCreatedAt());
               return accountResponse;
           }



     }
}
