package com.epam.esm.contoller;

import com.epam.esm.dto.ActionHypermedia;
import com.epam.esm.dto.CreateActionHypermedia;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.builder.ActionHypermediaLinkBuilder;
import com.epam.esm.util.builder.CertificateLinkBuilder;
import com.epam.esm.util.builder.CreateHypermediaLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";

    @Autowired
    private GiftCertificateService service;

    @GetMapping(value = "/")
    public ResponseEntity<?> retrieveCertificates
            (@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
             @RequestParam(defaultValue = DEFAULT_PAGE) int page,
             @RequestParam(required = false) String partOfName,
             @RequestParam(required = false) String partOfDescription,
             @RequestParam(required = false) String nameOfTag,
             @RequestParam(required = false) String field,
             @RequestParam(required = false) String direction) {
        List<GiftCertificateDTO> results;
        if (partOfName != null) {
            results = service.findByPartOfName(partOfName, limit, page);
        } else if (partOfDescription != null) {
            results = service.findByPartOfDescription(partOfDescription, limit, page);
        } else if (nameOfTag != null) {
            results = service.findByTag(nameOfTag, limit, page);
        } else if (field != null && direction != null) {
            results = service.sortByField(field, direction, limit, page);
        } else {
            results = service.findAll(limit, page);
        }
        if (results == null || results.isEmpty()) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia("not found"));
            builder.buildRetrieveAllCertificateSelfLink
                    (limit, page, partOfName, partOfDescription, nameOfTag, field, direction);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NOT_FOUND);
        }
        for (int i = 0; i < results.size(); i++) {
            CertificateLinkBuilder builder = new CertificateLinkBuilder(results.get(i));
            builder.buildCertificateHypermedia();
            results.set(i, builder.getHypermedia());
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSpecificCertificate(@PathVariable long id) {
        GiftCertificateDTO result = service.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewCertificate(@RequestBody GiftCertificateDTO certificateDTO) {
        long result = service.create(certificateDTO);
        CreateHypermediaLinkBuilder builder = new CreateHypermediaLinkBuilder(new CreateActionHypermedia(result));
        builder.buildNewCertificateLink(result);
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable long id) {
        service.delete(id);
        return new ResponseEntity<>(new ActionHypermedia("certificate with id " + id + " deleted"),
                HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@RequestBody GiftCertificateDTO certificate, @PathVariable long id) {
        service.update(certificate, id);
        ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                (new ActionHypermedia("updated with id" + id));
        builder.buildRetrieveSpecificCertificateLink(id);
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NO_CONTENT);


    }
}
