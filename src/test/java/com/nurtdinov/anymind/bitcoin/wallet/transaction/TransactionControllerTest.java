package com.nurtdinov.anymind.bitcoin.wallet.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private JacksonTester<TransactionDTO> jsonRequestTransaction;
    @Autowired
    private JacksonTester<Transaction> jsonResponseTransaction;

    @Test
    void postValidTransaction() throws Exception {
        // given
        OffsetDateTime dateTimeNow = OffsetDateTime.now();
        TransactionDTO request = new TransactionDTO(dateTimeNow, new BigDecimal("100"));
        Transaction expectedResponse = new Transaction(0L,
                dateTimeNow.withOffsetSameInstant(ZoneOffset.UTC), new BigDecimal("100"));
        given(transactionService.saveTransaction(any())).willReturn(expectedResponse);

        // when
        MockHttpServletResponse response = mvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestTransaction.write(request).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonResponseTransaction.write(expectedResponse).getJson());
    }

    @Test
    void postInvalidTransaction() throws Exception {
        // given
        OffsetDateTime dateTimeNow = OffsetDateTime.now().plusHours(1);
        TransactionDTO request = new TransactionDTO(dateTimeNow, new BigDecimal("100"));

        // when
        MockHttpServletResponse response = mvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestTransaction.write(request).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
