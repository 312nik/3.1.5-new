package com.kata312.controller;

import com.kata312.exception.RecordNotFoundException;
import com.kata312.model.Role;
import com.kata312.model.User;
import com.kata312.service.RoleServiceImpl;
import com.kata312.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller

public class AdminController {

    private final UserServiceImpl userService;

    @Autowired
    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model, Principal principal) {
        String userMail = principal.getName();
        User user= userService.findUserByEmail(userMail);
        List<User> users = userService.findAll();
        User newUser=new User();
        model.addAttribute("userPrincipal",user);
        model.addAttribute("users", users);
        model.addAttribute("newUser",newUser);
        return "/admin";
    }

@Transactional
    @PostMapping("admin/new")
    public String createUser(@ModelAttribute("newUser")  User user, RedirectAttributes redirectAttributes) {
        User userNew= userService.findUserByEmail(user.getEmail());

        if (userNew !=null){
            redirectAttributes.addFlashAttribute("message",
                    "A user with such an email already exists!");

            return "redirect:/admin";
        }
        userService.saveUser(user);
        return "redirect:/admin";

    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) throws RecordNotFoundException {

        userService.deleteUser(id);
        return "redirect:/admin";

    }



    @PostMapping("/admin/edit")
    public String update( User user, RedirectAttributes redirectAttributes) {



        if (userService.findUserByEmail(user.getUsername()) != null &&
                !userService.findUserByEmail(user.getUsername()).getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("message",
                    "A user with such an email already exists!");
        }

        userService.updateUser(user);
        return "redirect:/admin";

    }

}




