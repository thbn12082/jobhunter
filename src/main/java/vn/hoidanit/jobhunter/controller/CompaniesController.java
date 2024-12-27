package vn.hoidanit.jobhunter.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.error.UsernameInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getCompany() {
        return ResponseEntity.ok(this.companyService.handleFindAllCompany());
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> putCompany(@Valid @RequestBody Company company) {
        Company find = this.companyService.findCompanyById(company.getId());
        if (find == null) {
            Company tmp = this.companyService.handleSaveCompany(company);
            return ResponseEntity.ok(tmp);
        } else {
            find.setName(company.getName());
            find.setDescription(company.getDescription());
            find.setAddress(company.getAddress());
            find.setLogo(company.getLogo());
            Company tmp = this.companyService.handleSaveCompany(find);
            return ResponseEntity.ok(tmp);
        }
    }
}
