/*package com.crossover.techtrial.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonServiceImplTest {
	
	MockMvc mockMvc;
	  
	  @InjectMocks
	  private PersonServiceImpl personServiceImpl;
	  
	  @Mock
	  private PersonRepository personRepository;
	  
	  @Before
	  public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	    mockMvc = MockMvcBuilders.standaloneSetup(personServiceImpl).build();
	    
	  }
	  @Test
		public void testGetAllPersons() throws Exception {
			List<Person> personList = new ArrayList<Person>();
			personList.add(getPerson());
			String result = new ObjectMapper().writeValueAsString(personList);
			Mockito.when(((PersonServiceImpl) personRepository).getAll()).thenReturn(personList);
			mockMvc.perform(get("/api/person")).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().json(result));
			
			
	  }
	private Person getPerson() {
		Person p1 = new Person();
		p1.setId((long) 1);
		p1.setEmail("xxx@gmail.com");
		p1.setName("name");
		p1.setRegistrationNumber("TS123444");
		return p1;
	}
	
	  
	  
}*/

package com.crossover.techtrial.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.repositories.PersonRepository;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PersonServiceImplTest {

    @Mock
    PersonRepository personRepository ;

    @Autowired
    @InjectMocks
    private PersonServiceImpl personServiceImpl;

   
    @Test
    public void TestNotExsistReturnNull() {
        when(personRepository.findById(2l)).thenReturn(Optional.empty());
        assertNull(personServiceImpl.findById(2l));
    }

    @Test
    public void TestExsistReturnPerson() {
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(new Person()));
        Person person = personServiceImpl.findById(3L);
        assertNotNull(person);
    }

    //Test GetAll 
    @Test
    public void testGetAllEmpty(){
        when(personRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(0,personServiceImpl.getAll().size());
    }

    @Test
    public void testGetAllHasElements(){
        List list = Arrays.asList(new Person(),new Person(),new Person());
        when(personRepository.findAll()).thenReturn(list);
        assertEquals(3,personServiceImpl.getAll().size());
    }

    // Test Save 
    @Test
    public void testSavePerson(){
        Person person = new Person();
        person.setEmail("Test@test.com");
        person.setName("Jose");
        person.setRegistrationNumber("01114786614");

        when(personRepository.save(person)).thenReturn(person);
        assertEquals(person,personServiceImpl.save(person));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSavePersonWithoutEmail(){
        Person person = new Person();
        person.setName("Cindy");
        person.setRegistrationNumber("01114786614");
        when(personRepository.save(person)).thenThrow(new ConstraintViolationException(null));
        personServiceImpl.save(person);
    }

}
