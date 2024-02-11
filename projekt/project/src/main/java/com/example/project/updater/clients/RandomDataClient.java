package com.example.project.updater.clients;

import com.example.project.services.PersonService;
import com.example.project.updater.model.RandomPersonDTO;
import com.example.project.updater.model.RandomPersonMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RandomDataClient implements IRandomDataClient {

    private final RestTemplate restTemplate;
    private final String url;
    private static final Logger log = LoggerFactory.getLogger(RandomDataClient.class);

    @Autowired
    private PersonService personService;

    public RandomDataClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.url = "http://random-data-api.com/api/v2/users?size=";
    }

    @Override
    public List<RandomPersonDTO> getRandomPeople(int size) {
        //getting one by one
        String requestUrl = this.url + "1";
        log.info("Requesting Random People from URL: {}", requestUrl);

        try {
            //Array of people in JSONOBjects
            ArrayList<JSONObject> peoples = new ArrayList<>();

            for(int i = 0; i < size; i++)
            {
                String retrievedData = "";
                // BECAUSE OF THE REDIRECTION, I RETRIEVE DATA MANUALLY TO STRING
                URL hh= new URL(requestUrl);
                URLConnection connection = hh.openConnection();
                String redirect = connection.getHeaderField("Location");
                if (redirect != null){
                    connection = new URL(redirect).openConnection();
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                System.out.println();
                while ((inputLine = in.readLine()) != null) {
                    retrievedData = inputLine;
                }
                // Adding one by one
                peoples.add(new JSONObject(retrievedData));
            }

            //Returning them using RandomPersonDTO
            return peoples.stream()
                    .map(RandomPersonMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            log.error("Error during API call", e);
            log.error("Response Body: {}", e.getResponseBodyAsString());
            // Decide how you want to handle the error. For now, just return an empty list.
            return new ArrayList<>();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
