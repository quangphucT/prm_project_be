package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.AddToCart;
import com.example.demo.model.UpdateCartItem;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    public Cart addCart(AddToCart addToCart) {
        Account account = authenticationService.getCurrentAccount();

        // Tìm cart hiện tại của user
        Cart cart = cartRepository.findByAccount(account)
                .orElse(null);

        if (cart == null) {
            cart = new Cart();
            cart.setAccount(account);
            cart.setCartItems(new ArrayList<>());
        }


            Product product = productRepository.findById((int) addToCart.getProductId()).orElseThrow();
            Color color = colorRepository.findById(addToCart.getColorId()).orElseThrow();
            Size size = sizeRepository.findById(addToCart.getSizeId()).orElseThrow();

            // Kiểm tra sản phẩm đã có trong giỏ chưa
            CartItem existingItem = cart.getCartItems().stream()
                    .filter(item ->
                            item.getProduct().getId() == product.getId() &&
                                    item.getColor().getId() == color.getId() &&
                                    item.getSize().getId() == size.getId()
                    )
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                // Nếu có rồi => cập nhật số lượng
                existingItem.setQuantity(existingItem.getQuantity() + addToCart.getQuantity());
            } else {
                // Nếu chưa => tạo mới CartItem
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setColor(color);
                cartItem.setSize(size);
                cartItem.setQuantity(addToCart.getQuantity());
                cartItem.setCart(cart);

                cart.getCartItems().add(cartItem);
            }


        return cartRepository.save(cart);
    }

    public Cart getCart() {
        Account account = authenticationService.getCurrentAccount();
        return cartRepository.findByAccount(account)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
    }
   public void updateItemCart(long id, UpdateCartItem updateCartItem) {
       CartItem cartItem = cartItemRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Cart item not found"));


       Color color = colorRepository.findById(updateCartItem.getColorId())
               .orElseThrow(() -> new NotFoundException("Color not found"));

       Size size = sizeRepository.findById(updateCartItem.getSizeId())
               .orElseThrow(() -> new NotFoundException("Size not found"));

       cartItem.setColor(color);
       cartItem.setSize(size);
       cartItem.setQuantity(updateCartItem.getQuantity());
        cartItemRepository.save(cartItem);

   }
    @Transactional
   public void deleteCartItem(long id){
       // Kiểm tra cart tồn tại
       Cart cart = cartRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Cart not found"));
       cartItemRepository.deleteAllByCartId(id);
   }
    @Transactional
    public void deleteEachCartItem(long id){

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CartItem not found"));

        // Lấy người dùng hiện tại
        Account currentAccount = authenticationService.getCurrentAccount(); // hoặc SecurityContextHolder...

        // So sánh chủ sở hữu
        if (!Long.valueOf(cartItem.getCart().getAccount().getId())
                .equals((currentAccount.getId()))) {
            throw new UnauthorizedException("Unauthorized to delete!!");
        }

        cartItemRepository.deleteByIdCustom(id);
    }

}
