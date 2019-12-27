# Event Rest Api project
- Api : Application Programming Interface <br>
- Rest : REpresentational State Transfer
- 인터넷 상의 시스템 간의 상호 운용성(interoperability)을 제공하는 방법중 하나
- 시스템 제각각의 독립적인 진화를 보장하기 위한 방법
- REST API: REST 아키텍처 스타일을 따르는 API


## Point
Hateoas 를 만족하고, Self-descriptive message 를 만족하는 Rest-Api를 개발.<br>
 : 두 조건을 만족해야 완전한 Rest Api 임.
 
 - Self-descriptive message<br>
   메시지 스스로 메시지에 대한 설명이 가능해야 한다.
   서버가 변해서 메시지가 변해도 클라이언트는 그 메시지를 보고 해석이 가능하다.
   확장 가능한 커뮤니케이션
   
  - HATEOAS<br>
   하이퍼미디어(링크)를 통해 애플리케이션 상태 변화가 가능해야 한다.
   링크 정보를 동적으로 바꿀 수 있다. (Versioning 할 필요 없이!)


## Functions
 - GET /api/events <br>
이벤트 목록 조회 REST API (로그인 안 한 상태)<br>
이벤트 목록 조회 REST API (로그인 한 상태)
- POST /api/events <br>
이벤트 생성
- GET /api/events/{id} <br>
  이벤트 하나 조회
- PUT /api/events/{id} <br>
  이벤트 수정

## Using skills
- Spring framework
- Spring boot
- Spring data JPA
- Spring HATEOAS
- Spring REST Docs
- Spring Security OAuth2

## Memo
- Preference
  - Plug-in - lombok Plugin install
  - Annotation Processor - Enable (비동작 가능성 있기 때문.)
- Lombok Annotation을 이용, Build 할 때 알아서 해당 Annotation Code를 추가, Compile 같이 됨.
- @EqualIsAndHshCode of에 id를 준 이유<br> 
나중에 참조나 비교시 모든 변수를 가지고 작업이 이루어지면
      StackOverFlow 발생우려. 
      <br>id로만 진행하기 위해서 설정.
      @Data를 안쓰는 이유도, 들어가보면 @EqualsAndHashCode가 변수설정을 하지 않은 채 달려있음.<br>
      즉 상호참조와 관련하여 StackOvefFlow 발생가능성 있음.
 
 - Replace fo refectoring : "Spring" 블럭 지정 후 Option + Command + V , Local Variable
 - Autowired?
   - Springframework에서 지원하는 각 상황에 맞는 의존성 주입 Annotation.
 - ObjectMapper?
   - 데이터를 Json으로 바꿔주는 class.
 - mockMvc.perform(요청)
 - ResponseEntitly 가 뭔지? <br>
          통신 메시지 관련 header와 body의 값들을 하나의 객체로 저장하는 것이 HttpEntity class<br?
          Request 부분일 경우 HttpEntity를 상속받은 RequestEntity,<br>
          Response 부분일 경우 HttpEntity를 상속받은 ResponseEntity.<br>
 - Serialize : 직렬화, 객체를 전송가능한 Json 형태로 만드는 것.
 - Deserialize : 역직렬화, Json형태를 객채로 만드는 것.
 - assertThat ( p1, p2) : matcher에 의해 두 인자를 비교함. http://sejong-wiki.appspot.com/assertThat
 - Spring Docs 적용 : http://woowabros.github.io/experience/2018/12/28/spring-rest-docs.html
 
 
 
 ## Api 문서화
 - 요청 본문 문서화
 - 응답 본문 문서화
 - 링크 문서화
 - self
 - query-events
 - update-event
 - profile 링크 추가
 - 요청 헤더 문서화
 - 요청 필드 문서화
 - 응답 헤더 문서화
 - 응답 필드 문서화
 
 - Relaxed 접두어
   - 장점: 문서 일부분만 테스트 할 수 있다.
   - 단점: 정확한 문서를 생성하지 못한다.
   
   
### Flow
 - Event.class : Model data
 - EventDto.class : Model data의 Validate를 위한 semi-Model data.
 - EventController.cass : get,put request에 대한 response를 하기 위함.
 - EventResource.interface : Spring JPA 위한 interface.
 - EventResource.class : Hateoas를 위해 Link를 달아주기 위함.
 - EventStatus.class : Enum data.
 - EventValidator.class  :
 <br><br>
 - TestDescription.@interface : Annotation customize(@TestDescription)
  

### 단축키
 - 줄 단위 재정렬 ctrl + alt + I <br>
 - 필요없는 import 제거 : Ctrl + Option + O <br>
 - 변수로 빼내기 : option + Command + V <br>
 - 메소드로 빼내기 : option + command + M <br>
 - 해당 테스트 코드로 이동 : Command + Shift + T<br>
 - Select Method to Override/implement : ctrl + O <br>
 - 참고 : https://lalwr.blogspot.com/2018/04/intellij.html
 - 
   


### Todo
- Docs적용 :  http://woowabros.github.io/experience/2018/12/28/spring-rest-docs.html