package com.epam.esm.contoller;

import com.epam.esm.auth.UserPrincipal;
import com.epam.esm.dto.CreateActionHypermedia;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.exception.UnknownPrincipalException;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.builder.CreateHypermediaLinkBuilder;
import com.epam.esm.util.builder.OrderLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";

    @Autowired
    private OrderService service;

    @GetMapping("/")
    public ResponseEntity<?> retrieveOrders(@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
                                            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                            @RequestParam(required = false) Long idUser,
                                            @RequestParam(required = false) Long idOrder) {
        List<OrderDTO> resultList;
        if (idOrder != null && idUser != null) {
            return retrieveOrderOfUser(idOrder, idUser);
        }
        if (idUser != null) {
            resultList = service.findOrdersOfUser(idUser, limit, page);
        } else {
            resultList = service.findAll(limit, page);
        }
        for (int i = 0; i < resultList.size(); i++) {
            OrderLinkBuilder builder = new OrderLinkBuilder(resultList.get(i));
            builder.buildCertificateReferenceLink().buildUserReferenceLink().buildRetrieveSpecificOrderLink();
            resultList.set(i, builder.getHypermedia());
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveSpecificOrder(@PathVariable long id) {
        OrderDTO order = service.find(id);
        OrderLinkBuilder builder = new OrderLinkBuilder(order);
        builder.buildUserReferenceLink().buildCertificateReferenceLink();
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewOrder(@RequestBody OrderDTO order) {
        long result = service.create(order);
        CreateHypermediaLinkBuilder builder = new CreateHypermediaLinkBuilder(new CreateActionHypermedia(result));
        builder.buildNewOrderLink(result);
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<?> retrieveMyOrders(@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
                                              @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(required = false) Long idOrder) {
        Object me = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (me instanceof UserPrincipal) {
            if (idOrder != null) {
                return retrieveOrderOfUser(idOrder, ((UserPrincipal) me).getId());
            }
            return retrieveOrders(limit, page, ((UserPrincipal) me).getId(), 0L);
        } else {
            throw new UnknownPrincipalException("principal of " + me.getClass() + " is unknown");
        }
    }

    private ResponseEntity<?> retrieveOrderOfUser(long idOrder, long idUser) {
        OrderDTO order = service.findSpecificOrderOfUser(idUser, idOrder);
        OrderLinkBuilder builder = new OrderLinkBuilder(order);
        builder.buildUserReferenceLink().buildCertificateReferenceLink();
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
    }

}
