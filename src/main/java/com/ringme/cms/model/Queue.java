package com.ringme.cms.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@Table(name = "queue")
public class Queue extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "queue_name")
    private String queueName;
    @Column(name = "display_name")
    private String displayName;
    private Integer minutes;
    @ManyToOne
    @JoinColumn(name = "next_queue_id")
    private Queue nextQueue;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;
    @Column(name = "type_queue")
    private String typeQueue;
    private String description;
    @Column(name = "start_time")
    private Time startTime;
    @Column(name = "end_time")
    private Time endTime;
    private String image;
    @OneToMany(mappedBy="queue",fetch = FetchType.EAGER)
    private Collection<Staff> staff;
}
