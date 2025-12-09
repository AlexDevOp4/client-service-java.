package com.example.clientservice.controller;

import com.example.clientservice.model.ClientEntity;
import com.example.clientservice.repository.ClientRepository;
import com.example.clientservice.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



import java.util.List;
import java.util.Map;

@RestController
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    private final StatsService statsService;

    public ClientController(ClientRepository clientRepository, StatsService statsService) {
        this.clientRepository = clientRepository;
        this.statsService = statsService;
    }



    @GetMapping("/clients")
    public List<ClientEntity> getAllClients() {

        return clientRepository.findAll();
    }

    @PostMapping("/clients")
    public ClientEntity createClient(@RequestBody ClientEntity client) {
        System.out.println("Creating Client: " + client.getName());
        return clientRepository.save(client);
    }

    @GetMapping("/clients/{id}")
    public ClientEntity getClient(@PathVariable Long id) {
        System.out.println("Getting Client: " + id);
        return clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client not found."));
    }

    @PutMapping("/clients/{id}")
    public ClientEntity updateClient(@PathVariable Long id, @RequestBody ClientEntity client) {
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found." ));


        if (client.getName() != null) {
            clientEntity.setName(client.getName());
        }

        if (client.getSessionsRemaining() != 0 ){
            clientEntity.setSessionsRemaining(client.getSessionsRemaining());
        }

        return clientRepository.save(clientEntity);
    }

    @DeleteMapping("/clients/{id}")
    public List<ClientEntity> deleteClient(@PathVariable Long id) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found."));

        System.out.println("Deleted Client: " + client.getName());

        clientRepository.delete(client);

        return clientRepository.findAll();
    }

    @PostMapping("/clients/{id}/attend-session")
    public ClientEntity attendSession(@PathVariable Long id) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found."));

        if (client.getSessionsRemaining()  <= 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No more sessions..");
        } else {
            client.setSessionsRemaining(client.getSessionsRemaining() - 1);
            return clientRepository.save(client);

        }


    }

    //GET /clients/stats
    @GetMapping("/clients/stats")
    public Map<String, Object> getClientStats() {
        return statsService.computeClientStats();
    }

}
