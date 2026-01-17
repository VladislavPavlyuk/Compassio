package com.compassio.selection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.compassio.auth.UserAccount;
import com.compassio.auth.UserAccountRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SelectionController {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final SelectionService selectionService;
    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SelectionController(
            SelectionService selectionService,
            UserAccountRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.selectionService = selectionService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/selection/save-auth")
    public ResponseEntity<?> saveWithAuth(@RequestBody SelectionAuthRequest request, HttpSession session) {
        String email = normalizeEmail(request.getEmail());
        String password = request.getPassword();
        String mode = request.getMode() == null ? "login" : request.getMode().trim().toLowerCase();
        if (!isValidEmail(email) || password == null || password.length() < 8) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid email or password."));
        }

        UserAccount user = userRepository.findByEmailIgnoreCase(email).orElse(null);
        if ("register".equals(mode)) {
            if (user != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered."));
            }
            user = new UserAccount(email, passwordEncoder.encode(password));
            user.setVerified(true);
            userRepository.save(user);
        } else {
            if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials."));
            }
        }

        List<SelectionItem> items = request.getItems() == null ? List.of()
                : request.getItems().stream()
                .filter(item -> item.getCode() != null && item.getName() != null && item.getLevel() != null)
                .map(item -> new SelectionItem(item.getCode(), item.getName(), item.getLevel()))
                .toList();
        if (items.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No selection items."));
        }
        selectionService.saveSelection(user, SelectionStatus.SAVED, items);
        if (session != null) {
            session.setAttribute("userId", user.getId());
        }
        return ResponseEntity.ok(Map.of("message", "Selection saved.", "email", user.getEmail()));
    }

    @PostMapping("/api/selection/save-session")
    public ResponseEntity<?> saveWithSession(@RequestBody SelectionItemsRequest request, HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated."));
        }
        UserAccount user = userRepository.findById((Long) userId).orElse(null);
        if (user == null) {
            session.invalidate();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated."));
        }
        List<SelectionItem> items = request.getItems() == null ? List.of()
                : request.getItems().stream()
                .filter(item -> item.getCode() != null && item.getName() != null && item.getLevel() != null)
                .map(item -> new SelectionItem(item.getCode(), item.getName(), item.getLevel()))
                .toList();
        if (items.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No selection items."));
        }
        selectionService.saveSelection(user, SelectionStatus.SAVED, items);
        return ResponseEntity.ok(Map.of("message", "Selection saved.", "email", user.getEmail()));
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> registerLegacy(@RequestBody SelectionAuthRequest request) {
        request.setMode("register");
        return saveWithAuth(request, null);
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> loginLegacy(@RequestBody SelectionAuthRequest request) {
        request.setMode("login");
        return saveWithAuth(request, null);
    }

    @org.springframework.web.bind.annotation.GetMapping("/api/selection/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
        UserAccount user = userRepository.findById((Long) userId).orElse(null);
        if (user == null) {
            session.invalidate();
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("email", user.getEmail());
        return ResponseEntity.ok(response);
    }


    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
