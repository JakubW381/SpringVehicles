package dev.jakubw.rent.controller;

import dev.jakubw.rent.model.Vehicle;
import dev.jakubw.rent.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public List<Vehicle> getAllActiveVehicles() {
        return vehicleService.findAllActive();
    }

    @GetMapping("/all")
    public List<Vehicle> getAllVehiclesIncludingDeleted() {
        return vehicleService.findAll();
    }


    @GetMapping("/available")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.findAvailableVehicles();
    }

    @GetMapping("/rented")
    public List<Vehicle> getRentedVehicles() {
        return vehicleService.findRentedVehicles();
    }
}

