package com.dargon.reggie;

import com.dargon.reggie.utils.SMSUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RegginTakeOutApplicationTests {

	@Test
	void contextLoads() {

		SMSUtils.sendMessage("瑞吉外卖","SMS_249280277","18174885629","12345");
	}

}
