package com.example.demo.events;


import com.example.demo.common.TestDescription;
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
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
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
    @TestDescription("입력 값이 비어있는 경우 에러를 발생시키는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                    .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("잘못된 값이 들어있는 경우 에러를 발생시키는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception{
        EventDto eventdto = EventDto.builder()
                .name("Spring")
                .description("REST API Developerment with springboot")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .beginEventDateTime(LocalDateTime.of(2019, 12, 12, 18, 57))
                .endEventDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이태원 메이드ㅋㅋㅋㅋ ")
                .build();
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventdto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
        ;
    }


}

