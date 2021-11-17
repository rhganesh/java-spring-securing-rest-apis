package io.jzheaux.springsecurity.resolutions;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

@Component
public class ResolutionInitializer implements SmartInitializingSingleton {
	private final ResolutionRepository resolutions;
	private final UserRepository users;

	public ResolutionInitializer(ResolutionRepository resolutions, UserRepository users) {
		this.resolutions = resolutions;
		this.users = users;
	}

	@Override
	public void afterSingletonsInstantiated() {
		this.resolutions.save(new Resolution("Read War and Peace", "haswrite"));
		this.resolutions.save(new Resolution("Free Solo the Eiffel Tower", "haswrite"));
		this.resolutions.save(new Resolution("Hang Christmas Lights", "haswrite"));

		//create new user
		User user = new User("user",
				"{bcrypt}$2a$10$MywQEqdZFNIYnx.Ro/VQ0ulanQAl34B5xVjK2I/SDZNVGS5tHQ08W");

		User admin = new User("admin",
				"{bcrypt}$2a$10$ugiHACBoqlI68Q3.hLSvVua1YJjP3Y1F1SrQxNeH76/JGCKe4PJtK");

		//create read user
		User hasReadUser = new User("hasread",
				"{bcrypt}$2a$10$ugiHACBoqlI68Q3.hLSvVua1YJjP3Y1F1SrQxNeH76/JGCKe4PJtK");

		//create write user
		User hasWriteUser = new User("haswrite",
				"{bcrypt}$2a$10$ugiHACBoqlI68Q3.hLSvVua1YJjP3Y1F1SrQxNeH76/JGCKe4PJtK");

		//add authority grant
		user.grantAuthority("resolution:read");

		//admin
		admin.grantAuthority("ROLE_ADMIN");

		//hasread
		hasReadUser.grantAuthority("resolution:read");

		//haswrite
		hasWriteUser.grantAuthority("resolution:write");

		//save user
		this.users.save(user);
		this.users.save(hasReadUser);
		this.users.save(hasWriteUser);
		this.users.save(admin);
	}

}
