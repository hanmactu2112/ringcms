package com.ringme.cms.repository;

import com.ringme.cms.model.Queue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {
    // one
    @Query(name = "select * from queue q where q.department_id =?1 ", countQuery = "Select count(*) from queue q where q.department_id =?1", nativeQuery = true)
    Page<Queue> findAllQueueByDepartment(Long departmentId, Pageable pageable);

    @Query(name = "select * from queue q where q.type_queue =?1 ", countQuery = "Select count(*) from queue q where q.type_queue =?1", nativeQuery = true)
    Page<Queue> findAllQueueByTypeQueue(String typeQueue, Pageable pageable);

    @Query(name = "select * from queue q where q.mission_id =?1 ", countQuery = "Select count(*) from queue q where q.mission_id =?1", nativeQuery = true)
    Page<Queue> findAllQueueByMission(Long missionId, Pageable pageable);

    @Query(name = "select * from queue q where q.province like %?1% ", countQuery = "Select count(*) from queue q where q.province like %?1%", nativeQuery = true)
    Page<Queue> findAllQueueByProvince(String province, Pageable pageable);

    // two
    @Query(name = "select * from queue q where q.department_id =?1 and q.type_queue =?2 ", countQuery = "Select count(*) from queue q q.department_id =?1 and q.type_queue =?2", nativeQuery = true)
    Page<Queue> findAllQueueByDepartmentAndTypeQueue(Long departmentId, String typeQueue, Pageable pageable);

    @Query(name = "select * from queue q where q.department_id =?1 and q.mission_id =?2 ", countQuery = "Select count(*) from queue q q.department_id =?1 and q.mission_id =?2 ", nativeQuery = true)
    Page<Queue> findAllQueueByDepartmentAndMission(Long departmentId, Long missionId, Pageable pageable);

    @Query(name = "select * from queue q where q.department_id =?1 and q.province like %?2% ", countQuery = "Select count(*) from queue q q.department_id =?1 and q.province like %?2% ", nativeQuery = true)
    Page<Queue> findAllQueueByDepartmentProvince(Long departmentId, String province, Pageable pageable);

    @Query(name = "select * from queue q where q.type_queue =?1 and q.mission_id =?2 ", countQuery = "Select count(*) from queue q q.type_queue =?1 and q.mission_id =?2 ", nativeQuery = true)
    Page<Queue> findAllQueueByTypeQueueAndMission(String typeQueue, Long missionId, Pageable pageable);

    @Query(name = "select * from queue q where q.type_queue =?1 and q.province like %?2%  ", countQuery = "Select count(*) from queue q q.type_queue =?1 and q.province like %?2% ", nativeQuery = true)
    Page<Queue> findAllQueueByTypeQueueAndProvince(String typeQueue, String province, Pageable pageable);

    @Query(name = "select * from queue q where q.mission_id =?1 and q.province like %?2%  ", countQuery = "Select count(*) from queue q q.mission_id =?1 and q.province like %?2% ", nativeQuery = true)
    Page<Queue> findAllQueueByMissionAndProvince(Long missionId, String province, Pageable pageable);

    // three
    @Query(name = "select * from queue q where q.department_id =?1 and q.type_queue =?2 and q.mission_id =?3  ", countQuery = "Select count(*) from queue q q.department_id =?1 and q.type_queue =?2 and and q.mission_id =?3 ", nativeQuery = true)
    Page<Queue> findAllQueueByDepartmentAndTypeQueueAndMission(Long departmentId, String typeQueue, Long missionId, Pageable pageable);

    @Query(name = "select * from queue q where q.department_id =?1 and q.mission_id =?2 and q.province like %?3%  ", countQuery = "Select count(*) from queue q q.department_id =?1 and q.mission_id =?2 and and q.province like %?3% ", nativeQuery = true)
    Page<Queue> findAllQueueByDepartmentAndMissionAndProvince(Long departmentId, Long missionId, String province, Pageable pageable);

    @Query(name = "select * from queue q where q.type_queue =?1 and q.mission_id =?2 and q.province like %?3%  ", countQuery = "Select count(*) from queue q q.department_id =?1 and q.type_queue =?2 and and q.mission_id =?3 ", nativeQuery = true)
    Page<Queue> findAllQueueByTypeQueueAndMissionAndProvince(String typeQueue, Long missionId, String province, Pageable pageable);

    // four
    @Query(name = "select * from queue q where q.department_id =?1 and q.type_queue =?2 and q.mission_id =?3 and q.province like %?4%  ", countQuery = "Select count(*) from queue q q.department_id =?1 and q.type_queue =?2 and q.mission_id =?3 and q.province like %?4% ", nativeQuery = true)
    Page<Queue> findAllQueueByDepartmentAndTypeQueueAndMissionAndProvince(Long departmentId, String typeQueue, Long missionId, String province, Pageable pageable);

    @Query(name = "Select * from queue q where q.next_queue_id =: nextQueueId", nativeQuery = true)
    List<Queue> findAllByNextQueueId(Long nextQueueId);
}
