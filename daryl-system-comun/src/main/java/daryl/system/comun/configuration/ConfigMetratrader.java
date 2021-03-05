package daryl.system.comun.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@PropertySource("classpath:metatrader.properties")
@ConfigurationProperties(prefix = "metatrader")
@Component
class ConfigMetratrader {

	@Getter @Setter private String installation;
	@Getter @Setter private String terminal;
	@Getter @Setter private String folderRoot;
	@Getter @Setter private String folderTest;
	@Getter @Setter private String folderReal;
	@Getter @Setter private String extension;
}
