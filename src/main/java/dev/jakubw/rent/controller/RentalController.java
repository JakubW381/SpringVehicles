package dev.jakubw.rent.controller;

import dev.jakubw.rent.dto.RentalRequest;
import dev.jakubw.rent.model.Rental;
import dev.jakubw.rent.model.User;
import dev.jakubw.rent.repository.UserRepository;
import dev.jakubw.rent.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rent")
public class RentalController {

    private final RentalService rentalService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails){

        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));

        if (rentalRequest.vehicleId == null || user.getId() == null){
            return ResponseEntity.badRequest().build();
        }
        try{
            Rental rental = rentalService.rent(rentalRequest.vehicleId, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping
    public ResponseEntity<?> returnVehicle(@RequestBody RentalRequest rentalRequest , @AuthenticationPrincipal UserDetails userDetails){

        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
        if (rentalRequest.vehicleId == null || user.getId() == null){
            return ResponseEntity.badRequest().build();
        }
        boolean result = rentalService.returnRental(rentalRequest.vehicleId, user.getId());
        if(result){
            return ResponseEntity.status(HttpStatus.CREATED).body(true);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
