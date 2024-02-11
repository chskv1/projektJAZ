package com.example.project;

import com.example.project.exceptions.PersonNotFound;
import com.example.project.model.Address;
import com.example.project.model.Person;
import com.example.project.model.dto.AddressDTO;
import com.example.project.model.dto.PersonDTO;
import com.example.project.model.mappers.AddressMapper;
import com.example.project.model.mappers.PersonMapper;
import com.example.project.repository.AddressRepository;
import com.example.project.repository.PersonRepository;
import com.example.project.services.PersonService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.hibernate.Hibernate;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectApplicationTests {

	@Autowired
	PersonRepository personRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	PersonService personService;

	@LocalServerPort
	private Integer port;

	@AfterEach
	void setup()
	{
		RestAssured.baseURI = "http://localhost:" + port;

		//USUWANIE WSZYSTKICH ENCJI PRZED TESTEM
		addressRepository.deleteAll();
		personRepository.deleteAll();

		var addresses = getAddresses().stream().map(AddressMapper::toDTO).toList();
		var persons = getPeople().stream().map(PersonMapper::toDTO).toList();

		persons.forEach(personDTO -> personService.save(personDTO));

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

	// REPOSITORY TESTS ---------------------------------------------------------------------------------------------------

	@Test
	void repositoriesSize() {
		assertEquals(5, addressRepository.count());
		assertEquals(5, personRepository.count());
	}

	@Test
	void repositoryFindById() {
		assertEquals(3, personRepository.findById(3).get().getId());
		assertEquals(5, addressRepository.findById(5).get().getId());
	}

	@Test
	void repositoryPersonUpdate() {
		Person nP = new Person(1, "Maciej", "Czapliński", "maczaplinski@gmail.com", "Male", "maciej");
		personRepository.updateById(nP.getFirstName(), nP.getSecondName(), nP.getEmail(), nP.getGender(), nP.getUsername(), nP.getId());
		assertEquals(nP.getFirstName(), personRepository.findById(1).get().getFirstName());
	}

	@Test
	void repositoryAddressesForPerson()
	{
		assertEquals(1, addressRepository.findAddressesByPerson(personRepository.findById(1).get()).size());
		assertEquals("USA", addressRepository.findAddressesByPerson(personRepository.findById(1).get()).get(0).getCountry());
	}

	@Test
	void repositoryDeleteAddress()
	{
		Address address = addressRepository.findById(1).get();
		addressRepository.deleteAddressByStateAndCity(address.getState(), address.getCity());

		assertEquals(4, addressRepository.count());
	}



	// SERVICE TESTS ------------------------------------------------------------------------------------------------------

	@Test
	void serviceFindAll()
	{
		assertEquals(5, personService.getAll().size());
	}

	@Test
	void serviceGetById()
	{
		assertEquals("Michael",personService.getById(1).getFirstName());
	}

	@Test
	void serviceSave()
	{
		PersonDTO personDTO = new PersonDTO(6,  "Maciej", "Czapliński", "maczaplinski@gmail.com", "Male", "maciej");
		personService.save(personDTO);
		assertDoesNotThrow(() -> personService.getById(6));
		assertEquals(6, personService.getPersonsCount());
	}

	@Test
	void serviceUpdate()
	{
		PersonDTO personDTO = new PersonDTO(1,  "Maciej", "Czapliński", "maczaplinski@gmail.com", "Male", "maciej");
		personService.update(personDTO, 1);
		assertEquals("Maciej", personService.getById(1).getFirstName());
		assertEquals(5, personService.getPersonsCount());
	}

	@Test
	void serviceDelete()
	{
		personService.delete(1);
		assertThrows(PersonNotFound.class, () -> personService.getById(1));
		assertEquals(4, personService.getPersonsCount());
	}

	@Test
	void serviceAddAddress()
	{
		AddressDTO addressDTO = new AddressDTO(6, "Poland", "Pomorskie", "85-243", "Bydgoszcz", "Pionierska 24");
		personService.addAddress(1, addressDTO);

		assertEquals(6, addressRepository.count());
		assertEquals(addressDTO.getStreetName(), addressRepository.findById(6).get().getStreetName());
	}

	@Test
	void serviceGetAddress()
	{
		assertEquals(5, addressRepository.count());
	}

	// CONTROLLER TESTS ---------------------------------------------------------------------------------------------------

	@Test
	void controllerGetAll()
	{
		given()
				.contentType(ContentType.JSON)
				.when()
				.get("/persons")
				.then()
				.statusCode(HttpStatus.OK.value())
				.contentType(ContentType.JSON)
				.body("", hasSize(5));
	}

	@Test
	void controllerCreatePerson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", 6);
		jsonObject.put("first_name", "Maciej");
		jsonObject.put("second_name", "Czapliński");
		jsonObject.put("email", "mczaplinski@gmail.com");
		jsonObject.put("gender", "Male");
		jsonObject.put("username", "mczaplinski");

		given()
				.contentType(ContentType.JSON)
				.body(jsonObject.toString())
				.when()
				.post("/persons")
				.then()
				.contentType(ContentType.JSON)
				.statusCode(HttpStatus.CREATED.value())
				.body("id", equalTo(6));
	}

	@Test
	void controllerGetPersonById()
	{
		given()
				.contentType(ContentType.JSON)
				.when()
				.get("persons/1")
				.then()
				.contentType(ContentType.JSON)
				.statusCode(HttpStatus.OK.value())
				.body("id", equalTo(1));
	}

	@Test
	void controllerGetPersonByIdParam()
	{
		given()
				.contentType(ContentType.JSON)
				.get("/persons?id=1")
				.then()
				.contentType(ContentType.JSON)
				.statusCode(HttpStatus.OK.value())
				.body("id", equalTo(1));
	}

	@Test
	void controllerGetPersonByIdParamAndAddress()
	{
		given()
				.contentType(ContentType.JSON)
				.get("/persons?id=1&addresses=true")
				.then()
				.contentType(ContentType.JSON)
				.statusCode(HttpStatus.OK.value())
				.body("id", equalTo(1))
				.body("addresses", hasSize(1));
	}

	@Test
	void controllerUpdatePersonById()
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", 1);
		jsonObject.put("first_name", "Maciej");
		jsonObject.put("second_name", "Czapliński");
		jsonObject.put("email", "mczaplinski@gmail.com");
		jsonObject.put("gender", "Male");
		jsonObject.put("username", "mczaplinski");

		given()
				.contentType(ContentType.JSON)
				.body(jsonObject.toString())
				.when()
				.put("persons/1")
				.then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	void controllerDeletePersonById()
	{
		given()
				.contentType(ContentType.JSON)
				.when()
				.delete("persons/1")
				.then()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	void controllerGetAllAddressesById()
	{
		given()
				.contentType(ContentType.JSON)
				.when()
				.get("persons/1/addresses")
				.then()
				.contentType(ContentType.JSON)
				.body("", hasSize(1));
	}

	@Test
	void controllerAddAddress()
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", 6);
		jsonObject.put("country", "Poland");
		jsonObject.put("state", "Pomorskie");
		jsonObject.put("zip_code", "52-324");
		jsonObject.put("city", "Gdansk");
		jsonObject.put("street_name", "Traugutta 23");

		given()
				.contentType(ContentType.JSON)
				.body(jsonObject.toString())
				.when()
				.post("persons/1/addresses")
				.then()
				.contentType(ContentType.JSON)
				.statusCode(HttpStatus.CREATED.value())
				.body("id", equalTo(6));
	}

	// EXCEPTION TESTS ---------------------------------------------------------------------------------------------------

	// Nie ma wiecej wyjatkow w aplikacji wiec przetestowalem kazdy

	@Test
	void personNotFoundException()
	{
		given()
				.contentType(ContentType.JSON)
				.when()
				.get("persons/12312")
				.then()
				.contentType(ContentType.JSON)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("error_message", equalTo("Person could not be found!"));
	}
}
