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
   


 