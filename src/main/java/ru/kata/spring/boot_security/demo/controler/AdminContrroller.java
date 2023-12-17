package ru.kata.spring.boot_security.demo.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminContrroller {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AdminContrroller(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String findAll(Model model,
                          @ModelAttribute("user") User user,
                          Principal principal) {
        User admin = userService.getUser(principal.getName());
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("allRoles", roleService.getRolesList());
        model.addAttribute("user", new User());
        model.addAttribute("admin", admin );

        return "all-users";
    }
//форма нового пользователя
    @GetMapping("/addNewUser")
    public String addNewUser(Model model,
                             Principal principal) {
        User admin = userService.getUser(principal.getName());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getRolesList());
        model.addAttribute("admin", admin );
        return "new";
    }
//сохранение нового пользователя
    @PostMapping("/userNew/{id}")
    public String newUser(Model model,
                          @ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getRolesList());
            return "new";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/userEdit/{id}")
    public String editUser( Model model,
                           @ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult) {
        System.out.println("просят обновить...");
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getRolesList());
            return "edit";
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/userDelete/{id}")
    public String deleteUser(@RequestParam("userId") long id) {
        System.out.println("deleteUser: " + id);
        userService.deleteUser(id);
        return "redirect:/admin";
    }


}
