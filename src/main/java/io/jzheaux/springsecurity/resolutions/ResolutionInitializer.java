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
		this.resolutions.save(new Resolution("Read War and Peace", "user"));
		this.resolutions.save(new Resolution("Free Solo the Eiffel Tower", "user"));
		this.resolutions.save(new Resolution("Hang Christmas Lights", "user"));

		//create new user
		User user = new User("user",
				"{bcrypt}$2a$10$MywQEqdZFNIYnx.Ro/VQ0ulanQAl34B5xVjK2I/SDZNVGS5tHQ08W");

		//create read user
		User hasReadUser = new User("hasread",
				"{bcrypt}$2a$10$I/.a1H7Li.YM33wrEJulBulgD9gGB9ai5mG9L83zWYQrwP90h.KIa");

		//create write user
		User hasWriteUser = new User("haswrite",
				"{bcrypt}$2a$10$KOiZ..3R8LM/qSJVRvODgeNmLkfVuaVW350uZAyxuMFSsNrFZFYK6");

		//add authority grant
		user.grantAuthority("resolution:read");
		hasReadUser.grantAuthority("resolution:read");
		hasWriteUser.grantAuthority("resolution:write");

		//save user
		this.users.save(user);
		this.users.save(hasReadUser);
		this.users.save(hasWriteUser);

	}

}
