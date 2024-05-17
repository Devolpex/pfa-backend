package com.pfa.pfabackend.client.https.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.pfa.pfabackend.client.Client;
import com.pfa.pfabackend.client.ClientDto;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientPageResponse {
    private List<ClientDto> clients = new ArrayList<>();
    private int currentPage;
    private int totalPages;

}
