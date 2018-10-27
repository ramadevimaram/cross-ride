/*package com.crossover.techtrial.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.repositories.RideRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RideControllerTest {
	
	MockMvc mockMvc;
	  
	  @Mock
	  private RideController rideController;
	  
	  @Autowired
	  private TestRestTemplate template;
	  
	  @Autowired
	  RideRepository rideRepository;
	  
	  @Before
	  public void setup() throws Exception {
	    mockMvc = MockMvcBuilders.standaloneSetup(rideController).build();
	  }
	  
	  @Test
	  public void testPanelShouldBeRegistered1() throws Exception {
	    HttpEntity<Object> ride = getHttpEntity(
	        "{\"startTime\": \"1\", \"endTime\": \"2\"," 
	            + " \"distance\": \"10\", \"driver\": \"2\",\"rider\": \"2\"}");
	    ResponseEntity<Ride> response = template.postForEntity(
	        "/api/ride", ride, Ride.class);
	    //Delete this user
	   rideRepository.deleteById(response.getBody().getId());
	    Assert.assertEquals(1, response.getBody().getStartTime()); 
	    Assert.assertEquals(200,response.getStatusCode().value());
	  }

	  private HttpEntity<Object> getHttpEntity(Object body) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    return new HttpEntity<Object>(body, headers);
	  }

	

}
*/

package com.crossover.techtrial.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.service.RideService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author kshah
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RideControllerTest {
	MockMvc mockMvc;

	@InjectMocks
	private RideController rideController;

	@Mock
	private RideService rideService;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(rideController).build();
	}

	@Test
	public void testCreateNewRide() throws Exception {
		String input = new ObjectMapper().writeValueAsString(getRide());
		Mockito.when(rideService.save(Mockito.anyObject())).thenReturn(getRide());
		mockMvc.perform(post("/api/ride").content(input).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(input));
	}

	@Test
	public void testGetRideById() throws Exception {
		String output = new ObjectMapper().writeValueAsString(getRide());
		Mockito.when(rideService.findById(Mockito.anyLong())).thenReturn(getRide());
		mockMvc.perform(get("/api/ride/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(output));

	}

	@Test
	public void testGetRideIdNull() throws Exception {
		Mockito.when(rideService.findById(Mockito.anyLong())).thenReturn(null);
		mockMvc.perform(get("/api/ride/1")).andExpect(status().isNotFound());

	}

	private Ride getRide() {
		Ride r = new Ride();
		r.setId(new Long(1));
		r.setRider(getRider());
		r.setDriver(getDriver());
		r.setDistance(new Long(10));
		r.setStartTime("10:00");
		r.setEndTime("11:00");
		return r;
	}

	private Person getRider() {
		Person rider = new Person();
		rider.setId((long) 2);
		rider.setEmail("xxx@gmail.com");
		rider.setName("name");
		rider.setRegistrationNumber("TS123444");
		return rider;
	}

	private Person getDriver() {
		Person driver = new Person();
		driver.setId((long) 3);
		driver.setEmail("yyy@gmail.com");
		driver.setName("eeee");
		driver.setRegistrationNumber("TS12363636");
		return driver;
	}
}

