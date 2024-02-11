package cz.cvut.fel.ear.sis.DAO;

import org.hibernate.HibernateException;
import org.hibernate.cache.spi.access.EntityDataAccess;
import org.hibernate.cache.spi.access.NaturalIdDataAccess;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metamodel.spi.RuntimeModelCreationContext;
import org.hibernate.persister.entity.SingleTableEntityPersister;


public class SubUserPersister extends SingleTableEntityPersister {
    public SubUserPersister(PersistentClass persistentClass, EntityDataAccess cacheAccessStrategy, NaturalIdDataAccess naturalIdRegionAccessStrategy, RuntimeModelCreationContext creationContext) throws HibernateException {
        super(persistentClass, cacheAccessStrategy, naturalIdRegionAccessStrategy, creationContext);
    }
}
