package com.example.messenger.chat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootTest
class ApplicationTests {

	@MockBean
	private InMemoryUserDetailsManager userDetailsService;

	@MockBean
	private List<String> usernames;

	@Test
	void contextLoads() {
	}

}
