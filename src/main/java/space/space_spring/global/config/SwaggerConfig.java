package space.space_spring.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;

@OpenAPIDefinition(
        servers = {
                @Server(url = "/"),
        }
)
@Configuration
public class SwaggerConfig {

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(JwtLoginAuth.class);
    }

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "Authorization";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components().addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT"));


        Info info = new Info()
                .version("v2.0")
                .title("Space API")
                .description("동아리 통합 관리 서비스 [Space] API");

        return new OpenAPI()
                .components(components)
                .info(info)
                .addSecurityItem(securityRequirement);

    }
}
