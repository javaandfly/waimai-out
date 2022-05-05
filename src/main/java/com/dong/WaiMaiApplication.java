
package com.dong;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author dong
 * @create 2022/4/30
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.dong.mapper")
@EnableTransactionManagement
public class WaiMaiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaiMaiApplication.class,args);
        log.info("项目启动成功...");
    }

}
