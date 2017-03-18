package com.baturu.demo;

import com.baturu.demo.bean.ModulerSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootDemoApplication {

	@Value("${module.author}")
	private String moduleAuthor;
	@Value("${module.name}")
	private String moduleName;

	@Autowired
	private ModulerSettings ModulerSettings;

	@RequestMapping("/")
	String index() {
		System.out.println("module name is :" +ModulerSettings.getName());
		return "module name is :" + moduleName + " and module authro is : " + moduleAuthor;
	}

	public static void main(String[] args) {
//		SpringApplication app = new SpringApplication(SpringbootDemoApplication.class);
//		app.setShowBanner(false);
//		app.run(args);
		SpringApplication.run(SpringbootDemoApplication.class, args);
	}
}
