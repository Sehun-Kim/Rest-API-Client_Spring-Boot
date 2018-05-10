# Spring Boot Rest API Client
#### Rest API Client

###### Tomcat 9.0 
###### Spring Boot : 2.0.1

### ver 1.0
- DB에서 데이터를 읽어 JSON 타입으로 데이터를 리턴하는 API에Post 방식으로 데이터 요청
- 응답받은 데이터로 csv파일을 만듬

### ver 2.0
- 요청 방식 변경: DB 데이터를 가져오는 것이 아닌 요청의 성공여부만 응답으로 받음
- 요청을 하나의 API 서버가 아닌 여러 API서버로 보냄 : 멀티쓰레드 방식 (ThreadPool, ServletContextListener, ConcurrentLinkedQueue)
- 요청은 비동기적으로 여러번 가능
