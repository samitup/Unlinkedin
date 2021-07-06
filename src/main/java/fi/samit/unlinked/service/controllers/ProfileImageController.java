package fi.samit.unlinked.service.controllers;

import fi.samit.unlinked.service.services.AccountService;
import fi.samit.unlinked.service.services.ProfileImageService;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProfileImageController {

    @Autowired
    private ProfileImageService profileImageService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/kayttajat/{name}/profiilikuva")
    public String postProfileImage(@PathVariable String name, @RequestBody String data) throws IOException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        String formattedUsername = name.replace("+", " ");
        profileImageService.changeProfilePicture(formattedUsername, data);
        return "redirect:/kayttajat/" + encodedUsername;
    }

    @GetMapping("/kayttajat/{name}/profiilikuva")
    public String deleteProfileImage(@PathVariable String name) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        profileImageService.deleteProfileImage(name);
        return "redirect:/kayttajat/" + encodedUsername;
    }
}
