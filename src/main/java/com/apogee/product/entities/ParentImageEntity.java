package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "parent_images")
public class ParentImageEntity {

    @EmbeddedId
    ParentImageId id;

    @Column(name = "type")
    private String type;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "uploaded_at")
    private Date uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("parentItemId")
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private ParentItemEntity parentItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("imageId")
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageEntity image;
}
