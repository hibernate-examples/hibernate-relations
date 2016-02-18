package one_to_many;

import one_to_many.entity.Product;
import one_to_many.entity.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class Launch {
    public static void main(String[] args) {
        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .configure() // by default use "hibernate.cfg.xml"
                .build();

        MetadataSources metadataSources = new MetadataSources(standardServiceRegistry)
                .addAnnotatedClass(Product.class)
                .addAnnotatedClassName("one_to_many.entity.Category");

        SessionFactory sessionFactory = metadataSources
                .buildMetadata()
                .buildSessionFactory();


        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Category fruitCategory = new Category();
        Product productApple = new Product();
        Product productBanana = new Product();
        fruitCategory.setName("Fruits 1");
        productApple.setName("Apple 1");
        productBanana.setName("Banana 1");
        productApple.setCategory(fruitCategory);
        productBanana.setCategory(fruitCategory);

        session.save(productApple);
        session.save(fruitCategory);
        session.save(productBanana);

        session.getTransaction().commit();

        session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        List<Category> categories = session.createCriteria(Category.class).list();

        for (Category category : categories) {
            System.out.println(category.getName() + " :");
            for (Product product : category.getProducts()) {
                System.out.println("   " + product.getName());
            }
            System.out.println();
        }

        session.getTransaction().commit();

        sessionFactory.close();
    }


}
