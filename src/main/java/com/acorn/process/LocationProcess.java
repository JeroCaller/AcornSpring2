package com.acorn.process;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acorn.dto.LocationRoadsDto;
import com.acorn.dto.LocationSplitDto;
import com.acorn.entity.LocationGroups;
import com.acorn.entity.LocationRoads;
import com.acorn.entity.Locations;
import com.acorn.exception.NoDataFoundForRandomLocation;
import com.acorn.repository.LocationRoadsRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@Slf4j
public class LocationProcess {
	
	// location_roads 테이블 내 PK 필드에서 숫자가 불연속적인 구간이 있을 수 있음.
	// 그래서 없는 번호가 있을 수 있으므로
	// 테이블 내 존재하는 PK 번호에 해당하는 데이터를 추출할 때까지 반복할 횟수 지정.
	private final int MAX_REPEAT_COUNT = 100;
	
	@Autowired
	private LocationRoadsRepository locationRoadsRepository;
	
	/**
	 * DB 내 도로명 주소를 랜덤으로 하나 조회.
	 * 
	 * @author JeroCaller (JJH)
	 * @return 랜덤으로 조회된 전체 도로명 주소
	 * @throws NoDataFoundForRandomLocation 
	 *  랜덤으로 조회한 PK 번호가 테이블 내 존재하지 않아 조회된 데이터가 없는 경우 발생하는 예외
	 */
	public LocationRoads getRandomLocation() throws NoDataFoundForRandomLocation {
		Integer noMax = locationRoadsRepository.findByIdMax();
		
		// location_roads 테이블 내 데이터 한 건을 랜덤으로 조회
		Random rand = new Random();
		LocationRoads randomEntity = null;
		
		for (int i = 0; i < MAX_REPEAT_COUNT; i++) {
			Integer randId = rand.nextInt(1, noMax);
			Optional<LocationRoads> optEntity 
				= locationRoadsRepository.findById(randId);
			if (optEntity.isEmpty()) continue;
			
			randomEntity = optEntity.get();
			break;
		}
		
		if (randomEntity == null) {
			throw new NoDataFoundForRandomLocation();
		}
		
		return randomEntity;
	}
	
	/**
	 * 주소 파편 정보를 담은 locationSplitDto를 통해 새 주소를 DB에 저장.
	 * 
	 * @param locationSplitDto
	 * @return
	 */
	@Transactional
	public LocationRoads saveFull(LocationSplitDto locationSplitDto) {
		
		// TODO EXCEPTION PK에 해당하는 키가 자동으로 삽입되지 않아 에러 발생.
		LocationGroups locationGroupsEntity = LocationGroups.builder()
				.name(locationSplitDto.getLargeCity())
				.build();
		Locations locationsEntity = Locations.builder()
				.name(locationSplitDto.getMediumCity())
				.locationGroups(locationGroupsEntity)
				.build();
		LocationRoads locationRoadsEntity = LocationRoads.builder()
				.name(locationSplitDto.getRoadName())
				.locations(locationsEntity)
				.build();
		return locationRoadsRepository.save(locationRoadsEntity);
	}
}
