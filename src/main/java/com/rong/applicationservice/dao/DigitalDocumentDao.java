package com.rong.applicationservice.dao;

import com.rong.applicationservice.domain.DigitalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DigitalDocumentDao extends AbstractHibernateDao<DigitalDocument> {
    public DigitalDocumentDao() {
        setClazz(DigitalDocument.class);
    }
}
