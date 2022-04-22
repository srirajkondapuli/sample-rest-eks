package com.myown.app.sample.api;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.myown.app.sample.api.CredentialController;
import com.myown.app.sample.service.CredentialProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@SpringBootTest
@AutoConfigureMockMvc
@EnableCaching
public class CredentialControllerTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    CredentialProvider credentialProvider;


	@BeforeEach
	public void setup() throws Exception {


		mvc = MockMvcBuilders.standaloneSetup(new CredentialController(credentialProvider)).build();
	}

    @Test

    public void getCredential() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/credential/id").accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("dummySecret")));
    }
}
