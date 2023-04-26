# SpringProject

![스크린샷(320)](https://user-images.githubusercontent.com/44769598/231676714-2fbfea3f-985f-4ce2-9461-79f51a6484d5.png) 


# 사용 기술 

### backend
 * Mariadb
 * JdbcTempate  
 * Spring Boot 3
 * Spring Security
 * JWT 
 * ffmpeg >= 4.3
### frontend
 * thymeleaf 
 * javascript 
 * sock.js 
 * webRTC
 * bootstrap 


# 프로젝트 구조  

##### config  
└ Config.java   
└ OAuth2LoginConfig.java   
└ SecurityConfig.java   
└ WebSocketConfig.java   
##### controller  
└ error  
　└ CustomExceptionHandler.java  
└ handler  
　└ LoginSuccessHandler.java  
　└ SocketWebmHandler.java  
└ ArticleController.java  
└ HomeController.java  
└ MemberController.java  
└ StreamController.java
##### domain
└ Article  
└ KakaoOAuth2User  
└ Member  
└ Stream  
##### filter  
└ JwtAuthenticationFilter  
##### repository  
└ ArticleJdbcRepository  
└ ArticleRepository  
└ MemberRepository  
└ MemberJdbcRepository  
└ StreamRepository  
└ StreamJdbcRepository  
##### service  
└ ArticleService  
└ MemberService  
└ StreamService  
└ CustomOAuth2UserService  


# DB
![springproj](https://user-images.githubusercontent.com/44769598/234611756-d6b1ca2d-009e-466d-95ea-b847d8cd56f3.svg)


# Endpoint Mappings

##### /higlogin [GET]  
##### /home [GET]  
##### /member/register [POST]  
##### /post [DELETE]  
##### /post [GET]  
##### /post [POST]  
##### /post [PUT]  
##### /post/add [GET]  
##### /post/update [GET]  
##### /stream/play/{streamName} [GET]  
##### /stream/share [GET]  
##### /streams [GET]  

<h2>로그인, 회원가입</h2>  

![springLogin](https://user-images.githubusercontent.com/44769598/234212419-c9d4e117-03b3-4dae-b19b-f3378cbc1f18.svg)  

![springMember](https://user-images.githubusercontent.com/44769598/234212446-d23ddfa2-9fe5-4fb5-aebc-3101deaa3bfb.svg)  

![스크린샷(321)](https://user-images.githubusercontent.com/44769598/231676783-97039bfe-3a6c-4424-ac11-bd3355a72860.png)  

### OAuth2 카카오 로그인 구현
##### 로그인 성공시 JWT 토큰 생성후 Cookie에 저장 이후 로그인 인증 필요시 쿠키 확인.
### Remember me 미구현  

![캡처](https://user-images.githubusercontent.com/44769598/232427086-6f39e6a2-06c3-416e-8111-5888b6389c6b.PNG)  
회원가입 시 cloudflare에서 제공하는 봇체크 사용  

<h2>글쓰기 삭제 수정 열람</h2>  

![springArticle](https://user-images.githubusercontent.com/44769598/234212346-f537e706-b3d1-434e-917b-82f59de5ce7e.svg)  

### 글쓰기
![스크린샷(323)](https://user-images.githubusercontent.com/44769598/231677798-78fd1747-1fdc-4ca9-8938-0da6b8ff45c8.png)
### 글쓰기 이후
![스크린샷(322)](https://user-images.githubusercontent.com/44769598/231677539-45ce9709-e60b-45ab-81fc-42e3067903af.png)
### 글
![스크린샷(324)](https://user-images.githubusercontent.com/44769598/231678101-ec1e668d-d57d-40ad-a2db-dc7ed0e1d843.png)
### 수정
![스크린샷(325)](https://user-images.githubusercontent.com/44769598/231678114-c0b9aab8-7d2a-4eb7-a2f9-3f2c93bc423d.png)
### 수정 후
![스크린샷(326)](https://user-images.githubusercontent.com/44769598/231678780-69a90223-5b66-47af-b7c5-40f319046d42.png)

<h2>기기 영상/소리 녹음 장치 공유</h2>

![springStream](https://user-images.githubusercontent.com/44769598/234212965-88dc4ac5-695d-41b6-8216-529af6775c3d.svg)


<h3>webRTC</h3>


[WebRTC samples](https://webrtc.github.io/samples/)

<h3>ffmpeg</h3>


[ffmpeg commandline builder](https://github.com/peterchave/ffbuilder) <br>
[ffmpeg documentation](https://ffmpeg.org/ffmpeg.html)


<h3>sock.js</h3>  
[sock.js github](https://github.com/sockjs/sockjs-client)  

### 영상 캡쳐 페이지  
![스크린샷(327)](https://user-images.githubusercontent.com/44769598/231680811-12d848e0-ea3a-47fd-b9f8-089b87f15a6e.png)  
![스크린샷(328)](https://user-images.githubusercontent.com/44769598/231680989-66259f4b-bd65-432c-af44-83ab72f890c7.png)

### 스트림 목록 페이지
![캡처](https://user-images.githubusercontent.com/44769598/232487625-3a334daf-f68c-4104-a25e-180f50b119b9.PNG)

### 영상 시청
![캡처2](https://user-images.githubusercontent.com/44769598/232526253-8bd6e4c9-0c18-4296-8152-e66f0ce523d8.PNG)





녹음 하기전, 브라우저와 spring서버를 websocket으로 연결

연결 성공시, ffmpeg 서브프로세스를 생성하고 로그인한 아이디를 key로 한 HashMap에 프로세스의 참조를 저장
 
webRTC를 사용하여 영상/소리 녹음

일정 시간마다 서버로 아이디와 데이터 보냄

서버에서는 데이터를 받아 해당하는 아이디의 ffmpeg 서브프로세스에 데이터를 보낸다.

ffmpeg가 데이터를 받고, 썸네일용 사진, mpd 파일과 각각의 segment파일을 static/stream/(아이디) 에 저장한다.


<h1>application.properties 설정</h1>

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.datasource.username= db접속 id

spring.datasource.password= db접속 pw

spring.datasource.url=jdbc:mysql:// <db주소>:<포트>/<db이름>

spring.thymeleaf.cache=false

server.ssl.key-store= ssl 인증서 파일 이름

server.ssl.key-store-type=PKCS12

server.ssl.key-store-password= ssl 인증서 암호

server.ssl.key-alias= ssl 인증서 이름

server.port = 8443

logging.config = classpath:log4j2.xml

server.tomcat.basedir=.

server.tomcat.accesslog.enabled=true

server.tomcat.accesslog.directory=log

server.tomcat.accesslog.pattern=%h %a %u %t "%r" %s %b %D

oauth.kakao.clientid = 카카오톡 oauth api 공개키

oauth.kakao.secret = 카카오톡 oauth api 비공개키

captcha.secret = cloudflare turnstile captcha 비공개키

jwt.secret = jwt 비공개 키

