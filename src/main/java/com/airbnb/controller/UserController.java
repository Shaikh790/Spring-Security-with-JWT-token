package com.airbnb.controller;

import com.airbnb.dto.JWTResponse;
import com.airbnb.dto.LoginDto;
import com.airbnb.dto.PropertyUserDto;
import com.airbnb.entity.PropertyUser;
import com.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    //create method addusers
    @PostMapping("/addUsers")
    public ResponseEntity<String> addUser(@RequestBody PropertyUserDto dto) {
        PropertyUser user = userService.addUser(dto);
        System.out.println(dto);
        if (user!=null){


        return new ResponseEntity<>("sign up succesfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }


        //localhost:8080/api/v1/users/addUsers
    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody LoginDto loginDto){

//        System.out.println(loginDto.getUsername());
//        System.out.println(loginDto.getPassword());
       String jwtToken=userService.verifyLogin(loginDto);

       if(jwtToken!=null){
           JWTResponse jwtResponse= new JWTResponse();
           jwtResponse.setToken(jwtToken);
           return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
       }
        return new ResponseEntity<>("Invalid Credential ", HttpStatus.UNAUTHORIZED);
        //http://localhost:8080/api/v1/users/login
    }


    @GetMapping("/profile")
     public  PropertyUser getCurrentProfile(@AuthenticationPrincipal PropertyUser propertyUser){
        return propertyUser;
     }
   //http://localhost:8080/api/v1/users/profile
}
