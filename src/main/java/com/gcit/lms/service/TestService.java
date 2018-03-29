/**
 * 
 */
package com.gcit.lms.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 *
 */
@RestController
public class TestService {

	@RequestMapping(name="/home",method=RequestMethod.GET)
	public String testMe() {
		return "Hell Wolrd";
	}
}
