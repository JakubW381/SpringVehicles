package dev.jakubw.rent.service.impl;

import dev.jakubw.rent.model.Vehicle;
import dev.jakubw.rent.repository.VehicleRepository;
import dev.jakubw.rent.service.RentalService;
import dev.jakubw.rent.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VehicleServiceImpl implements VehicleService {


    private final VehicleRepository vehicleRepository;
    private final RentalService rentalService;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, RentalService rentalService) {
        this.vehicleRepository = vehicleRepository;
        this.rentalService = rentalService;
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findAllActive() {
        return vehicleRepository.findByActiveTrue();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findById(id)
                .filter(Vehicle::isActive);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        vehicle.setActive(true);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return findAllActive().stream()
                .filter(v -> !rentalService.isVehicleRented(v.getId()))
                .toList();
    }

    @Override
    public List<Vehicle> findRentedVehicles() {
        return findAllActive().stream()
                .filter(v -> rentalService.isVehicleRented(v.getId()))
                .toList();
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        return findById(vehicleId)
                .filter(v -> !rentalService.isVehicleRented(vehicleId))
                .isPresent();
    }

    @Override
    public void deleteById(String id) {
        vehicleRepository.findById(id).ifPresent(vehicle -> {
            vehicle.setActive(false);
            vehicleRepository.save(vehicle);
        });
    }
}
