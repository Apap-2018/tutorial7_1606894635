package com.apap.tutorial7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import org.springframework.web.client.RestTemplate;

import com.apap.tutorial7.model.PilotModel;
import com.apap.tutorial7.rest.PilotDetail;
import com.apap.tutorial7.rest.Setting;
import com.apap.tutorial7.service.PilotService;

/**
 * PilotController
 */
@RestController
@RequestMapping("/pilot")
public class PilotController {
    @Autowired
    private PilotService pilotService;

    @RequestMapping("/")
    private String home() {
        return "home";
    }

    @RequestMapping(value = "/pilot/add", method = RequestMethod.GET)
    private String add(Model model) {
        model.addAttribute("pilot", new PilotModel());
        return "add-pilot";
    }

    @RequestMapping(value = "/pilot/add", method = RequestMethod.POST)
    private String addPilotsSubmit(@ModelAttribute PilotModel pilot) {
        pilotService.addPilot(pilot);
        return "add";
    }

//    @RequestMapping(value = "/pilot/view", method = RequestMethod.GET)
//    private String view(@RequestParam(value = "licenseNumber") String licenseNumber, Model model) {
//        Optional<PilotModel> archivePilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
//        
//        model.addAttribute("pilot", archivePilot.get());
//        return "view-pilot";
//    }

//    @RequestMapping(value = "/pilot/update", method = RequestMethod.GET)
//    private String update(@RequestParam(value = "licenseNumber") String licenseNumber, Model model) {
//        PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
//        model.addAttribute("pilot", archive);
//        return "update-pilot";
//    }
//
//    @RequestMapping(value = "/pilot/update", method = RequestMethod.POST)
//    private @ResponseBody PilotModel updatePilotSubmit(@ModelAttribute PilotModel pilot, Model model) {
//        pilotService.addPilot(pilot);
//        return pilot;
//    }
    
    @PostMapping(value= "/add")
    public PilotModel addPilotSubmit(@RequestBody PilotModel pilot) {
    	return pilotService.addPilot(pilot);
    }
    
    @GetMapping(value= "/view/{licenseNumber}")
    public PilotModel addPilotSubmit(@PathVariable("licenseNumber") String licenseNumber) {
    	PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
    	return pilot;
    }
    
    @DeleteMapping(value = "/delete")
    public String delete(@RequestParam(value = "licenseNumber") String licenseNumber, Model model) {
        pilotService.deletePilotByLicenseNumber(licenseNumber);
        return "success";
    }
    
    @PutMapping(value= "/update/{pilotId}")
    private String updatePilotSubmit(@PathVariable(value="pilotId") long pilotId,@RequestParam("name") String name, @RequestParam("flyHour") int flyHour) {
    		PilotModel pilot = pilotService.getPilotDetailById(pilotId).get();
    		if (pilot.equals(null)) {
    			return "Couldn't find your pilot";
    		}
    		pilot.setName(name);
    		pilot.setFlyHour(flyHour);
    		pilotService.addPilot(pilot);

    	return "update";
    }
    
    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate rest() {
    	return new RestTemplate();
    }
    
    @GetMapping(value= "/status/{licenseNumber}")
    public String getStatus(@PathVariable("licenseNumber") String licenseNumber) {
    	String path = Setting.pilotUrl+"/pilot?licenseNumber="+ licenseNumber;
    	return restTemplate.getForEntity(path, String.class).getBody();
    }

    @GetMapping(value= "/full/{licenseNumber}")
    public PilotDetail postStatus(@PathVariable("licenseNumber") String licenseNumber) {
    	String path = Setting.pilotUrl+"/pilot";
    	PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
    	PilotDetail detail = restTemplate.postForObject(path, pilot, PilotDetail.class);
    	return detail;
    }
}