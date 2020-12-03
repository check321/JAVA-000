package net.check321.databasedemo.mapper;

import net.check321.databasedemo.annotation.ReadOnly;
import net.check321.databasedemo.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @ReadOnly
    @Results({
            @Result(property = "userCode",column = "user_code"),
            @Result(property = "userName",column = "user_name")
    })
    @Select("select * from t_user where id = #{id}")
    User getById(@Param("id") Long id);

    @Insert("insert into t_user (user_code,user_name,password) values (#{userCode},#{userName},#{password})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int save(User user);

}
