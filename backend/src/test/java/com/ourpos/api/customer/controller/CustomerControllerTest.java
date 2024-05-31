package com.ourpos.api.customer.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourpos.api.customer.service.CustomerServiceImpl;
import com.ourpos.api.order.service.OrderQueryService;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerServiceImpl customerServiceImpl;

    @MockBean
    OrderQueryService orderQueryService;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("고객 정보 요청")
    @Test
    void findCustomer() throws Exception {
        // given
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/my"))
            .andExpect(status().is3xxRedirection())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(redirectedUrl("http://localhost/oauth2/authorization/naver"));
    }

}