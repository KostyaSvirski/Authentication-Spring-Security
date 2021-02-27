package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;

public interface OrderService extends BaseService<OrderDTO> {

    long create(OrderDTO newOrder);
}
