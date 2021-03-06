package com.example.demo.events;

//Ctrl + option + O -> remove imports was not needed.
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Event-REST-API Project")
                .description("REST-API Development with Springboot")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
//        //Given
//        String name = "Event";
//        String description = "Spring";
//
//        //When
//        Event event = new Event();
//        event.setName("Event");
//        event.setDescription(description);
//
//        //Then
//        assertThat(event.getName()).isEqualTo(name);
//        assertThat(event.getDescription()).isEqualTo(description);
        //refectoring : "Spring" 블럭 지정 후 option + Command + V , Local Variable

    }

    @Test
    @Parameters(method = "parametersForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
//        //Given
//        Event event = Event.builder()
//                .basePrice(basePrice)
//                .maxPrice(maxPrice)
//                .build();
//
//        //When
//        event.update();
//
//        //Then
//        assertThat(event.getfree).isEqualTo(isFree);
    }

    private Object[] parametersForTestFree() {
        return new Object[] {
                new Object[] {0,0,true},
                new Object[] {100,0,false},
                new Object[] {0,100,false},
                new Object[] {100,200,false}

        };
    }

    @Test
    public void testoffline() {
//        Event event = Event.builder()
//                .location("강남역 토즈")
//                .build();
//
//        //When
//        event.update();
//
//        //Then
//        assertThat(event.getoffline).isTrue();
//        event = Event.builder()
//                .location("강남역 토즈")
//                .build();
//
//        //When
//        event.update();
//
//        //Then
//        assertThat(event.getoffline).isFalse();

    }
}