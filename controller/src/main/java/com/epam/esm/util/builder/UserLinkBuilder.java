package com.epam.esm.util.builder;

import com.epam.esm.contoller.OrderController;
import com.epam.esm.contoller.UserController;
import com.epam.esm.dto.UserDTO;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class UserLinkBuilder extends BuilderContainer<UserDTO> {

    public UserLinkBuilder(UserDTO hypermedia) {
        super(hypermedia);
    }

    public UserLinkBuilder buildRetrieveSpecificUserLink() {
        hypermedia.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .retrieveSpecificUser(hypermedia.getId()))
                .withRel("user"));
        return this;
    }

    public UserLinkBuilder buildOrdersReferencesLink(int limit, int page) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .retrieveOrders(limit, page, hypermedia.getId(), 0L))
                .withRel("orders"));
        return this;
    }

}
