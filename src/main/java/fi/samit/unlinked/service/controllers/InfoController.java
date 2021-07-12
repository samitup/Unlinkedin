package fi.samit.unlinked.service.controllers;

import fi.samit.unlinked.service.services.UserInfoService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/kayttajat/{name}/info")
    public String postInfo(@PathVariable String name,
            @RequestParam(required = false) String infoContent) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        userInfoService.postInfo(name, infoContent);
        return "redirect:/kayttajat/" + encodedUsername;

    }

    @GetMapping("/kayttajat/{name}/info/{id}")
    public String deleteInfo(@PathVariable String name,
            @PathVariable Long id) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        userInfoService.deleteInfo(name, id);
        return "redirect:/kayttajat/" + encodedUsername;
    }
}
