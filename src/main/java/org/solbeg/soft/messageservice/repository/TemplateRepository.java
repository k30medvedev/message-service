package org.solbeg.soft.messageservice.repository;

import org.solbeg.soft.messageservice.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer> {

    Template findByTemplateName(String templateName);

}