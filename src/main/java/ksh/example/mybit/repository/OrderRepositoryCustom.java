package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Order;


public interface OrderRepositoryCustom {

    Order findMatchingOrder(Order order);

}
