package mrs.domain.service.reservation;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.Set;

import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationUser implements Serializable {
	private static final ObjectMapper objectMapper = new ObjectMapper() {
		{
			this.registerModule(new JavaTimeModule());
			this.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		}
	};

	private String userId;
	private String userName;
	private String displayName;
	private String clientId;
	private Set<String> scope;
	private Set<String> authorities;
	private Instant exp;

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getClientId() {
		return clientId;
	}

	public Set<String> getScope() {
		return scope;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	public Instant getExp() {
		return exp;
	}

	@Override
	public String toString() {
		return "ReservationUser{" + "userId='" + userId + '\'' + ", userName='" + userName
				+ '\'' + ", displayName='" + displayName + '\'' + ", clientId='"
				+ clientId + '\'' + ", scope=" + scope + ", authorities=" + authorities
				+ ", exp=" + exp + '}';
	}

	public static ReservationUser fromJwtAccessToken(String accessToken) {
		String payload = accessToken.split("\\.")[1];
		try {
			byte[] json = Base64Utils.decodeFromUrlSafeString(payload);
			ReservationUser user = objectMapper.readValue(json, ReservationUser.class);
			return user;
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
