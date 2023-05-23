package com.hig.hwangingyu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hig.hwangingyu.service.ArticleService;
import com.hig.hwangingyu.service.MemberService;
import com.hig.hwangingyu.service.StreamService;
import com.hig.hwangingyu.domain.Member;
import com.hig.hwangingyu.domain.Stream;
import com.hig.hwangingyu.fake.FakeArticleRepository;
import com.hig.hwangingyu.fake.FakeMemberRepository;
import com.hig.hwangingyu.fake.FakeStreamRepository;
import com.hig.hwangingyu.repository.ArticleRepository;
import com.hig.hwangingyu.repository.MemberRepository;
import com.hig.hwangingyu.repository.StreamRepository;
import com.hig.hwangingyu.domain.Article;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@WebAppConfiguration
class HwangingyuApplicationTests {
	
	StreamRepository streamRepository;
	
	MemberRepository memberRepository;
	
	ArticleRepository articleRepository;
	
	StreamService streamService;
	
	MemberService memberService;
	
	ArticleService articleService;

	@BeforeEach
	public void BeforeEach() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		streamRepository = new FakeStreamRepository();
		memberRepository = new FakeMemberRepository();
		articleRepository = new FakeArticleRepository();

		streamService = new StreamService(streamRepository);
		memberService = new MemberService(memberRepository,passwordEncoder);
		articleService = new ArticleService(articleRepository);
	}

	@Test
	void contextLoads() {
		
	}

	@Test
	void StreamRegistTest() {

		for(int i=0;i<10;++i) {
			Member member = new Member.Builder()
			.setName("hello12"+(3+i))
			.setPasswd("12536")
			.build();

			memberService.register(member);
			Member v = memberService.findMember(member.getName()).get();
			assertEquals(member.getName(), v.getName());
		}

		streamService.registStream("hello124");
		streamService.registStream("hello125");
		streamService.registStream("hello126");
		streamService.registStream("hello123");
		streamService.registStream("hello124");
		streamService.registStream("hello125");
		streamService.registStream("hello126");

		for(int i=3;i<=6;++i) {
			String name = "hello12"+(i);

			Stream stream = streamService.findByid(name).get();
			String value = stream.getStreamer();
			assertEquals(name, value);
		}

		List<Stream> list = streamService.findAll( PageRequest.of(1,10)).getContent();

		for(Stream i : list) {
			System.out.println(i.getStreamer());
		}
		
		
		streamService.deleteStream("hello123");
		streamService.deleteStream("hello124");
		streamService.deleteStream("hello125");
		streamService.deleteStream("hello126");
		streamService.findAll( PageRequest.of(1,10));
	}

	@Test 
	void MemberTest() {
		for(int i=0;i<10;++i) {
			Member member = new Member.Builder()
			.setName("member"+i)
			.setPasswd("12536")
			.build();

			memberService.register(member);
			Member v = memberService.findMember(member.getName()).get();
			assertEquals(member.getName(), v.getName());
		}
	}


	@Test
	void ArticleTest() {
		Article article = new Article.Builder()
								.setAuthor("adw")
								.setBody("body")
								.setTitle("title")
								.build();
		articleService.uploadArticle(article);
		Article v = articleService.findById(article.getId()).get();
		assertEquals(v.getAuthor(), article.getAuthor());

	}
	

}
