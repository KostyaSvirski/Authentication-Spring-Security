package com.epam.esm.service.impl;

import com.epam.esm.auth.UserPrincipal;
import com.epam.esm.converter.OrderDTOToOrderEntityConverter;
import com.epam.esm.converter.OrderEntityToOrderDTOConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.UnknownPrincipalException;
import com.epam.esm.hibernate.OrderRepository;
import com.epam.esm.persistence.OrderEntity;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.PreparedValidatorChain;
import com.epam.esm.validator.realisation.IntermediateOrderLink;
import com.epam.esm.validator.realisation.order.IdCertificateValidatorLink;
import com.epam.esm.validator.realisation.order.IdUserValidatorLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderEntityToOrderDTOConverter toOrderDTOConverter;
    private final OrderDTOToOrderEntityConverter toOrderEntityConverter;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, OrderEntityToOrderDTOConverter toOrderDTOConverter,
                            OrderDTOToOrderEntityConverter toOrderEntityConverter) {
        this.repository = repository;
        this.toOrderDTOConverter = toOrderDTOConverter;
        this.toOrderEntityConverter = toOrderEntityConverter;
    }

    @Override
    public List<OrderDTO> findAll(int limit, int page) {
        List<OrderEntity> listFromDao = repository.findAll(limit, page);
        return listFromDao.stream().map(toOrderDTOConverter)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO find(long id) throws EntityNotFoundException {
        Optional<OrderEntity> order = repository.find(id);
        if (!order.isPresent()) {
            throw new EntityNotFoundException("order with id " + id + " not found");
        }
        return toOrderDTOConverter.apply(order.get());
    }

    @Override
    public long create(OrderDTO newOrder) {
        Object me = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (me instanceof UserPrincipal) {
            newOrder.setIdUser(((UserPrincipal) me).getId());
            newOrder.setPurchaseTime(LocalDateTime.now().toString());
            PreparedValidatorChain<OrderDTO> chain = new IntermediateOrderLink();
            chain.linkWith(new IdCertificateValidatorLink()).linkWith(new IdUserValidatorLink());
            if (chain.validate(newOrder)) {
                return repository.create(toOrderEntityConverter.apply(newOrder));
            }
        } else {
            throw new UnknownPrincipalException("principal of " + me.getClass() + " is unknown");
        }
        return 0;
    }

    @Override
    public List<OrderDTO> findOrdersOfUser(long idUser, int limit, int page) {
        List<OrderEntity> listFromDao = repository.findOrdersOfSpecificUser(idUser, limit, page);
        return listFromDao.stream().map(toOrderDTOConverter)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findSpecificOrderOfUser(long idUser, long idOrder) {
        List<OrderEntity> listFromDao = repository.findOrderOfSpecificUser(idUser, idOrder);
        return listFromDao.stream().map(toOrderDTOConverter)
                .findFirst().orElseThrow(() ->
                        new EntityNotFoundException("order with id " + idOrder + " of user " + idUser + " not found"));
    }
}
