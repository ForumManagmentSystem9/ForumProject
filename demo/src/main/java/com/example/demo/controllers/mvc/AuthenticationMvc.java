package com.example.demo.controllers.mvc;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.helpers.UserMapper;
import com.example.demo.models.userfolder.CustomUserDetails;
import com.example.demo.models.userfolder.LoginDto;
import com.example.demo.models.userfolder.RegistreDto;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.JWTService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springdoc.core.converters.ModelConverterRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvc {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final ModelConverterRegistrar modelConverterRegistrar;
    private final UserMapper userMapper;
    private UserService userService;

    @Autowired
    public AuthenticationMvc(AuthenticationManager authenticationManager,
                             JWTService jwtService,
                             UserService userService, ModelConverterRegistrar modelConverterRegistrar, UserMapper userMapper){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.modelConverterRegistrar = modelConverterRegistrar;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public  boolean populateIsAithenticated(HttpSession session){
        return session.getAttribute("currentUser") !=null;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model){
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }
    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto loginDto,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model){
        if (bindingResult.hasErrors()){
            return "LoginView";
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                            loginDto.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userDetails.getUser());

            session.setAttribute("currentUser", loginDto.getEmail());
            session.setAttribute("jwtToken", jwtToken);

            return "redirect:/";
        }catch (AuthorizationException e){
            bindingResult.rejectValue("email", "auth_error",
                    "Invalid email or password");
            return "LoginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/refresh-token")
    public String refreshToken(HttpSession session, Model model){
        String token = (String) session.getAttribute("jwtToken");

        if (token !=null){
            try {
                String refreshToken = jwtService.refreshToken(token);
                session.setAttribute("jwtToken", refreshToken);
                model.addAttribute("message", "Token refreshed");
                return "TokenRefreshView";
            }catch (Exception e){
                model.addAttribute("error", "Invalid token");
                return "ErrorView";
            }
        }else {
            model.addAttribute("error", "No token found");
            return "ErrorView";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model){
        model.addAttribute("register", new RegistreDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegistre(@Valid @ModelAttribute("register") RegistreDto registreDto,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "RegisterView";
        }

        if (!registreDto.getPassword().equals(registreDto.getPasswordConfirm())){
            bindingResult.rejectValue("passwordConfirm", "password_error",
                    "Password does not match");
            return "RegisterView";
        }
        try {
            User user = new User();
            user.setUsername(registreDto.getUsername());
            user.setEmail(registreDto.getEmail());
            user.setPassword(registreDto.getPassword());

            user.setPassword(userService.encodePassword(user.getPassword()));

            return "redirect:/auth/login";
        }catch (EntityDuplicateException e){
            bindingResult.rejectValue("email", "duplicate_email", e.getMessage());
            return "RegisterView";
        }
    }
}
