/**
 * @author Randy PatanasakPinyo (raterko@iastate.edu)
 */
package cs363c;
import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSession 
{
	private SessionFactory sessionFactory;
	// A SessionFactory is set up once for an application!
	
	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
					.configure() // configures settings from hibernate.cfg.xml
					.build();
	
	public HibernateSession()
	{
		
	}
	
	protected void setUp() throws Exception 
	{
		try 
		{
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		}
		catch (Exception e) 
		{
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			StandardServiceRegistryBuilder.destroy(registry);
			System.out.println(e.toString());
		}
	}
	
	public SessionFactory getSessionFactory()
	{
		return this.sessionFactory;
	}
}
