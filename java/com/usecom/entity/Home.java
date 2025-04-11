package com.usecom.entity;

import java.util.List;

import com.usecom.domain.HOME_CATEGORY_SECTION;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@EqualsAndHashCode
public class Home {
//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
	
	private List<HOME_CATEGORY_SECTION> grid;
	
	private List<HOME_CATEGORY_SECTION> shopByCategories;
	
	private List<HOME_CATEGORY_SECTION> electricCategories;
	
	private List<HOME_CATEGORY_SECTION> dealCategories;
	
	private List<Deal> Deals;
	
}
