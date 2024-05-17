package com.pfa.pfabackend.admin.https.res;

import java.util.ArrayList;
import java.util.List;

import com.pfa.pfabackend.admin.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminPageResponse {
    private List<Admin> admins = new ArrayList<>();
    private int currentPage;
    private int totalPages;
}
