package dev.jakubw.rent.controller;

import dev.jakubw.rent.dto.RoleRequest;
import dev.jakubw.rent.model.Role;
import dev.jakubw.rent.model.User;
import dev.jakubw.rent.model.Vehicle;
import dev.jakubw.rent.repository.RoleRepository;
import dev.jakubw.rent.service.UserService;
import dev.jakubw.rent.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepo;

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        Vehicle saved = vehicleService.save(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String id) {
        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers(){
        return new ResponseEntity<>(userService.findAll(),HttpStatus.OK);
    }

    @DeleteMapping("/soft-delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/give-role")
    public ResponseEntity<?> giveRole(@RequestBody RoleRequest request){
        Optional<Role> optRole = roleRepo.findByName(request.getRoleName());
        Optional<User> optUser = userService.findByLogin(request.getLogin());

        if (optRole.isPresent() && optUser.isPresent()){
            User user = optUser.get();
            Role role = optRole.get();

            if (!user.getRoles().contains(role)){
                Set<Role> roles = user.getRoles();
                roles.add(role);
                user.setRoles(roles);
                userService.save(user);
                return ResponseEntity.ok("gave role " + request.getRoleName() + "to user " + request.getLogin());
            }else{
                return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/delete-role")
    public ResponseEntity<?> deleteRole(@RequestBody RoleRequest request){
        Optional<Role> optRole = roleRepo.findByName(request.getRoleName());
        Optional<User> optUser = userService.findByLogin(request.getLogin());

        if (optRole.isPresent() && optUser.isPresent()){
            User user = optUser.get();
            Role role = optRole.get();

            if (user.getRoles().contains(role)){
                Set<Role> roles = user.getRoles();
                roles.remove(role);
                user.setRoles(roles);
                userService.save(user);
                return ResponseEntity.ok("removed role " + request.getRoleName() + "from user " + request.getLogin());
            }else{
                return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }
}
