package mrs;

import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.sampler.PercentageBasedSampler;
import org.springframework.cloud.sleuth.sampler.SamplerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfig {

	@Bean
	public Sampler traceSampler(SamplerProperties config) {
		return new HystrixStreamDisabledSampler(new PercentageBasedSampler(config));
	}

	static class HystrixStreamDisabledSampler implements Sampler {
		private final Sampler delegate;

		HystrixStreamDisabledSampler(Sampler delegate) {
			this.delegate = delegate;
		}

		@Override
		public boolean isSampled(Span span) {
			String spanName = span.getName();
			if ("send-metrics".equals(spanName) || "gather-metrics".equals(spanName)) {
				return false;
			}
			return delegate.isSampled(span);
		}
	}
}
