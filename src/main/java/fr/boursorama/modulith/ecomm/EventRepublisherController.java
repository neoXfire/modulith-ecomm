package fr.boursorama.modulith.ecomm;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/events")
@SecurityRequirement(name = "basicAuth")
@Tag(name = SecurityConfig.OPEN_API_SECURED_TAG_NAME)
public class EventRepublisherController {

	private final IncompleteEventPublications republisher;

	@Autowired
	public EventRepublisherController(IncompleteEventPublications republisher) {
		this.republisher = republisher;
	}

	@PostMapping("/resubmit")
	public void republishNotCompletedEvents() {
		republisher.resubmitIncompletePublications(eventPublication -> true);
	}

}
