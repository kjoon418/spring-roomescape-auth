package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.auth.SessionManager;
import roomescape.auth.UserSession;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class WebViewController {

    private final SessionManager sessionManager;

    @GetMapping({"", "/"})
    public String welcome() {
        return "index";
    }

    @GetMapping("/admin")
    public String adminHome() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/reservations")
    public String adminReservations() {
        return "admin/reservations";
    }

    @GetMapping("/admin/times")
    public String adminTimes() {
        return "admin/times";
    }

    @GetMapping("/admin/themes")
    public String adminThemes() {
        return "admin/themes";
    }

    @GetMapping("/user")
    public String userHome() {
        return "user/home";
    }

    @GetMapping("/user/reserve")
    public String userReserve(HttpServletRequest request, Model model) {
        UserSession userInfo = sessionManager.getUserInfo(request);
        if (userInfo != null) {
            model.addAttribute("currentUserId", userInfo.userId());
        }
        return "user/reserve";
    }

    @GetMapping("/user/reservations")
    public String userReservations() {
        return "user/reservations";
    }

    @GetMapping("/user/popular")
    public String userPopular() {
        return "user/popular";
    }
}
