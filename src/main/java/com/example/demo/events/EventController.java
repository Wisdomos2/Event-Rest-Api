package com.example.demo.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
/*
    위의 RequestMapping 으로 아래 Class는 해당 /api/events 로 base로 url을 사용,
    HAL_JSON_UTF8_타입으로 data를 보내게 됨.
 */
public class EventController {
    /*
        ResponseEntitly 가 뭔지?
        통신 메시지 관련 header와 body의 값들을 하나의 객체로 저장하는 것이 HttpEntity class
        Request 부분일 경우 HttpEntity를 상속받은 RequestEntity,
        Response 부분일 경우 HttpEntity를 상속받은 ResponseEntity.
     */
    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        /*
            using HATEOS's linkTO, methodOn
         */
        URI createdUri = linkTo(EventController.class).slash("{id}").toUri();
        event.setId(10);
        return ResponseEntity.created(createdUri).body(event);
    }
}
