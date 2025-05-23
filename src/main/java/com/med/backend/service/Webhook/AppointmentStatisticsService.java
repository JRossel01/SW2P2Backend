package com.med.backend.service.Webhook;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.backend.dto.appointmentStatistics.AppointmentStatisticsDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AppointmentStatisticsService {

    private final RestTemplate restTemplate;

    public AppointmentStatisticsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendDailyAppointmentStatistics(AppointmentStatisticsDTO statistics) {
        String djangoUrl = "http://localhost:8000/dashboard/appointment-statistics/";
        //String djangoUrl = "http://20.206.205.29:8000/dashboard/appointment-statistics/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsond = objectMapper.writeValueAsString(statistics);
            System.out.println("JSON result: " + jsond);
            HttpEntity<String> request = new HttpEntity<>(jsond, headers);
            System.out.println("Request a enviar: " + request);
            restTemplate.postForEntity(djangoUrl, request, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}
