package fi.samit.unlinked.service.controllers;

import fi.samit.unlinked.service.annotations.ValidImage;
import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.services.AlbumService;
import fi.samit.unlinked.service.model.ImageObject;
import fi.samit.unlinked.service.services.AccountService;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
@Validated
@Controller
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/kayttajat/{profileName}/albumi")
    public String postImageToAlbum(@PathVariable String profileName, @ValidImage @RequestParam("file") MultipartFile file) throws IOException {
        String encodedUsername = URLEncoder.encode(profileName, StandardCharsets.UTF_8.toString());
        String formattedUsername = profileName.replace("+", " ");
        albumService.postImageToAlbum(formattedUsername, file);
        return "redirect:/kayttajat/" + encodedUsername + "/albumi";
    }

    @GetMapping("/albumi")
    public String album() throws UnsupportedEncodingException {
        String username = accountService.getAuthenticatedAccount().getUsername();
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
        return "redirect:/kayttajat/" + encodedUsername + "/albumi";
    }

    // Get album of user
    @GetMapping("/kayttajat/{profileName}/albumi")
    public String getAlbum(@PathVariable String profileName, Model model) {
        String currentUser = accountService.getAuthenticatedAccount().getUsername();
        String formattedUsername = profileName.replace("+", " ");
        Account account = accountService.getAccount(formattedUsername);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("account", account);
        model.addAttribute("pictures", albumService.getImagesOfUser(account));
        return "albumPage";
    }

    @PostMapping("/kayttajat/{profileName}/albumi/{imageId}")
    public String deleteImage(@PathVariable String profileName, @PathVariable Long imageId) throws UnsupportedEncodingException {
        String username = accountService.getAuthenticatedAccount().getUsername();
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
        String formattedUsername = profileName.replace("+", " ");
        if (formattedUsername.equals(username)) {
            albumService.deleteImageById(imageId);
        }
        return "redirect:/kayttajat/" + encodedUsername + "/albumi";
    }

    //Get picture from album
    @GetMapping("/kayttajat/{profileName}/albumi/{imageId}")
    public String getImage(Model model, @PathVariable Long imageId, @PathVariable String profileName) throws UnsupportedEncodingException {
        String currentUser = accountService.getAuthenticatedAccount().getUsername();
        String formattedUsername = profileName.replace("+", " ");
        Account account = accountService.getAccount(formattedUsername);
        String encodedUsername = URLEncoder.encode(profileName, StandardCharsets.UTF_8.toString());
        if (!albumService.imageExistById(imageId)) {
            return "redirect:/kayttajat/" + encodedUsername;
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("account", account);
        byte[] encode = albumService.getImageContentById(imageId);
        model.addAttribute("imageContent", new String(encode, "UTF-8"));

        List<ImageObject> images = albumService.getImagesByAccount(profileName);
        model.addAttribute("count", images.size());
        Optional<ImageObject> next = images.stream().filter(img -> img.getId() > imageId).findFirst();
        Collections.reverse(images);
        Optional<ImageObject> previous = images.stream().filter(img -> img.getId() < imageId).findFirst();

        model.addAttribute("next", next.isPresent() ? next.get().getId() : null);
        model.addAttribute("previous", previous.isPresent() ? previous.get().getId() : null);
        model.addAttribute("current", imageId);
        return "imagePage";
    }
}
