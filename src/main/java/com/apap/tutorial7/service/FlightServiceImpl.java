package com.apap.tutorial7.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import com.apap.tutorial7.model.FlightModel;
import com.apap.tutorial7.repository.FlightDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FlightServiceImpl
 */
@Service
@Transactional
public class FlightServiceImpl implements FlightService {
	@Autowired
	private FlightDb flightDb;

	@Override
	public FlightModel addFlight(FlightModel flight) {
		return flightDb.save(flight);
	}

	@Override
	public void deleteByFlightNumber(String flightNumber) {
		flightDb.deleteByFlightNumber(flightNumber);
	}

	@Override
	public Optional<FlightModel> getFlightDetailByFlightNumber(String flightNumber) {
		return flightDb.findByFlightNumber(flightNumber);
	}

	public void updateFlight(long id, String flightNumber, String origin, String destination, Date time) {
		FlightModel flightModel = flightDb.findById(id).get();
		if(!flightNumber.isEmpty()) {
			flightModel.setFlightNumber(flightNumber);
		}
		if(!origin.isEmpty()) {
			flightModel.setOrigin(origin);
		}
		if(!destination.isEmpty()) {
			flightModel.setDestination(destination);
		}
		if(!(time == null)) {
			flightModel.setTime(time);
		}
	}
	
	public List<FlightModel> findAllFlight(){
		return flightDb.findAll();
	}
	
	public void deleteFlightById(long id) {
		flightDb.deleteById(id);
	}
}