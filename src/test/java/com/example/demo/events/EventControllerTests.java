package com.example.demo.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
// @WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    /*
    가짜 request, response를 이용해서 Test.
    Web과 관련된 Bean들만 등록하여 Test, Slice Test라고 함.
    Web Server를 띄우는 Test보다는 빠르지만 Unit Test보다는 느린 중간 정도의 test.
     */
    @Autowired
    MockMvc mockMvc;

    /*
        Autowired?
        ObjectMapper?
     */
    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;


    @Test
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Developerment with springboot")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .beginEventDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .endEventDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이태원 메이드ㅋㅋㅋㅋ ")
                .build();

        //Mockito.when(eventRepository.save(event)).thenReturn(event);


        // perform 안이 요청, 요청이 갔으면 응답이 있음.
        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                //해당 입력 막기.
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

    @Test
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Developerment with springboot")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .beginEventDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .endEventDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이태원 메이드ㅋㅋㅋㅋ ")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        //Mockito.when(eventRepository.save(event)).thenReturn(event);


        // perform 안이 요청, 요청이 갔으면 응답이 있음.
        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    //test 3개 실행을 다하려면, 메서드 밖에서 ctrl shift R
    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                    .andExpect(status().isBadRequest())
        ;
    }

}

