package com.glasspark.studyspot.studyrooms.mapper;

import com.glasspark.studyspot.studyrooms.domain.StudyRoom;
import com.glasspark.studyspot.studyrooms.dto.StudyRoomSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudyRoomMapper {

    void insertStudyRoom(StudyRoom studyRoom);

    // 같은 주소에 룸이 여러 개 있을 수 있으므로 (주소 + 이름) 조합으로 중복을 본다. DB 의 UNIQUE (address, name) 와 같은 기준.
    int countByAddressAndName(@Param("address") String address, @Param("name") String name);

    List<StudyRoom> findAll(StudyRoomSearchRequest request);

    long count(StudyRoomSearchRequest request);

    // 상세 조회용 (락 없음). 예약 처리에는 쓰지 말고 lockById 를 쓸 것.
    StudyRoom findById(@Param("studyRoomId") Long studyRoomId);

    // 예약용 락 조회. 같은 트랜잭션의 "첫 번째 DB 호출"이어야 한다.
    StudyRoom lockById(@Param("studyRoomId") Long studyRoomId);
}
