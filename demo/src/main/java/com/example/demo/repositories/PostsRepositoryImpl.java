package com.example.demo.repositories;

import com.example.demo.models.Post;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
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
    public Post save(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            if (post.getId() == 0) {
                session.persist(post);
            } else {
                session.merge(post);
            }
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public Optional<Post> findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            return post != null ? Optional.of(post) : Optional.empty();
        }
    }

    @Override
    public List<Post> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post", Post.class);
            return query.list();
        }
    }

    @Override
    public void deleteById(int id) {
        Post postToDelete = findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with ID " + id));
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Post> findByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where user.id = :userId", Post.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<Post> findByTitleContaining(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title like :title", Post.class);
            query.setParameter("title", "%" + title + "%");
            return query.list();
        }
    }
    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Post> findTop10ByOrderByCreatedDateDesc() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post order by createdDate desc", Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }

    @Override
    public List<Post> findTop10ByOrderByCommentsCountDesc() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post order by comments.size desc", Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }

}
