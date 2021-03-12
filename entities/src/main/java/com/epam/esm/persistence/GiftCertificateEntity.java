package com.epam.esm.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "gift_certificate")
public class GiftCertificateEntity {

    @Column(name = "id_certificate")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name_of_certificate")
    private String name;
    private String description;
    private long price;
    private int duration;
    @Column(name = "create_date")
    private LocalDate createDate;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,
                    CascadeType.DETACH,
                    CascadeType.REFRESH,
                    CascadeType.REMOVE})
    @JoinTable(
            name = "junction_gift_certificates_and_tags",
            joinColumns = {@JoinColumn(name = "id_certificate")},
            inverseJoinColumns = {@JoinColumn(name = "id_tag")}
    )
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<TagEntity> tagsDependsOnCertificate = new HashSet<>();

    public void addTag(TagEntity tag) {
        tagsDependsOnCertificate.add(tag);
        if (!tag.getCertificateEntitySet().contains(this)) {
            tag.addCertificate(this);
        }
    }

    public void removeTag(TagEntity tag) {
        tagsDependsOnCertificate.remove(tag);
        if(tag.getCertificateEntitySet().contains(this)) {
            tag.removeCertificate(this);
        }
    }

}


