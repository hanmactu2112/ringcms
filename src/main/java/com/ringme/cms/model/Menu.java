package com.ringme.cms.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@Table(name = "menu")
public class Menu extends EntityBase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "router_id")
    private Router router;
    private Integer order;
    @Column(name = "parent_name")
    private String parentName;
}
