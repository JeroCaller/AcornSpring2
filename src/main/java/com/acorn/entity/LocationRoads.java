package com.acorn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "location_roads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRoads {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length = 11)
	private Integer no;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "location_no", nullable = false, referencedColumnName = "no")
	private Locations locations;
	
}
