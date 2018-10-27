package com.crossover.techtrial.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.repositories.RideRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RideServiceImplTest {
	
	MockMvc mockMvc;
	
	@Mock
    RideRepository rideRepository ;

    
    @InjectMocks
    private RideServiceImpl rideServiceImpl;
    
    @Test
    public void TestNotExsistReturnNull() {
        when(rideRepository.findById(2l)).thenReturn(Optional.empty());
        assertNull(rideServiceImpl.findById(2l));
    }
    
    @Test
    public void TestExsistReturnRide() {
        when(rideRepository.findById(anyLong())).thenReturn(Optional.of(new Ride()));
        Ride ride = rideServiceImpl.findById(3L);
        assertNotNull(ride);
    }
    
    @Test(expected = ConstraintViolationException.class)
    public void testSaveRideWithOutEndTime(){
        Ride ride = new Ride();
        ride.setId(new Long(1));
    	ride.setRider(getRider());
    	ride.setDriver(getDriver());
    	ride.setDistance(new Long(10));
        ride.setStartTime("10:00");
        when(rideRepository.save(ride)).thenThrow(new ConstraintViolationException(null));
        rideServiceImpl.save(ride);
    }

 // Test Save 
    @Test
    public void testSaveRide(){
    	Ride ride = new Ride();
    	ride.setId(new Long(1));
    	ride.setRider(getRider());
    	ride.setDriver(getDriver());
    	ride.setDistance(new Long(10));
    	ride.setStartTime("10:00");
    	ride.setEndTime("11:00");
       

        when(rideRepository.save(ride)).thenReturn(ride);
        assertEquals(ride,rideServiceImpl.save(ride));
        
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
