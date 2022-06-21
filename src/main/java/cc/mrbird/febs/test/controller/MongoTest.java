package cc.mrbird.febs.test.controller;

import cc.mrbird.febs.test.entity.TestUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    boolean useMongoDB = true;

    @Test
    public void test1() {
        if(useMongoDB){
            List<TestUser> userList = mongoTemplate.findAll(TestUser.class);
            if (userList != null && userList.size() > 0) {
                userList.forEach(user -> {
                    System.out.println(user.toString());
                });
            }
        }


    }
}
