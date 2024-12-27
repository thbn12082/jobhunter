package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleSaveCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public Boolean handleFindCompany(Company company) {
        if (this.companyRepository.findByName(company.getName()) != null) {
            return true;
        }
        return false;
    }

    public List<Company> handleFindAllCompany() {
        return this.companyRepository.findAll();
    }

    public Company findCompanyById(long id) {
        return this.companyRepository.findById(id);
    }

}
