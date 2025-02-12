package fr.boursorama.modulith.ecomm;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

	private EntityNotFoundException(String message) {
		super(message);
	}

	public static EntityNotFoundException of(String entityType, String idName) {
		final String message = String.format("No existing %s with that %s", entityType, idName);
		return new EntityNotFoundException(message);
	}
}
