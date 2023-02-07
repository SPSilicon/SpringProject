# SpringProject

스프링 공부


기능--

<h2>로그인, 회원가입</h2>
<h2>글쓰기 삭제 수정 열람</h2>

<ul>
Database : Mariadb

spring dataSource driver : mysql

jdbcTemplate사용
</ul>








<h2>기기 영상/녹음 장치 공유</h2>


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

