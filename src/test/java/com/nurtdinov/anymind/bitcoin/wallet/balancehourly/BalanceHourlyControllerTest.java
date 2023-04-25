package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

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
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(BalanceHourlyController.class)
public class BalanceHourlyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BalanceHourlyService balanceHourlyService;

    @Autowired
    private JacksonTester<BalanceHourlyRequest> jsonRequestBalanceHourly;
    @Autowired
    private JacksonTester<List<BalanceHourly>> jsonResponseBalanceHourly;

    @Test
    public void getHourlyBalanceTest() throws Exception {
        // given
        OffsetDateTime startDateTime = OffsetDateTime.parse("2023-04-11T13:00:01+00:00");
        OffsetDateTime endDateTime = OffsetDateTime.parse("2023-04-11T15:59:59+00:00");
        BalanceHourlyRequest request = new BalanceHourlyRequest(startDateTime, endDateTime);

        BalanceHourly balanceHourlyRecord1 = new BalanceHourly(OffsetDateTime.parse("2023-04-11T14:00+00:00"),
                new BigDecimal("1000"));
        BalanceHourly balanceHourlyRecord2 = new BalanceHourly(OffsetDateTime.parse("2023-04-11T15:00+00:00"),
                new BigDecimal("2000"));
        List<BalanceHourly> expectedResponse = List.of(balanceHourlyRecord1, balanceHourlyRecord2);
        given(balanceHourlyService.getHourlyBalanceWithin(startDateTime, endDateTime))
                .willReturn(expectedResponse);

        // when
        MockHttpServletResponse response = mvc.perform(get("/balanceHourly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBalanceHourly.write(request).getJson()))
                .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonResponseBalanceHourly.write(expectedResponse).getJson());
    }
}
