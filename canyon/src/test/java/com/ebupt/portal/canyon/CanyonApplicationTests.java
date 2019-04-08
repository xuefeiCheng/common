package com.ebupt.portal.canyon;

import com.ebupt.portal.canyon.common.listener.ApplicationStartedEventListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(ApplicationStartedEventListener.class)
public class CanyonApplicationTests {

	@Test
	public void contextLoads() {
	}

}
