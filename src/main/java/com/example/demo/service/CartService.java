package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.AddToCart;
import com.example.demo.model.ItemCart;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

        for (ItemCart req : addToCart.getCartItems()) {
            Product product = productRepository.findById((int) req.getProductId()).orElseThrow();
            Color color = colorRepository.findById(req.getColorId()).orElseThrow();
            Size size = sizeRepository.findById(req.getSizeId()).orElseThrow();

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
                existingItem.setQuantity(existingItem.getQuantity() + req.getQuantity());
            } else {
                // Nếu chưa => tạo mới CartItem
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setColor(color);
                cartItem.setSize(size);
                cartItem.setQuantity(req.getQuantity());
                cartItem.setCart(cart);

                cart.getCartItems().add(cartItem);
            }
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public void removeCart(long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        // Xóa liên kết cart ra khỏi account
        Account account = cart.getAccount();
        if (account != null) {
            account.setCart(null); // NGẮT liên kết
        }

        cartRepository.delete(cart);
    }
    public Cart getCart() {
        Account account = authenticationService.getCurrentAccount();
        return cartRepository.findByAccount(account)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
    }
   public void updateItemCart(long id, ItemCart itemCart) {
       CartItem cartItem = cartItemRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Cart item not found"));

       Product product = productRepository.findById((int) itemCart.getProductId())
               .orElseThrow(() -> new NotFoundException("Product not found"));

       Color color = colorRepository.findById(itemCart.getColorId())
               .orElseThrow(() -> new NotFoundException("Color not found"));

       Size size = sizeRepository.findById(itemCart.getSizeId())
               .orElseThrow(() -> new NotFoundException("Size not found"));

       cartItem.setProduct(product);
       cartItem.setColor(color);
       cartItem.setSize(size);
       cartItem.setQuantity(itemCart.getQuantity());
        cartItemRepository.save(cartItem);

   }
}
