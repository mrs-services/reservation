package mrs;

import mrs.domain.model.ReservableRoomId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
public class ReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
	}

	@Configuration
	static class RestConfig extends RepositoryRestConfigurerAdapter {
		@Override
		public void configureConversionService(
				ConfigurableConversionService conversionService) {
			conversionService.addConverter(String.class, LocalDate.class,
					s -> LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE));
			conversionService.addConverter(String.class, LocalTime.class,
					s -> LocalTime.parse(s, DateTimeFormatter.ISO_LOCAL_TIME));
			conversionService.addConverter(String.class, ReservableRoomId.class,
					ReservableRoomId::valueOf);
		}
	}
}
