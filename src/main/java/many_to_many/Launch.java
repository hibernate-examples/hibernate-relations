package many_to_many;

import many_to_many.entity.Author;
import many_to_many.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Arrays;
import java.util.List;

public class Launch {

    public static void main(String[] args) {
        StandardServiceRegistry bootstrapRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .configure("ManyToMany.cfg.xml")
                .build();

        MetadataSources sources = new MetadataSources(bootstrapRegistry);

        SessionFactory sessionFactory = sources
                .buildMetadata()
                .buildSessionFactory();

        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        Author author1 = new Author();
        Author author2 = new Author();
        author1.setName("author1");
        author2.setName("author2");
        Book newBook = new Book();
        newBook.setName("book1");
        newBook.setAuthors(Arrays.asList(author1, author2));

        session.save(author1);
        session.save(author2);
        session.save(newBook);
        session.getTransaction().commit();

        session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        List<Book> books = session.createQuery("from many_to_many.entity.Book").list();
        for (Book book : books) {
            System.out.println(book.getName() + ":");
            for (Author author : book.getAuthors()) {
                System.out.println("  " + author.getName());
            }
            System.out.println();
        }

        session.getTransaction().commit();

        sessionFactory.close();
    }

}
