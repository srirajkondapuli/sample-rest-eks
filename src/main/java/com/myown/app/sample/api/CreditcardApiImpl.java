package com.myown.app.sample.api;

import static org.springframework.http.HttpStatus.OK;
import javax.validation.Valid;
import com.myown.app.sample.api.CreditcardApi;
import com.myown.app.sample.model.DetokenizeRequest;
import com.myown.app.sample.model.DetokenizeResponse;
import com.myown.app.sample.model.TokenizeRequest;
import com.myown.app.sample.model.TokenizeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.opentelemetry.extension.annotations.SpanAttribute;
import io.opentelemetry.extension.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CreditcardApiImpl implements CreditcardApi {
	public static final String SAMPLE_TOKEN = "hello tokenization";
	public static final String SAMPLE_CARD = "4444444444444444";
	@RequestMapping(method = RequestMethod.POST, value = "/creditcard/detokenization", produces = { "application/json",
			"application/xml" }, consumes = { "application/json", "application/xml" })
	@WithSpan()
	public ResponseEntity<DetokenizeResponse> detokenizeCreditCard(@SpanAttribute("detokenizeRequest")
			@Valid @RequestBody DetokenizeRequest detokenizeRequest) {
		log.info("Credit Card Detokenization!!");
		log.info("De-Tokenization Request : {}", detokenizeRequest);
		DetokenizeResponse response = new DetokenizeResponse();
		response.setCreditCardNumber(SAMPLE_CARD);
		return new ResponseEntity<>(response, OK);
	}

	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/creditcard/tokenization", produces = { "application/json",
			"application/xml" }, consumes = { "application/json", "application/xml" })
	public ResponseEntity<TokenizeResponse> tokenizeCreditCard(@Valid @RequestBody TokenizeRequest tokenizeRequest) {
		log.info("Credit Card Tokenization!!");
		log.info("Tokenization Request : {}", tokenizeRequest);
		TokenizeResponse response = new TokenizeResponse();
		response.setCreditCardToken(SAMPLE_TOKEN);
		return new ResponseEntity<>(response, OK);
	}

}
