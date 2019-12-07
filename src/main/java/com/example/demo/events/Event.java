package com.example.demo.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
    Command + shift + t -> 해당 테스트 코드로 이동.
    Lombok Annotation을 이용, Build 할 때 알아서 해당 Annotation Code를 추가, Compile 같이 됨.
    @EqualIsAndHshCode of에 id를 준 이유, 나중에 참조나 비교시 모든 변수를 가지고 작업이 이루어지면
    StackOverFlow 발생우려. id로만 진행하기 위해서 설정.
    @Data를 안쓰는 이유도, 들어가보면 @EqualsAndHashCode가 변수설정을 하지 않은 채 달려있음.
    즉 상호참조와 관련하여 StackOvefFlow 발생가능성 있음.
 */


@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        // Update free
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }
        // Update offline
        if (this.location == null || this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }

}
