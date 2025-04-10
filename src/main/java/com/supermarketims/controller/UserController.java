package com.supermarketims.controller;

import com.supermarketims.model.User;
import com.supermarketims.model.UserRole;
import com.supermarketims.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ✅ Show Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Returns login.html
    }

    // ✅ Show Signup Page
    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup"; // Returns signup.html
    }

    // ✅ Register User (Signup)
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists!");
            return "signup"; // Stay on signup page if username exists
        }

        User newUser = new User(username, password, UserRole.valueOf(role.toUpperCase()));
        userRepository.save(newUser);
        return "redirect:/user/login"; // Redirect to login after signup
    }

    @PostMapping("/login")
public String login(@RequestParam String username, 
                    @RequestParam String password, 
                    HttpSession session) {
    User user = userRepository.findByUsername(username).orElse(null);

    if (user != null && user.getPassword().equals(password)) {
        session.setAttribute("user", user); // Store user in session

        System.out.println("Login Successful for: " + user.getUsername() + " | Role: " + user.getRole());

        return "redirect:/user/dashboard"; // Redirect to dashboard based on role
    } else {
        return "login"; // Stay on login page if authentication fails
    }
}

    

    // ✅ Logout User
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login"; // Redirect to login page after logout
    }

    // ✅ Get All Users (Admin Only)
    @GetMapping("/all")
    public String getAllUsers(HttpSession session, Model model) {
        User loggedInUser = getSessionUser(session);
        if (loggedInUser == null || loggedInUser.getRole() != UserRole.ADMIN) {
            model.addAttribute("error", "Access Denied! Only Admins can view all users.");
            return "error"; // Show an error page
        }
        model.addAttribute("users", userRepository.findAll());
        return "users"; // Return a users.html template with list of users
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
    
        if (loggedInUser == null) {
            return "redirect:/user/login"; // Redirect to login if not authenticated
        }
    
        // Redirect based on role
        if (loggedInUser.getRole() == UserRole.ADMIN) {
            return "redirect:/admin/dashboard";
        } else if (loggedInUser.getRole() == UserRole.STAFF) {
            return "redirect:/staff/dashboard";
        } else {
            return "redirect:/customer/dashboard";
        }
    }
    
    


    // ✅ Delete User (Admin Only)
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session, Model model) {
        User loggedInUser = getSessionUser(session);
        if (loggedInUser == null || loggedInUser.getRole() != UserRole.ADMIN) {
            model.addAttribute("error", "Access Denied! Only Admins can delete users.");
            return "error"; // Show error page
        }
        userRepository.deleteById(id);
        return "redirect:/user/all"; // Redirect back to users list after deletion
    }

    // ✅ Helper method to get logged-in user from session
    private User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}
