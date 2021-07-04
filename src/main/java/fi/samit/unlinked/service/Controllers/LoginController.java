package fi.samit.unlinked.service.Controllers;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.services.AccountService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class LoginController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/kayttajat")
    public String loginRedirect() throws UnsupportedEncodingException {
        String username = accountService.getAuthenticatedAccount().getUsername();
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
        return "redirect:/kayttajat/" + encodedUsername;
    }

    @GetMapping("/rekisteroidy")
    public String registrationPage(@ModelAttribute Account account) {
        return "registrationPage";
    }

    @PostMapping("/rekisteroidy")
    public String registerNewUser(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
        accountService.getAllAccounts().stream().filter((name) -> (name.getUsername().equals(account.getUsername()))).forEachOrdered((_item) -> {
            bindingResult.rejectValue("username", "error.username", "Käyttäjänimi on jo varattu!");
        });

        if (bindingResult.hasErrors()) {
            return "registrationPage";
        }
        if (accountService.getAuthenticatedAccount() != null) {
            return "redirect:/login";
        }
        accountService.save(account);
        return "redirect:/login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/rekisteroidy";
    }
}
