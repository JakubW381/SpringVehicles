package dev.jakubw.rent.repository;

import dev.jakubw.rent.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    // Metody findAll(), findById(), save(), deleteById() fromJpaRepository.
    List<Vehicle> findByActiveTrue();
    Optional<Vehicle> findByIdAndActiveTrue(String id);
    List<Vehicle> findByActiveTrueAndIdNotIn(Set<String> rentedVehicleIds);
}