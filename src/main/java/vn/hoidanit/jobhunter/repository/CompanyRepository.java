package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company save(Company company);

    Company findByName(String companyName);

    Company findById(long id);

    void deleteById(long id);

}
