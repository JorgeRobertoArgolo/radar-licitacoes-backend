package br.com.jorgeargolo.radar_licitacoes_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RadarLicitacoesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RadarLicitacoesBackendApplication.class, args);
	}

}
