package mrs;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import mrs.domain.model.MeetingRoom;
import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableResourceServer
@EnableBinding(ReservationSink.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
	}

	@Profile("!cloud")
	@Bean
	RequestDumperFilter requestDumperFilter() {
		return new RequestDumperFilter();
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
		return new Jackson2ObjectMapperBuilder()
				.modulesToInstall(new Jackson2HalModule(), new JavaTimeModule())
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.deserializers(new StdDeserializer<ReservableRoom>(ReservableRoom.class) {
					@Override
					public ReservableRoom deserialize(JsonParser jsonParser,
							DeserializationContext deserializationContext)
							throws IOException {
						return new ReservableRoom(
								ReservableRoomId.valueOf(jsonParser.getValueAsString()));
					}
				});
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

		@Override
		public void configureRepositoryRestConfiguration(
				RepositoryRestConfiguration config) {
			config.exposeIdsFor(ReservableRoom.class, MeetingRoom.class,
					Reservation.class);
		}
	}

	// Workarround http://stackoverflow.com/a/30713264/5861829
	@Configuration
	static class TxConfig {
		@Autowired
		PlatformTransactionManager transactionManager;

		@Bean
		TransactionInterceptor txAdvice() {
			NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
			source.addTransactionalMethod("post*", new DefaultTransactionAttribute());
			source.addTransactionalMethod("put*", new DefaultTransactionAttribute());
			source.addTransactionalMethod("delete*", new DefaultTransactionAttribute());
			source.addTransactionalMethod("patch*", new DefaultTransactionAttribute());
			return new TransactionInterceptor(transactionManager, source);
		}

		@Bean
		Advisor txAdvisor() {
			AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
			pointcut.setExpression(
					"execution(* org.springframework.data.rest.webmvc.RepositoryEntityController.*(..))");
			return new DefaultPointcutAdvisor(pointcut, txAdvice());
		}
	}
}
