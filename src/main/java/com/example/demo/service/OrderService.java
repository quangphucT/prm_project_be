package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.NotEnoughException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.OrderDetailsRequest;
import com.example.demo.model.OrderRequest;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductVariantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductVariantsRepository productVariantsRepository;
    public Orders createOrder(OrderRequest orderRequest){
        Account customer = authenticationService.getCurrentAccount();
           Orders orders = new Orders();
        orders.setCreatedAt(new Date());
        orders.setCustomer(customer);
           float totalPrice = 0;
           List<OrderDetails> orderDetailsList = new ArrayList<>();

           for (OrderDetailsRequest req : orderRequest.getOrderDetails()){
               ProductVariants variant = productVariantsRepository
                       .findFirstByProductIdAndColorIdAndSizeIdAndIsDeletedFalse(req.getProductId(), req.getColorId(), req.getSizeId())
                       .orElseThrow(() -> new NotFoundException("Không tìm thấy biến thể sản phẩm"));


               if (variant.getStock() < req.getQuantity()) {
                   throw new NotEnoughException("Không đủ tồn kho cho sản phẩm " + variant.getProduct().getName());
               }
               variant.setStock(variant.getStock() - req.getQuantity());
               productVariantsRepository.save(variant);

               OrderDetails orderDetail = new OrderDetails();
               orderDetail.setProduct(variant.getProduct());
               orderDetail.setColor(variant.getColor());
               orderDetail.setSize(variant.getSize());
               orderDetail.setQuantity(req.getQuantity());
               orderDetail.setPrice(variant.getProduct().getPrice()); // hoặc variant-specific price nếu có
               orderDetail.setOrders(orders);

               totalPrice += variant.getProduct().getPrice() * req.getQuantity();
               orderDetailsList.add(orderDetail);
           }

         orders.setOrderDetails(orderDetailsList);
           orders.setTotal(totalPrice);
           return orderRepository.save(orders);
    }
    public List<Orders> getOrders(){
        Account customer = authenticationService.getCurrentAccount();
        List<Orders> ordersList = orderRepository.findOrdersByCustomer(customer);
        return ordersList;
    }
}
