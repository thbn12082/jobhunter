package vn.hoidanit.jobhunter.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.error.UsernameInvalidException;

@Controller
public class CompaniesController {
    private final CompanyService companyService;

    public CompaniesController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company binh) throws UsernameInvalidException {
        // if (binh.getName().equals("")) {
        // throw new UsernameInvalidException("Username không được để trống");
        // }
        Company company = this.companyService.handleSaveCompany(binh);
        return ResponseEntity.ok(company);
    }

}
