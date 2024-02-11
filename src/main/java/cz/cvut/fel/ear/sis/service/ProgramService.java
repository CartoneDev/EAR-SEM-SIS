package cz.cvut.fel.ear.sis.service;

import cz.cvut.fel.ear.sis.DAO.ProgramDao;
import cz.cvut.fel.ear.sis.model.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgramService {
    final
    ProgramDao programDao;


    public ProgramService(ProgramDao programDao) {
        this.programDao = programDao;
    }

    @Transactional
    public int generateIndividualProgram() {
        Program program = new Program();
        program.setName("Individual program");
        programDao.persist(program);
        return program.getId();
    }

    public Program findProgramById(Integer programId) {
        return programDao.find(programId);
    }
}
