package com.apap.tutorial7.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apap.tutorial7.model.FlightModel;
import com.apap.tutorial7.model.PilotModel;
import com.apap.tutorial7.service.FlightService;
import com.apap.tutorial7.service.PilotService;

/**
 * FlightController
 */
@RestController
@RequestMapping("/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;
    
    @Autowired
    private PilotService pilotService;

    @PostMapping(value = "/add")
    private FlightModel addRow(@RequestBody FlightModel flight) {
        flightService.addFlight(flight);
        return flight;
    }

    @RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.POST, params={"save"})
    private String addFlightSubmit(@ModelAttribute PilotModel pilot) {
        PilotModel archive = pilotService.getPilotDetailByLicenseNumber(pilot.getLicenseNumber());
        for (FlightModel flight : pilot.getListFlight()) {
            if (flight != null) {
                flight.setPilot(archive);
                flightService.addFlight(flight);
            }
        }
        return "add";
    }

	@RequestMapping(value = "/all")
	public List<FlightModel> viewFlight(Model model) {
		return flightService.findAllFlight();
	}

    @GetMapping(value = "/view/{flightNumber}")
    public FlightModel view(@PathVariable(value = "flightNumber") String flightNumber, Model model) {
        FlightModel archive = flightService.getFlightDetailByFlightNumber(flightNumber).get();
        return archive;
    }

    @DeleteMapping(value = "/{flightId}")
    public String delete(@PathVariable(value = "flightId") long flightId) {
    	flightService.deleteFlightById(flightId);
        return "flight has been deleted";
    }

    @PutMapping(value = "/update/{flightid}")
	public String updateFlightDB(@PathVariable(value = "pilotId") long flightId, @RequestParam(value = "flightNumber", required = false) String flightNumber, @RequestParam(value = "origin", required = false) String origin, @RequestParam(value = "destination", required = false) String destination, @RequestParam(value = "time", required = false) Date time) {
        PilotModel pilot = pilotService.getPilotDetailById(flightId).get();
        if (pilot.equals(null)) {
			return "Couldn't find your pilot";
		}
        
        System.out.println(flightId);
        System.out.println(flightNumber + " " + origin + " " + destination);
        System.out.println(pilot.getLicenseNumber());
		flightService.updateFlight(flightId, flightNumber, origin, destination, time);
        return "flight update success";
    }
}