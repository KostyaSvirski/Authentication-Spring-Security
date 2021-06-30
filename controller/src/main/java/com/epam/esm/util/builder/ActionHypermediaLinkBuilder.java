package com.epam.esm.util.builder;

import com.epam.esm.contoller.GiftCertificateController;
import com.epam.esm.contoller.TagController;
import com.epam.esm.dto.ActionHypermedia;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class ActionHypermediaLinkBuilder extends BuilderContainer<ActionHypermedia> {

    public ActionHypermediaLinkBuilder(ActionHypermedia hypermedia) {
        super(hypermedia);
    }

    public ActionHypermediaLinkBuilder buildRetrieveAllCertificateSelfLink
            (int limit, int page, String partOfName, String partOfDescription,
             String nameOfTag, String field, String method) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .retrieveCertificates(limit, page, partOfName, partOfDescription, nameOfTag, field,
                                method))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveSpecificCertificateLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .findSpecificCertificate(id))
                .withRel("certificate"));
        return this;
    }


    public ActionHypermediaLinkBuilder buildFindAllTagsLink(int limit, int page) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .findAllTags(limit, page))
                .withRel("tags"));
        return this;
    }
}
