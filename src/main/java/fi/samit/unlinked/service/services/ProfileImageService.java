package fi.samit.unlinked.service.services;

import fi.samit.unlinked.service.model.Account;
import fi.samit.unlinked.service.model.Messages;
import fi.samit.unlinked.service.model.ProfileImage;
import fi.samit.unlinked.service.repositories.AccountRepository;
import fi.samit.unlinked.service.repositories.MessageRepository;
import fi.samit.unlinked.service.repositories.ProfileImageRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ProfileImageService {

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private MessageRepository messageRepository;

    public void changeProfilePicture(String name, String data) throws IOException {
        Account loggedInUser = accountService.getAuthenticatedAccount();
        String imageDataBytes = data.substring(data.indexOf(",") + 1);
        InputStream stream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(imageDataBytes.getBytes()));

        //   ProfileImage profileImage = new ProfileImage();
        //   byte[] imageData = stream.readAllBytes();
        if (profileImageRepository.findByAccount(loggedInUser)
                == null && name.equals(loggedInUser.getUsername())) {
            ProfileImage profileImage = new ProfileImage();
            byte[] imageData = stream.readAllBytes();
            profileImage.setContent(imageData);
            profileImage.setAccount(loggedInUser);
            loggedInUser.setProfileImage(profileImage);
            accountRepository.save(loggedInUser);
            profileImageRepository.save(profileImage);
        } else if (profileImageRepository.findByAccount(loggedInUser)
                != null && name.equals(loggedInUser.getUsername())) {
            ProfileImage profileImage = new ProfileImage();
            byte[] imageData = stream.readAllBytes();
            profileImage = profileImageRepository.findByAccount(loggedInUser);
            profileImageRepository.delete(profileImage);
            accountService.deleteProfilePicture(loggedInUser);
            profileImage.setName(null);
            profileImage.setContent(null);
            profileImage.setAccount(loggedInUser);
            profileImage.setContent(imageData);
            loggedInUser.setProfileImage(profileImage);
            accountRepository.save(loggedInUser);
            profileImageRepository.save(profileImage);
        }
    }

    public ProfileImage getProfileImageByAccount(Account account) {
        return profileImageRepository.findByAccount(account);

    }

    public void deleteProfileImage(String name) {
        String username = accountService.getAuthenticatedAccount().getUsername();
        ProfileImage t = profileImageRepository.findByAccount(accountService.getAccount(name));             //tarkistetaan että kirjautunut käyttäjä
        if (name.equals(username) && t != null) {
            profileImageRepository.delete(t);
            Account account = accountRepository.findByUsername(username);
            account.setProfileImage(null);
            accountRepository.save(account);
            for (Messages message : messageRepository.findAll()) {
            if(message.getSender().getUsername().equals(username)){
                message.getSender().setProfileImage(null);
                messageRepository.save(message);
            }
        }
            
        }
    }

    public HashMap getAllProfileImages() throws UnsupportedEncodingException {
        HashMap byteImages = new HashMap();
        List<ProfileImage> images = profileImageRepository.findAll();
        for (ProfileImage img : images) {
            byte[] encode = Base64.getMimeEncoder().encode(img.getContent());
            byteImages.put(img.getAccount(), new String(encode, "UTF-8"));
        }
        return byteImages;
    }

    public void init() throws IOException {
        if (profileImageRepository.findByAccount(accountService.getAuthenticatedAccount()) == null) {
            Resource resource = new ClassPathResource("static/nopic.png");
            InputStream input = resource.getInputStream();
            byte[] is = input.readAllBytes();
            String encode = Base64.getMimeEncoder().encodeToString(is);
            // changeProfilePicture(accountService.getAuthenticatedAccount().getUsername(), encode);
            String imageDataBytes = encode.substring(encode.indexOf(",") + 1);
            InputStream stream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(imageDataBytes.getBytes()));
            ProfileImage profileImage = new ProfileImage();
            byte[] imageData = stream.readAllBytes();
            profileImage.setContent(imageData);
            profileImage.setAccount(accountService.getAuthenticatedAccount());
            profileImage.setName("defaultProfilePicture");
            profileImageRepository.save(profileImage);
        }
    }

}
