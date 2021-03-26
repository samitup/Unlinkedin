package fi.samit.unlinked.service.Controllers;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.ProfileImage;
import fi.samit.unlinked.service.model.UserInfo;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fi.samit.unlinked.service.services.AccountService;
import fi.samit.unlinked.service.services.MessageService;
import fi.samit.unlinked.service.services.ProfileImageService;
import fi.samit.unlinked.service.services.UserInfoService;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProfileController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ProfileImageService profileImageService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/kayttajat/{name}")
    public String getProfilePage(Model model, @PathVariable String name) throws UnsupportedEncodingException, IOException {
        profileImageService.init();
        String formattedName = name.replace("+", " ");
        String currentUser = accountService.getAuthenticatedAccount().getUsername();
        Account account = accountService.getAccount(formattedName);
        if (account == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find user: " + name);
        }
        model.addAttribute("account", account);
        model.addAttribute("currentUser", currentUser);

        model.addAttribute("messages", messageService.getMessagesByAccount(account));

        List<UserInfo> info = userInfoService.getUserInfo(account);
        if (info != null) {
            model.addAttribute("infos", info);
        }
        ProfileImage image = profileImageService.getProfileImageByAccount(account);
        if (image != null) {
            byte[] encode = Base64.getMimeEncoder().encode(image.getContent());
            model.addAttribute("imageData", image);
            model.addAttribute("imageContent", new String(encode, "UTF-8"));
        }

        return "profilePage";
    }

    @PostMapping("/kayttajat/{name}/kommentit")
    public String postMessage(@PathVariable String name, @RequestParam(required = false) String post) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        messageService.postMessage(name, post);
        UserController asd = new UserController();
        return "redirect:/kayttajat/" + encodedUsername;
    }

    @GetMapping("/kayttajat/{name}/kommentit/{id}")
    public String deleteMessage(@PathVariable String name, @PathVariable Long id) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        messageService.deleteMessage(name, id);
        return "redirect:/kayttajat/" + encodedUsername;
    }

    @PostMapping("/kayttajat/{name}/info")
    public String postInfo(@PathVariable String name, @RequestParam(required = false) String infoContent) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        userInfoService.postInfo(name, infoContent);
        return "redirect:/kayttajat/" + encodedUsername;

    }

    @GetMapping("/kayttajat/{name}/info/{id}")
    public String deleteInfo(@PathVariable String name, @PathVariable Long id) throws UnsupportedEncodingException {
        String encodedUsername = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        userInfoService.deleteInfo(name, id);
        return "redirect:/kayttajat/" + encodedUsername;
    }

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
