package com.example.demo.repositories;

import com.example.demo.models.Posts;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostsRepositoryImpl implements PostsRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostsRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Posts save(Posts post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            if (post.getPost_id() == 0) {
                session.persist(post);
            } else {
                session.merge(post);
            }
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public Optional<Posts> findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Posts post = session.get(Posts.class, id);
            return post != null ? Optional.of(post) : Optional.empty();
        }
    }

    @Override
    public List<Posts> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Posts> query = session.createQuery("from Posts", Posts.class);
            return query.list();
        }
    }

    @Override
    public void deleteById(int id) {
        Posts postToDelete = findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with ID " + id));
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Posts> findByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Posts> query = session.createQuery("from Posts where user.id = :userId", Posts.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<Posts> findByTitleContaining(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Posts> query = session.createQuery("from Posts where title like :title", Posts.class);
            query.setParameter("title", "%" + title + "%");
            return query.list();
        }
    }
}
