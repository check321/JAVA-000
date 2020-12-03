package net.check321.databasedemo;

import lombok.extern.slf4j.Slf4j;
import net.check321.databasedemo.entity.User;
import net.check321.databasedemo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class DynamicSourceNodeTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void readWriteShardingTest(){

        User user = new User();
        user.setUserName("sharding_test_02");
        user.setUserCode("sharding_02");
        user.setPassword("123456");

        userMapper.save(user);
        log.info("save record [{}] success.",user.getId());

        final User userData = userMapper.getById(1000003L);
        log.info("query record: {}",userData.toString());

    }
}
