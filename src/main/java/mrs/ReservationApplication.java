package mrs;

import mrs.domain.model.ReservableRoomId;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

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
