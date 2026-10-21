package cat.udl.eps.softarch.demo.controller;

import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@BasePathAwareController
public class UserSuspensionController {

    private final UserRepository userRepository;

    public UserSuspensionController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/users/{id}/suspension")
    public ResponseEntity<Void> suspend(@PathVariable String id) {
        setSuspended(id, true);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}/suspension")
    public ResponseEntity<Void> reactivate(@PathVariable String id) {
        setSuspended(id, false);
        return ResponseEntity.ok().build();
    }

    private void setSuspended(String id, boolean suspended) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setSuspended(suspended);
        userRepository.save(user);
    }
}
