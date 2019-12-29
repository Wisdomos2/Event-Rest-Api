package com.example.demo.events;


import com.example.demo.accounts.Account;
import com.example.demo.accounts.AccountRepository;
import com.example.demo.accounts.AccountRole;
import com.example.demo.accounts.AccountService;
import com.example.demo.common.AppProperties;
import com.example.demo.common.BaseControllerTest;
import com.example.demo.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTests extends BaseControllerTest {

    /*
    가짜 request, response를 이용해서 Test.
    Web과 관련된 Bean들만 등록하여 Test, Slice Test라고 함.
    Web Server를 띄우는 Test보다는 빠르지만 Unit Test보다는 느린 중간 정도의 test.
     */

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @Before
    public void setUp() {
        this.eventRepository.deleteAll();
        this.accountRepository.deleteAll();
    }



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
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                                linkWithRel("update-event").description("link to update an existing"),
                                linkWithRel("profile").description("link to update an existing event")
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

    private String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .Id(100)
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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우 에러를 발생시키는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventdto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        //Given
        IntStream.range(0,30).forEach(i->{
            this.generateEvent(i);
        });


        //When & Then
        this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size","10")
                    .param("sort","DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;
    }

    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception {
        //given
        Event event = this.generateEvent(10);

        //when & then
        this.mockMvc.perform(get("/api/events/{id}",event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
        ;


    }


    @Test
    @TestDescription("없는 이벤트 조회했을 때 404 응답")
    public void getEvent404() throws Exception {
        //given
        Event event = this.generateEvent(100);

        //when & then
        this.mockMvc.perform(get("/api/event/1233"))
                .andExpect(status().isNotFound())
        ;


    }


    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        //Given
        Event event = this.generateEvent(100);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        String eventName = "Updated Event";
        eventDto.setName(eventName);

        //When & Then

        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"))
        ;
    }

    @Test
    @TestDescription("입력된 값이 비어있는 경우 이벤트 수정 실패")
    public void updateEvent400_Empty() throws Exception {
        //Given
        Event event = this.generateEvent(10);
        EventDto eventDto = new EventDto();

        //When & Then

        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력된 값이 잘못된 경우 이벤트 수정 실패")
    public void updateEvent400_Wrong() throws Exception {
        //Given
        Event event = this.generateEvent(10);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        //When & Then

        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        //When & Then

        this.mockMvc.perform(put("/api/events/12344444")
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }


    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("Spring")
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .beginEventDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .endEventDateTime(LocalDateTime.of(2019, 9, 8, 18, 57))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("노원구 중계본동")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return this.eventRepository.save(event);
    }

    //Access token 을 꺼내는 method.
    public String getAccessToken() throws Exception{
        Account junseo = Account.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(junseo);


        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret())) //basic Auth 라는 header를 만듦.
                .param("username",appProperties.getUserUsername()) // Token을 만들기 위한 3가지 변수 ( 위에서 고의로 만듦.)
                .param("password",appProperties.getUserPassword())
                .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists()
                );

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
    }


}

