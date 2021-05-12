package cn.solwind.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//@ServletComponentScan("cn.solwind.admin")
@EnableTransactionManagement
@MapperScan("cn.solwind.admin.mapper")
public class AdminBootServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminBootServerApplication.class, args);
	}

}
