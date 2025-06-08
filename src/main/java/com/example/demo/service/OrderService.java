package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.model.OrderDetailsRequest;
import com.example.demo.model.OrderRequest;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
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
    public Orders createOrder(OrderRequest orderRequest){
        Account customer = authenticationService.getCurrentAccount();
           Orders orders = new Orders();
           float totalPrice = 0;
           List<OrderDetails> orderDetailsList = new ArrayList<>();
           orders.setCreatedAt(new Date());
           orders.setCustomer(customer);
           for (OrderDetailsRequest orderDetailsRequest : orderRequest.getOrderDetails()){
                       OrderDetails orderDetail = new OrderDetails();
               Product product = productRepository.findProductById(orderDetailsRequest.getProductId());
                      orderDetail.setPrice(product.getPrice());
                      orderDetail.setQuantity(orderDetailsRequest.getQuantity());
                      orderDetail.setProduct(product);
                      orderDetail.setOrders(orders);
                       orderDetailsList.add(orderDetail);
               totalPrice += product.getPrice() * orderDetailsRequest.getQuantity();
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
