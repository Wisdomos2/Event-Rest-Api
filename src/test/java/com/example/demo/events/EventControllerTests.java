package com.example.demo.events;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {

    /*
    가짜 request, response를 이용해서 Test.
    Web과 관련된 Bean들만 등록하여 Test, Slice Test라고 함.
    Web Server를 띄우는 Test보다는 빠르지만 Unit Test보다는 느린 중간 정도의 test.
     */
    @Autowired
    MockMvc mockMvc;

    @Test
    public void createEvent() throws Exception {
        // perform 안이 요청, 요청이 갔으면 응답이 있음.
        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated());
    }

}
