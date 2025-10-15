//package com.example.taskManager;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class TaskManagerApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//	 @Test
//    void mainMethodRuns() {
//        TaskManagerApplication.main(new String[]{}); // ✅ Covers line 10 and 11
//    }
//
//
//}

package com.example.taskManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskManagerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethodRuns() {
        TaskManagerApplication.main(new String[]{});
    }
}
