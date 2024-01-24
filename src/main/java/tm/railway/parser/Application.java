package tm.railway.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import tm.railway.parser.dictionaries.Dictionaries;
import tm.railway.parser.services.FilesHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static tm.railway.parser.dictionaries.Dictionaries.readSpr;

@SpringBootApplication
@RequiredArgsConstructor
public class Application {


	private final Dictionaries dictionaries;
	private final FilesHandler filesHandler;

	@PostConstruct
	public void init() throws IOException {

		Resource stanCodecNamesResource = new ClassPathResource("static/stan_codes_names.txt");
		Resource operNamesResource = new ClassPathResource("static/oper_names.txt");
		Resource arendaResource = new ClassPathResource("static/arenda.txt");

		dictionaries.setStanCodesNames(readSpr(stanCodecNamesResource));
		dictionaries.setOperNames(readSpr(operNamesResource));
		dictionaries.setArenda(readSpr(arendaResource));

	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
