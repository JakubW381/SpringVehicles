package dev.jakubw.rent.service.impl;

import dev.jakubw.rent.model.Rental;
import dev.jakubw.rent.model.User;
import dev.jakubw.rent.model.Vehicle;
import dev.jakubw.rent.repository.RentalRepository;
import dev.jakubw.rent.repository.UserRepository;
import dev.jakubw.rent.repository.VehicleRepository;
import dev.jakubw.rent.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RentalServiceImpl(RentalRepository rentalRepository,
                             VehicleRepository vehicleRepository,
                             UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    @Override
    public Optional<Rental> findActiveRentalByVehicleId(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        if (isVehicleRented(vehicleId)) {
            throw new IllegalStateException("Pojazd jest już wypożyczony.");
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu: " + vehicleId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika: " + userId));

        Rental rental = Rental.builder()
                .id(UUID.randomUUID().toString())
                .vehicle(vehicle)
                .user(user)
                .rentDate(LocalDateTime.now())
                .returnDate(null)
                .build();

        return rentalRepository.save(rental);
    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        Optional<Rental> rentalOpt = findActiveRentalByVehicleId(vehicleId);

        if (rentalOpt.isPresent() && rentalOpt.get().getUser().getId().equals(userId)) {
            Rental rental = rentalOpt.get();
            rental.setReturnDate(LocalDateTime.now());
            rentalRepository.save(rental);
            return true;
        }

        return false;
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }
}
