package qa.model;

import java.util.List;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.sessions.factories.SessionFactory;

import pj.Dept;
import pj.Emp;

public class JavaServiceFacade {
    private SessionFactory sessionFactory;

    public JavaServiceFacade() {
        this.sessionFactory = new SessionFactory("META-INF/sessions.xml", "default");
    }

    public static void main(String [] args) {
        final JavaServiceFacade javaServiceFacade = new JavaServiceFacade();
        //  TODO:  Call methods on javaServiceFacade here...
    }

    public Object mergeEntity(Object entity) {
        UnitOfWork uow = getSessionFactory().acquireUnitOfWork();
        Object workingCopy = uow.readObject(entity);
        if (workingCopy == null)
            throw new RuntimeException("Could not find entity to update");
        uow.deepMergeClone(entity);
        uow.commit();

        return workingCopy;
    }

    public Object persistEntity(Object entity) {
        UnitOfWork uow = getSessionFactory().acquireUnitOfWork();
        Object newInstance = uow.registerNewObject(entity);
        uow.commit();

        return newInstance;
    }

    public Object refreshEntity(Object entity) {
        Session session = getSessionFactory().acquireSession();
        Object refreshedEntity = session.refreshObject(entity);
        session.release();

        return refreshedEntity;
    }

    public void removeEntity(Object entity) {
        UnitOfWork uow = getSessionFactory().acquireUnitOfWork();
        Object workingCopy = uow.readObject(entity);
        if (workingCopy == null)
            throw new RuntimeException("Could not find entity to update");
        uow.deleteObject(workingCopy);
        uow.commit();
    }

    private SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public List<Emp> findAllEmp() {
        Session session = getSessionFactory().acquireSession();
        List<Emp> result =
            (List<Emp>)session.executeQuery("findAllEmp", Emp.class);
        session.release();
        // Uncomment the following lines of code if the results from this query will be mutated.
        // See SessionFactory.detach() for more information.
         result = (java.util.List<pj.Emp>)getSessionFactory().detach(result);

        return result;
    }

    public List<Dept> findAllDept() {
        Session session = getSessionFactory().acquireSession();
        List<Dept> result =
            (List<Dept>)session.executeQuery("findAllDept", Dept.class);
        session.release();
        // Uncomment the following lines of code if the results from this query will be mutated.
        // See SessionFactory.detach() for more information.
         result = (java.util.List<pj.Dept>)getSessionFactory().detach(result);

        return result;
    }
}
