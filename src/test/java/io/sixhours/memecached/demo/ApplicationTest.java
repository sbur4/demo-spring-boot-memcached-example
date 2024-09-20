package io.sixhours.memecached.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CacheManager cacheManager;

	@Before
	public void setUp() {
		cacheManager.getCache("books").clear();
	}

	@Test
	public void thatSearchReturnsAllBooks() {
		String expected = "[{\"id\":1,\"title\":\"Kotlin in Action\",\"year\":2017},{\"id\":2,\"title\":\"Spring Boot in Action\",\"year\":2016},{\"id\":3,\"title\":\"Programming Kotlin\",\"year\":2017},{\"id\":4,\"title\":\"Kotlin\",\"year\":2017}]";

		ResponseEntity<String> response = restTemplate.getForEntity("/books", String.class);

		assertThat(response.getBody(), is(expected));
	}

	@Test
	public void thatSearchByTitleReturnsBook() {
		String expected = "[{\"id\":4,\"title\":\"Kotlin\",\"year\":2017}]";

		ResponseEntity<String> response = restTemplate.getForEntity("/books/Kotlin", String.class);

		assertThat(response.getBody(), is(expected));
	}

	@Test
	public void thatDeleteAndRecacheReturnsBooksWithoutKotlinTitle() {
		String expected = "[{\"id\":1,\"title\":\"Kotlin in Action\",\"year\":2017},{\"id\":2,\"title\":\"Spring Boot in Action\",\"year\":2016},{\"id\":3,\"title\":\"Programming Kotlin\",\"year\":2017}]";

		restTemplate.delete("/books/Kotlin");

		ResponseEntity<String> response = restTemplate.getForEntity("/books", String.class);

		assertThat(response.getBody(), is(expected));
	}

	@Test
	public void thatCacheManagerIsLoaded() {
		assertThat(cacheManager, instanceOf(ConcurrentMapCacheManager.class));
	}
}
