package net.check321.databasedemo.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class BatchInsertByJDBC {

    private static final String URL = "jdbc:mysql://localhost:3306/mall-demo?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowMultiQueries=true&useAffectedRows=true&rewriteBatchedStatements=true";

    private static final String U_NAME = "root";

    private static final String PWD = "";

    private static final String MOCK_USER_TEMPLETE = "U{0,number,#},name_{1,number,#},12345";

    private static final int TOTAL_COUNT = 1000000;

    private static final int STEP_COUNT = 100000; // commit in every 10k records .

    public static void main(String[] args) {

        List<String> users = new ArrayList<>(TOTAL_COUNT);
        // mock-data template: usercode,username,password
        for (int i = 1; i <= TOTAL_COUNT; i++) {
            users.add(MessageFormat.format(MOCK_USER_TEMPLETE,i,i));
        }

        long start = System.currentTimeMillis();
        execInBatch(users);
        log.info("total spend: [{}] ms ", System.currentTimeMillis() - start);
    }


    private static void execInBatch(List<String> users){

        String sql = "insert into t_user (user_code,user_name,password) values (?,?,?)";
        final AtomicInteger counter = new AtomicInteger(0);

        try(Connection conn = DriverManager.getConnection(URL,U_NAME,PWD)){
            try(PreparedStatement ps = conn.prepareStatement(sql)){

                // begin tx.
                conn.setAutoCommit(false);
                users.stream()
//                        .parallel()
                            .forEach(u -> {
                                try {
                                    // mock user-data
                                    String[] mockData = u.split(",");
                                    ps.setString(1, mockData[0]);
                                    ps.setString(2, mockData[1]);
                                    ps.setString(3, mockData[2]);
                                    // execute by batch.
                                    ps.addBatch();
                                    counter.incrementAndGet();
                                    // commit once in every 10k times.
                                    if(counter.get() > 0 && counter.get() % STEP_COUNT == 0) {
                                        ps.executeBatch();
                                        log.info("flush data index [{}]",counter.get());
                                        conn.commit();
                                        ps.clearBatch();
                                    }


                            } catch (Exception e) {
                                log.error("insert error ,total count: [{}]",counter.get(),e);
                            }
                        }

                );
            }catch (Exception e){
                log.error("executor occurs an error.",e);
                conn.rollback();
            }finally {
                conn.setAutoCommit(true);
            }

            log.info("total insert [{}] records.",counter.get());

        } catch (Exception e) {
            log.error("jdbc error.",e);
        }
    }


}
