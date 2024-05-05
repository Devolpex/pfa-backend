package com.pfa.pfabackend.response.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.pfa.pfabackend.dto.client.ClientDto;
import com.pfa.pfabackend.model.Client;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientPageResponse {
    private List<ClientDto> clients = new ArrayList<>();
    private int currentPage;
    private int totalPages;

}
