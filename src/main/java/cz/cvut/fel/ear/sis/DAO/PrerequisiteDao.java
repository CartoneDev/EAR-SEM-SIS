package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Guarantor;
import cz.cvut.fel.ear.sis.model.Prerequisite;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PrerequisiteDao extends BaseDao<Prerequisite>{
    public PrerequisiteDao() {super(Prerequisite.class); }


    public List<Prerequisite> findPrerequisitesByCourseId(long courseId) { // Td change it
        return findAll().stream().filter(p -> p.getRequestedCourse().getId() == courseId).toList();
    }
}
