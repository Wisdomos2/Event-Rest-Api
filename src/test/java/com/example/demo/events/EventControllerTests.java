package com.example.demo.events;


import com.example.demo.common.RestDocsConfiguration;
import com.example.demo.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
// @WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-event").description("link to update an existing")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content_tpye haader")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of beginEnrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollmentDateTime"),
                                fieldWithPath("beginEventDateTime").description("date time of beginEventDateTime"),
                                fieldWithPath("endEventDateTime").description("date time of endEventDateTime"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("BasePrice of new event"),
                                fieldWithPath("maxPrice").description("M axPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")

                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        //prefix relax 붙이면 일부분만 문서화 할수 있음. ( 전부다 안하면 error 남.)
                        relaxedResponseFields(
                                fieldWithPath("id").description("Id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of beginEnrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollmentDateTime"),
                                fieldWithPath("beginEventDateTime").description("date time of beginEventDateTime"),
                                fieldWithPath("endEventDateTime").description("date time of endEventDateTime"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("BasePrice of new event"),
                                fieldWithPath("maxPrice").description("M axPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("free yes or no"),
                                fieldWithPath("offline").description("offline yes or no"),
                                fieldWithPath("eventStatus").description("eventStatus yes or no")
                        )
                        ))
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

