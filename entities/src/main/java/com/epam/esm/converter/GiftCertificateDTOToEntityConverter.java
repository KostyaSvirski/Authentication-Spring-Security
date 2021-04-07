package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.persistence.GiftCertificateEntity;
import com.epam.esm.persistence.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDTOToEntityConverter implements Function<GiftCertificateDTO, GiftCertificateEntity> {

    @Autowired
    private TagDTOToTagEntityConverter tagDTOToTagEntityConverter;

    @Override
    public GiftCertificateEntity apply(GiftCertificateDTO certificateDTO) {
        GiftCertificateEntity entity = GiftCertificateEntity.builder().id(certificateDTO.getId())
                .name(certificateDTO.getName())
                .createDate(certificateDTO.getCreateDate() != null ? LocalDate.parse(certificateDTO.getCreateDate()) : null)
                .lastUpdateDate(LocalDateTime.parse(certificateDTO.getLastUpdateDate()))
                .description(certificateDTO.getDescription())
                .duration(certificateDTO.getDuration())
                .price(certificateDTO.getPrice()).build();
        Set<TagEntity> setOfTags = certificateDTO.getTags().stream()
                .map(tag -> tagDTOToTagEntityConverter.apply(tag)).collect(Collectors.toSet());
        setOfTags.forEach(entity::addTag);
        return entity;
    }
}
