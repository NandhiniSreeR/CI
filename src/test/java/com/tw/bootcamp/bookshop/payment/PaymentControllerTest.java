package com.tw.bootcamp.bookshop.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@WithMockUser
class PaymentControllerTest {

    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnSuccessWhenInvoked() throws Exception {
        CreditCardDetailsRequest ccRequest =
                CreditCardDetailsRequest.builder()
                .cardHolderName("John Smith")
                .cardNumber(4311_5678_8987_3456L)
                .expiresOn(Date.from(Instant.now().plusMillis(10000)))
                .cvv(123)
                .build();

        mockMvc.perform(post("/payments/creditcards")
                        .content(objectMapper.writeValueAsString(ccRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void shouldReturnErrorWhenInvokedWithInvalidCardNumber() throws Exception {
        CreditCardDetailsRequest ccRequest = CreditCardDetailsRequest.builder()
                .cardHolderName("John Smith")
                .cardNumber(4311_5678_8987L)
                .expiresOn(Date.from(Instant.now()))
                .cvv(12356)
                .build();

        mockMvc.perform(post("/payments/creditcards")
                        .content(objectMapper.writeValueAsString(ccRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }
}