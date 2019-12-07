package com.example.demo.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/*
    입력 값을 받는 부분을 DTO로 뻈음.
    Domain에 너무 많은 Annotaion이 붙어 번잡해지는 것을 예방.
    대신 Domain Variable 에 대한 중복이 생김. ( 변수를 다시 선언해야하기 때문.)
 */
@Builder @NoArgsConstructor @AllArgsConstructor
@Data
public class EventDto  {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    @NotNull
    private LocalDateTime closeEnrollmentDateTime;
    @NotNull
    private LocalDateTime beginEventDateTime;
    @NotNull
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    @Min(0)
    private int basePrice; // (optional)
    @Min(0)
    private int maxPrice; // (optional)
    @Min(0)
    private int limitOfEnrollment;

}
