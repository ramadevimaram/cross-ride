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
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.repositories.PersonRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {
  
  MockMvc mockMvc;
  
  @Mock
  private PersonController personController;
  
  @Autowired
  private TestRestTemplate template;
  
  @Autowired
  PersonRepository personRepository;
  
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
  }
  
 /* @Test
  public void testPanelShouldBeRegistered() throws Exception {
    HttpEntity<Object> person = getHttpEntity(
        "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\"," 
            + " \"registrationNumber\": \"41DCT\",\"registrationDate\":\"2018-08-08T12:12:12\" }");
    ResponseEntity<Person> response = template.postForEntity(
        "/api/person", person, Person.class);
    //Delete this user
    personRepository.deleteById(response.getBody().getId());
    Assert.assertEquals("test 1", response.getBody().getName());
    Assert.assertEquals(200,response.getStatusCode().value());
  }

  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }
*/
  
/*  @Test
  public void testPanelShouldBeRegistered() throws Exception {
    HttpEntity<Object> person = getHttpEntity(
        "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\"," 
            + " \"registrationNumber\": \"41DCT\"}");
    ResponseEntity<Person> response = template.postForEntity(
        "/api/person", person, Person.class);
    //Delete this user
    personRepository.deleteById(response.getBody().getId());
    Assert.assertEquals("test 1", response.getBody().getName());
    Assert.assertEquals("test10000000000001@gmail.com", response.getBody().getEmail());
    Assert.assertEquals("41DCT", response.getBody().getRegistrationNumber());
    
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

import java.util.ArrayList;
import java.util.List;

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
import com.crossover.techtrial.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author kshah
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {
  
  MockMvc mockMvc;
  
  @InjectMocks
  private PersonController personController;
  
  @Mock
  private PersonService personService;
  
  
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
  }
  
  @Test
	public void testGetAllPersons() throws Exception {
		List<Person> personList = new ArrayList<Person>();
		personList.add(getPerson());
		String result = new ObjectMapper().writeValueAsString(personList);
		Mockito.when(personService.getAll()).thenReturn(personList);
		mockMvc.perform(get("/api/person")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(result));

	}

  @Test
  public void testRegister() throws Exception {
		String input =  new ObjectMapper().writeValueAsString(getPerson());
		Mockito.when(personService.save(Mockito.anyObject())).thenReturn(getPerson());
		mockMvc.perform(post("/api/person").content(input).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		       .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(content().json(input));		
  }
  @Test
  public void testGetPersonId() throws Exception {
	  String output = new ObjectMapper().writeValueAsString(getPerson());
	  Mockito.when(personService.findById(Mockito.anyLong())).thenReturn(getPerson());
	  mockMvc.perform(get("/api/person/1")).andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(content().json(output));
	  
  }
  @Test
  public void testGetPersonIdNull() throws Exception {
	  Mockito.when(personService.findById(Mockito.anyLong())).thenReturn(null);
	  mockMvc.perform(get("/api/person/1")).andExpect(status().isNotFound());
	  
  }
  private Person getPerson() {
		Person p1 = new Person();
		p1.setId((long) 1);
		p1.setEmail("xxx@gmail.com");
		p1.setName("name");
		p1.setRegistrationNumber("TS123444");
		return p1;
	}
}