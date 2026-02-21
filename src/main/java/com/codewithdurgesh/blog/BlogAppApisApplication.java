package com.codewithdurgesh.blog;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.codewithdurgesh.blog.config.AppConstants;
import com.codewithdurgesh.blog.entities.Role;
import com.codewithdurgesh.blog.repositories.RoleRepo;

@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	// 🔥 AUTO CREATE ROLES ON STARTUP
	@Override
	public void run(String... args) throws Exception {

		if (roleRepo.count() == 0) {

			Role admin = new Role();
			admin.setId(AppConstants.ADMIN_USER);
			admin.setName("ROLE_ADMIN");

			Role normal = new Role();
			normal.setId(AppConstants.NORMAL_USER);
			normal.setName("ROLE_NORMAL");

			roleRepo.save(admin);
			roleRepo.save(normal);

			System.out.println("DEFAULT ROLES INSERTED !");
		}
	}
}