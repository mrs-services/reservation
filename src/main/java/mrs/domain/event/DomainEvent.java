package mrs.domain.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(name = ReserveEvent.TYPE, value = ReserveEvent.class),
		@JsonSubTypes.Type(name = CancelEvent.TYPE, value = CancelEvent.class) })
public interface DomainEvent {
}