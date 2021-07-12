package fi.samit.unlinked.service.controllers;

import fi.samit.unlinked.service.services.AccountService;
import fi.samit.unlinked.service.services.MessageService;
import fi.samit.unlinked.service.services.ProfileImageService;
import fi.samit.unlinked.service.services.ReplyService;
import fi.samit.unlinked.service.services.UserInfoService;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ProfileImageService profileImageService;

    @GetMapping("/kayttajat")
    public String listAllUsers(Model model) throws UnsupportedEncodingException, IOException {
        Map image = profileImageService.getAllProfileImages();
        if (image != null) {
            model.addAttribute("images", image);
        }
        if (image == null) {
            Resource resource = new ClassPathResource("static/nopic.png");
            InputStream input = resource.getInputStream();
            byte[] is = input.readAllBytes();
            byte[] encode = Base64.getMimeEncoder().encode(is);
            model.addAttribute("images", new String(encode, "UTF-8"));
        }

        return "usersPage";
    }

    @PostMapping("/etsi")
    public String searchUser(Model model,
            @RequestParam String nameToSearch
    ) {
        if (accountService.getAccount(nameToSearch) != null) {
            return "redirect:/kayttajat/" + nameToSearch;
        }
        return "redirect:/kayttajat";
    }
}
