package fi.samit.unlinked.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.assertTrue;
import fi.samit.unlinked.service.services.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationAndLoginControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    public void testRegistrationStatusIsOk() throws Exception {
        mockMvc.perform(get("/rekisteroidy"))
                .andExpect(status().isOk());

    }

    @Test
    public void testLoginStatusIsOk() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

    }

    @Test
    public void testRegistrationPageContainsWord() throws Exception {
        MvcResult res = mockMvc.perform(get("/rekisteroidy")).andReturn();

        String content = res.getResponse().getContentAsString();
        assertTrue(content.contains("Rekisteröidy"));
    }

    @Test
    public void testRedirectToLoginPageAfterRegistration() throws Exception {
        mockMvc.perform(post("/rekisteroidy")
                .param("username", "testuser")
                .param("password", "testpassword"))
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testRegisteredAccountIsFoundFromDatabase() throws Exception {
        mockMvc.perform(post("/rekisteroidy")
                .param("username", "testuser")
                .param("password", "testpassword"))
                .andReturn();

        assertTrue(accountService.getAllAccounts().stream()
                .anyMatch(account -> account.getUsername().equals("testuser")));
    }
}
