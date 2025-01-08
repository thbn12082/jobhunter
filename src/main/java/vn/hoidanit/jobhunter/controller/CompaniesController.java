package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.UsernameInvalidException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.turkraft.springfilter.boot.Filter;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api/v1")
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

    // @GetMapping("/companies")
    // public ResponseEntity<List<Company>> getCompany(
    // @RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional) {
    // String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
    // String sPageSize = pageSizeOptional.isPresent() ? currentOptional.get() : "";
    // Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1,
    // Integer.parseInt(sPageSize));
    // return ResponseEntity.ok(this.companyService.handleFindAllCompany(pageable));
    // }

    // @GetMapping("/companies")
    // public ResponseEntity<ResultPaginationDTO> getCompany(
    // @RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional) {
    // String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
    // String sPageSize = pageSizeOptional.isPresent() ? currentOptional.get() : "";
    // Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1,
    // Integer.parseInt(sPageSize));
    // return ResponseEntity.ok(this.companyService.handleFindAllCompany(pageable));
    // }

    @GetMapping("/companies")
    @ApiMessage("Fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getCompany(
            @Filter Specification spe,
            Pageable pageable
    // @RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional) {
    ) {
        return ResponseEntity.ok(this.companyService.handleFindAllCompany(spe, pageable));
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

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> delCompany(@PathVariable("id") Long id) {
        this.companyService.handleDelCompanyById(id);
        return ResponseEntity.ok(null);
    }
}