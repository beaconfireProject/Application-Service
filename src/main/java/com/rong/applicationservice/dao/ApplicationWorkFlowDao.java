package com.rong.applicationservice.dao;

import com.rong.applicationservice.domain.ApplicationWorkFlow;
import com.rong.applicationservice.domain.Status;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ApplicationWorkFlowDao extends AbstractHibernateDao<ApplicationWorkFlow> {

    public ApplicationWorkFlowDao() {
        setClazz(ApplicationWorkFlow.class);
    }

    public ApplicationWorkFlow findByEmployeeIdAndApplicationType(String id, String onboarding) {
        String hql = "from ApplicationWorkFlow where employeeId=:id and applicationType=:onboarding";
        Query<ApplicationWorkFlow> query = getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        query.setParameter("onboarding", onboarding);
        return query.getSingleResult();
    }

    public ApplicationWorkFlow getOngoingByAppId(int applicationId) {
        String hql = "from ApplicationWorkFlow where id=:applicationId";
        Query<ApplicationWorkFlow> query = getCurrentSession().createQuery(hql);
        query.setParameter("applicationId", applicationId);
        return query.getSingleResult();
    }

    public void updateStatus(ApplicationWorkFlow app) {
        Session session = getCurrentSession();
        session.update(app);
    }
}
