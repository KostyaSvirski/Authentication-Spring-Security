package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;

import java.util.List;

public interface OrderService extends BaseService<OrderDTO> {

    long create(OrderDTO newOrder);

    List<OrderDTO> findOrdersOfUser(long idUser, int limit, int page);

    OrderDTO findSpecificOrderOfUser(long idUser, long idOrder);
}
