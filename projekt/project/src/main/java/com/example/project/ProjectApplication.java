package com.example.project;

import com.example.project.model.Address;
import com.example.project.model.Person;
import com.example.project.model.mappers.AddressMapper;
import com.example.project.model.mappers.PersonMapper;
import com.example.project.repository.AddressRepository;
import com.example.project.repository.PersonRepository;
import com.example.project.services.PersonService;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {
	private final PersonRepository personRepository;
	private final AddressRepository addressRepository;
	private final PersonService personService;

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return builder;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Running...");

		var addresses = getAddresses().stream().map(AddressMapper::toDTO).toList();
		var persons = getPeople().stream().map(PersonMapper::toDTO).toList();

		persons.forEach(personService::save);

		for(int i = 0; i < addresses.size(); i++)
		{
			personService.addAddress(i+1, addresses.get(i));
		}
	}

	private static List<Person> getPeople() {
		Person p1 = new Person(1, "Michael", "Stanford", "mstanford@gmail.com", "Male", "mstanford");
		Person p2 = new Person(2, "Rachel", "Bullet", "rachelBullet@yahoo.com", "Female", "rbullet");
		Person p3 = new Person(3, "Mike", "Guletto", "mikeGul@gmail.com", "Male", "superMike");
		Person p4 = new Person(4, "Trevor", "Pacino", "superTrevor@box.com", "Non-specified", "travorFromGTA");
		Person p5 = new Person(5, "Adam", "Kolodziej", "polandProPlayer@gmail.com", "Male", "adamMickiewicz");
		var persons = List.of(p1, p2, p3, p4, p5);
		return persons;
	}

	private static List<Address> getAddresses() {
		Address a1 = new Address(1,"USA", "Florida", "24-345", "Rancher", "The Old Street 53");
		Address a2 = new Address(2,"Poland", "Pomorskie", "56-245", "Gdynia", "Wojska Polskiego 24");
		Address a3 = new Address(3,"Poland", "Pomorskie", "12-456", "Gdansk", "Wielkoposlki 67");
		Address a4 = new Address(4,"Poland", "Pomorskie", "98-432", "Radom", "Staromiejska 21");
		Address a5 = new Address(5,"USA", "Alabama", "253-33", "Old Town", "Modern Vibes 25");
		var addresses = List.of(a1, a2, a3, a4, a5);
		return addresses;
	}
}
