package com.kodemi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerDto {
	private String banner_Id;
	private String title;
	private String subtitle;
	private String image_url;
	private String redirect_url;
	private Integer priority;
	private String status;
}
