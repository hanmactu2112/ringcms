package com.ringme.cms.criteria;

import lombok.*;


@Data
public class QueueSearchCriteria {
    private Long id;
    private String queueName;
    private String hostName;
    private String displayName;
    private String department;
    private String typeQueue;
    private String mission;
    private String province;

    public QueueSearchCriteria(Long id, String queueName, String hostName, String displayName, String department, String typeQueue, String mission, String province) {
        this.id = id;
        this.queueName = queueName;
        this.hostName = hostName;
        this.displayName = displayName;
        this.department = department;
        this.typeQueue = typeQueue;
        this.mission = mission;
        this.province = province;
    }
}
