package mrs.domain.service.notification;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationClient {
	private final RestTemplate restTemplate;
	private final Logger log = LoggerFactory.getLogger(NotificationClient.class);

	public NotificationClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void createNotification(Type type, String message, String userId,
			String accessToken) {
		Map<String, Object> json = new HashMap<>();
		json.put("notificationType", type.name());
		json.put("notificationMessage", message);
		json.put("userId", userId);
		log.info("create notification {}", json);
		restTemplate
				.exchange(
						post(fromHttpUrl("http://notification")
								.pathSegment("v1", "notifications").build().toUri())
										.header(HttpHeaders.AUTHORIZATION,
												"Bearer " + accessToken)
										.body(json),
						Void.class);
	}

	public enum Type {
		INFO, WARN, ERROR
	}
}
