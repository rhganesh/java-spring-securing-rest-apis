package io.jzheaux.springsecurity.resolutions;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class ResolutionController {
	private final ResolutionRepository resolutions;

	public ResolutionController(ResolutionRepository resolutions) {
		this.resolutions = resolutions;
	}

	@GetMapping("/resolutions")
	@PreAuthorize("hasAuthority('resolution:read')")
	public Iterable<Resolution> read() {
		return this.resolutions.findAll();
	}

	@GetMapping("/resolution/{id}")
	@PreAuthorize("hasAuthority('resolution:read')")
	@PostAuthorize("@post.authorize(#root)")
	@PostFilter("@post.filter(#root)")
	@CrossOrigin
	public Iterable<Resolution> read(@PathVariable("id") UUID id) {
		return this.resolutions.findById(id).stream().collect(Collectors.toList());
	}

	@PostMapping("/resolution")
	@PreAuthorize("hasAuthority('resolution:write')")
	public Resolution make(@CurrentUsername String owner, @RequestBody String text) {
		Resolution resolution = new Resolution(text, owner);
		return this.resolutions.save(resolution);
	}

	@PutMapping(path="/resolution/{id}/revise")
	@PostAuthorize("@post.authorize(#root)")
	@Transactional
	public Iterable<Resolution> revise(@PathVariable("id") UUID id, @RequestBody String text) {
		this.resolutions.revise(id, text);
		return read(id);
	}

	@PutMapping("/resolution/{id}/complete")
	@PostAuthorize("@post.authorize(#root)")
	@Transactional
	public Iterable<Resolution> complete(@PathVariable("id") UUID id) {
		this.resolutions.complete(id);
		return read(id);
	}
}
