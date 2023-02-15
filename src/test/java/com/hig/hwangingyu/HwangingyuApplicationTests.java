package com.hig.hwangingyu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.hig.hwangingyu.service.ArticleService;
import com.hig.hwangingyu.service.MemberService;
import com.hig.hwangingyu.service.StreamService;
import com.hig.hwangingyu.domain.Member;
import com.hig.hwangingyu.domain.Stream;
import com.hig.hwangingyu.domain.Article;

import java.util.List;


@SpringBootTest
@WebAppConfiguration
@Transactional
class HwangingyuApplicationTests {


	@Autowired
	StreamService streamService;

	@Autowired
	MemberService memberService;

	@Autowired
	ArticleService articleService;

	@Test
	void contextLoads() {
		
	}

	@Test
	void StreamTest() {
		for(int i=0;i<10;++i) {
			Member member = new Member.Builder()
			.setName("hello12"+(3+i))
			.setPasswd("12536")
			.build();

			memberService.register(member);
		}
		streamService.registStream("hello124");
		streamService.registStream("hello125");
		streamService.registStream("hello126");
		streamService.registStream("hello123");
		streamService.registStream("hello124");
		streamService.registStream("hello125");
		streamService.registStream("hello126");
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
		}
	Member check = new Member.Builder()
		.setName("member1")
		.setPasswd("12536")
		.build();
	memberService.register(check);

	Member check2 = new Member.Builder()
		.setName("member10")
		.setPasswd("12536")
		.build();
	memberService.register(check2);
	}

	@Test
	void ArticleTest() {
		Article article = new Article.Builder()
								.setAuthor("adw")
								.setBody("body")
								.setTitle("title")
								.build();
		articleService.uploadArticle(article);

		
	}
	

}
