package com.example.demo.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {


    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            errors.rejectValue("basePirce","WrongValue","BaseValue is wrong");
            errors.rejectValue("maxPirce","WrongValue","MaxValue is wrong");

        }

        LocalDateTime endEventDataTime = eventDto.getEndEventDateTime();
        if(endEventDataTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDataTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDataTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDataTime", "WrongValue", "endEventDataTime is wrong");
        }

        //TODO beginDatatie
        //TODO closeEntrollmentDatetime..
    }
}
