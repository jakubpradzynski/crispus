package pl.jakubpradzynski.crispus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of Crispus application.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@SpringBootApplication
public class CrispusApplication {

	/**
	 * Main function of Crispus application which starts Spring Application
	 * @param args - arguments from command line
	 */
	public static void main(String[] args) {
		SpringApplication.run(CrispusApplication.class, args);
	}
}
