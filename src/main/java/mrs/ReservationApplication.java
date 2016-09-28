package mrs;

import mrs.domain.model.MeetingRoom;
import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.*;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
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

	@Profile("!cloud")
	@Bean
	CommonsRequestLoggingFilter requestDumperFilter() {
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter() {
			@Override
			protected boolean shouldLog(HttpServletRequest request) {
				return logger.isInfoEnabled();
			}

			@Override
			protected void beforeRequest(HttpServletRequest request, String message) {
				logger.info(message);
			}

			@Override
			protected void afterRequest(HttpServletRequest request, String message) {
				logger.info(message);
			}
		};
		filter.setIncludePayload(true);
		filter.setIncludeClientInfo(true);
		filter.setIncludeHeaders(false);
		filter.setIncludeQueryString(true);
		filter.setMaxPayloadLength(2048000);
		return filter;
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
