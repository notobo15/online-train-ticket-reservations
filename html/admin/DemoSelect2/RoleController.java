
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RoleController {

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/roles")
    public List<String> getRoles(@RequestParam(required = false) String search) {
        List<String> allRoles = Arrays.asList("Admin", "User", "Manager", "Supervisor", "Guest");

        if (search != null && !search.isEmpty()) {
            return allRoles.stream()
                    .filter(role -> role.toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return allRoles;
    }
}
