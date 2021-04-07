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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private OrderService service;

    @GetMapping("/")
    public ResponseEntity<?> retrieveOrders(@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
                                            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                            @RequestParam(required = false) Long idUser,
                                            @RequestParam(required = false) Long idOrder) {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();
        if (authorities.stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ROLE_ADMIN))) {
            return retrieveMyOrders(limit, page, idOrder);
        }
        List<OrderDTO> resultList;
        if (idOrder != null && idUser != null) {
            return retrieveOrderOfUser(idOrder, idUser);
        }
        if (idUser != null) {
            resultList = service.findOrdersOfUser(idUser, limit, page);
        } else {
            resultList = service.findAll(limit, page);
        }
        return getResponseEntity(resultList);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveSpecificOrder(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ROLE_ADMIN))) {
            return retrieveOrderOfUser(id, ((UserPrincipal) authentication.getPrincipal()).getId());
        }
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

    private ResponseEntity<?> retrieveMyOrders(@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
                                               @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                               @RequestParam(required = false) Long idOrder) {
        Object me = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (me instanceof UserPrincipal) {
            if (idOrder != null) {
                return retrieveOrderOfUser(idOrder, ((UserPrincipal) me).getId());
            }
            List<OrderDTO> resultList = service.findOrdersOfUser(((UserPrincipal) me).getId(), limit, page);
            return getResponseEntity(resultList);
        } else {
            throw new UnknownPrincipalException("principal of " + me.getClass() + " is unknown");
        }
    }

    private ResponseEntity<?> getResponseEntity(List<OrderDTO> resultList) {
        for (int i = 0; i < resultList.size(); i++) {
            OrderLinkBuilder builder = new OrderLinkBuilder(resultList.get(i));
            builder.buildCertificateReferenceLink().buildUserReferenceLink().buildRetrieveSpecificOrderLink();
            resultList.set(i, builder.getHypermedia());
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    private ResponseEntity<?> retrieveOrderOfUser(long idOrder, long idUser) {
        OrderDTO order = service.findSpecificOrderOfUser(idUser, idOrder);
        OrderLinkBuilder builder = new OrderLinkBuilder(order);
        builder.buildUserReferenceLink().buildCertificateReferenceLink();
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
    }

}
