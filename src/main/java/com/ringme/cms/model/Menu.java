package com.ringme.cms.model;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "menu")
public class Menu extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotNull(message = "Name menu not null")
    private String name;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "router_id")
    private Router router;
    @NotNull(message = "Order not null")
    private Integer order_num;
    @Column(name = "parent_name")
    private String parentName;

    @ManyToOne
    @JoinColumn(name = "icon_id")
    private Icon icon;
}
