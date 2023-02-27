package com.ringme.cms.model;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@Table(name = "log")
public class Log{
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long objectId;
    @Column
    private String accountId;
    @Column
    private String accountName;
    @Column
    private String action;
    @Column
    private String objectName;
    @Column
    private String methodName;
    @Column
    private LocalDateTime time;
}
