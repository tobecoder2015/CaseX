package com.autonavi.poi.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 
 * @author qingshan.wqs
 *
 */

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SenderContext {
	private String name;
	private String baseUrl;
	private String description;
	private String cookie;
			
}
