package com.example.clientservice.service;

import com.example.clientservice.model.ClientEntity;
import com.example.clientservice.repository.ClientRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    private final ClientRepository clientRepository;

    public StatsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Map<String, Object> computeClientStats() {
        List<ClientEntity> clients = clientRepository.findAll();

        int totalClients = clients.size();

        int totalSessions = clients.stream()
                .mapToInt(ClientEntity::getSessionsRemaining)
                .sum();

        double averageSessions = totalClients == 0
                ? 0.0
                : (double) totalSessions / totalClients;

        averageSessions = Math.round(averageSessions * 100.0) / 100.0;

        List<ClientEntity> lowSessionClients = clients.stream()
                .filter(c -> c.getSessionsRemaining() <= 2)
                .toList();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalClients", totalClients);
        summary.put("totalSessions", totalSessions);
        summary.put("averageSessions", averageSessions);
        summary.put("lowSessionClients", lowSessionClients);

        return summary;
    }

    @Scheduled(fixedRate = 10000)
    public void logClientStats() {
        Map<String, Object> stats = computeClientStats();
        System.out.println("[Stats] " + stats);
    }
}

