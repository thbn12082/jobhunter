package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
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

    // public List<Company> handleFindAllCompany(Pageable pageable) {
    // Page<Company> page = this.companyRepository.findAll(pageable);
    // return page.getContent();
    // }

    public ResultPaginationDTO handleFindAllCompany(Pageable pageable) {
        Page<Company> page = this.companyRepository.findAll(pageable);
        Meta mt = new Meta();
        ResultPaginationDTO res = new ResultPaginationDTO();
        mt.setPage(page.getNumber() + 1);
        mt.setPageSize(page.getSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());

        res.setMeta(mt);
        res.setResult(page.getContent());
        return res;
    }

    public Company findCompanyById(long id) {
        return this.companyRepository.findById(id);
    }

    public void handleDelCompanyById(long id) {
        this.companyRepository.deleteById(id);
    }
}
