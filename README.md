# SpringProject

<h2>로그인, 회원가입</h2>
<h2>글쓰기 삭제 수정 열람</h2>

<ul>
<li>Database : Mariadb</li>

<li>spring dataSource driver : mysql</li>

<li>jdbcTemplate사용</li>
</ul>



<h2>기기 영상/소리 녹음 장치 공유</h2>

<h3>webRTC</h3>


[WebRTC samples](https://webrtc.github.io/samples/)

<h3>ffmpeg</h3>


[ffmpeg commandline builder](https://github.com/peterchave/ffbuilder) <br>
[ffmpeg documentation](https://ffmpeg.org/ffmpeg.html)

<h3>sock.js</h3>


[sock.js github](https://github.com/sockjs/sockjs-client)


녹음 하기전, 브라우저와 spring서버를 websocket으로 연결

연결 성공시, ffmpeg 서브프로세스를 생성하고 로그인한 아이디를 key로 한 HashMap에 핸들 저장
 
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

