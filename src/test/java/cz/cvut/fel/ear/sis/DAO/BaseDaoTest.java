package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.SISApplication;
import cz.cvut.fel.ear.sis.exception.PersistenceException;
import cz.cvut.fel.ear.sis.model.Program;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

// For explanatory comments, see ProductDaoTest
@DataJpaTest
@ComponentScan(basePackageClasses = SISApplication.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SISApplication.class)})
@ActiveProfiles("test")
public class BaseDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProgramDao sut;
    @Test
    public void persistSavesSpecifiedInstance() {
        final Program prog = generateProgram();
        sut.persist(prog);
        assertNotNull(prog.getId());

        final Program result = em.find(Program.class, prog.getId());
        assertNotNull(result);
        assertEquals(prog.getId(), result.getId());
        assertEquals(prog.getName(), result.getName());
    }

    private static Program generateProgram() {
        final Program prog = new Program();
        prog.setName("Test program " + new Random().nextInt());
        prog.setDescription("Magnificent program");
        return prog;
    }

    @Test
    public void findRetrievesInstanceByIdentifier() {
        final Program prog = generateProgram();
        em.persistAndFlush(prog);
        assertNotNull(prog.getId());

        final Program result = sut.find(prog.getId());
        assertNotNull(result);
        assertEquals(prog.getId(), result.getId());
        assertEquals(prog.getName(), result.getName());
    }

    @Test
    public void findAllRetrievesAllInstancesOfType() {
        final Program progOne = generateProgram();
        em.persistAndFlush(progOne);
        final Program progTwo = generateProgram();
        em.persistAndFlush(progTwo);

        final List<Program> result = sut.findAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(progOne.getId())));
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(progTwo.getId())));
    }

    @Test
    public void updateUpdatesExistingInstance() {
        final Program prog = generateProgram();
        em.persistAndFlush(prog);

        final Program update = new Program();
        update.setId(prog.getId());
        final String newName = "New program name";
        update.setName(newName);
        sut.update(update);

        final Program result = sut.find(prog.getId());
        assertNotNull(result);
        assertEquals(prog.getName(), result.getName());
    }

    @Test
    public void removeRemovesSpecifiedInstance() {
        final Program prog = generateProgram();
        em.persistAndFlush(prog);
        assertNotNull(em.find(Program.class, prog.getId()));
        em.detach(prog);

        sut.remove(prog);
        assertNull(em.find(Program.class, prog.getId()));
    }

    @Test
    public void removeDoesNothingWhenInstanceDoesNotExist() {
        final Program prog = generateProgram();
        prog.setId(123);
        assertNull(em.find(Program.class, prog.getId()));

        sut.remove(prog);
        assertNull(em.find(Program.class, prog.getId()));
    }

    @Test
    public void exceptionOnPersistInWrappedInPersistenceException() {
        final Program prog = generateProgram();
        em.persistAndFlush(prog);
        em.remove(prog);
        assertThrows(PersistenceException.class, () -> sut.update(prog));
    }

    @Test
    public void existsReturnsTrueForExistingIdentifier() {
        final Program prog = generateProgram();
        em.persistAndFlush(prog);
        assertTrue(sut.exists(prog.getId()));
        assertFalse(sut.exists(-1));
    }
}
