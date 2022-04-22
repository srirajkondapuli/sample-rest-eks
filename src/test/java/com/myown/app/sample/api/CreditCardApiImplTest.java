package com.myown.app.sample.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myown.app.sample.api.CreditcardApiImpl;
import com.myown.app.sample.model.DetokenizeRequest;
import com.myown.app.sample.model.TokenizeRequest;
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
public class CreditCardApiImplTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;


	@BeforeEach
	public void setup() throws Exception {


		mvc = MockMvcBuilders.standaloneSetup(new CreditcardApiImpl()).build();
	}

	@Test
	public void tokenizeCreditCard() throws Exception {
		TokenizeRequest request = new TokenizeRequest();
		request.setCreditCardNumber(CreditcardApiImpl.SAMPLE_CARD);
		String jsonRequest = mapper.writeValueAsString(request);
		mvc.perform(MockMvcRequestBuilders.post("/creditcard/tokenization").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)).andExpect(status().isOk())
				.andExpect(content().json("{\n" + "  \"creditCardToken\": \"hello tokenization\"\n" + "}"));
	}
    @Test
	public void badRoletokenizeCreditCard() throws Exception {
		TokenizeRequest request = new TokenizeRequest();
		request.setCreditCardNumber(CreditcardApiImpl.SAMPLE_CARD);
		String jsonRequest = mapper.writeValueAsString(request);
		mvc.perform(MockMvcRequestBuilders.post("/creditcard/tokenization").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)).andExpect(status().isOk());
	}

	@Test

	public void deTokenizeCreditCard() throws Exception {
		DetokenizeRequest request = new DetokenizeRequest();
		request.setCreditCardToken(CreditcardApiImpl.SAMPLE_TOKEN);
		String jsonRequest = mapper.writeValueAsString(request);
		mvc.perform(MockMvcRequestBuilders.post("/creditcard/detokenization").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)).andExpect(status().isOk())
				.andExpect(content().json("{\n" + "  \"creditCardNumber\": \"4444444444444444\"\n" + "}"));
	}

	@Test

	public void tokenizeCreditCard_400() throws Exception {
		TokenizeRequest request = new TokenizeRequest();
		request.setCreditCardNumber("asd");
		String jsonRequest = mapper.writeValueAsString(request);
		mvc.perform(MockMvcRequestBuilders.post("/creditcard/tokenization").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)).andExpect(status().is4xxClientError());
	}

	@Test

	public void deTokenizeCreditCard_400() throws Exception {
		DetokenizeRequest request = new DetokenizeRequest();
		request.setCreditCardToken("123");
		String jsonRequest = mapper.writeValueAsString(request);
		mvc.perform(MockMvcRequestBuilders.post("/creditcard/detokenization").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)).andExpect(status().is4xxClientError());
	}
}
