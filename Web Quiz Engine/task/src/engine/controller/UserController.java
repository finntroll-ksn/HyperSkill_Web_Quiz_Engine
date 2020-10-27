package engine.controller;

import engine.entity.User;
import engine.repository.UserRepository;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.regex.Pattern;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PostMapping(value = "/api/register", consumes = "application/json")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        String regex = "^(.+)@(.+)\\.(.+)$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(user.getEmail()).matches()) {
            return new ResponseEntity<>("The email must have a valid format (with @ and .).", HttpStatus.BAD_REQUEST);
        }

        if (user.getPassword().length() < 5) {
            return new ResponseEntity<>("The password must have at least five characters.", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByEmail(user.getEmail()) == null) {
            userService.saveUser(user);

            return new ResponseEntity<>("Registration completed!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("This email is taken", HttpStatus.BAD_REQUEST);
        }
    }
}
