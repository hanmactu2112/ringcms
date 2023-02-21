package com.ringme.cms.models;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@MappedSuperclass
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EntityBase implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column(name = "created_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Timestamp createdDate;
    @Column(name = "modified_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Timestamp modifiedDate;
    @PrePersist
    public <T extends EntityBase> void prePersist(T t) {
        // Set createdDate before persisting
        t.setCreatedDate(Timestamp.from(Instant.now()));
    }

    @PreUpdate
    public  <T extends EntityBase> void preUpdate(T t) {
        // Set updatedDate before updating
        t.setModifiedDate(Timestamp.from(Instant.now()));
    }
}
