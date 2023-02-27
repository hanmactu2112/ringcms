package com.ringme.cms.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@MappedSuperclass
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class EntityBase implements Serializable {



    @Column(updatable = false)
    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date createdDate;

    @Column
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;
}
