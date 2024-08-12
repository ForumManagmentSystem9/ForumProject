package com.example.demo.repositories;

import com.example.demo.exceptions.LikeException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostsRepositoryImpl implements PostsRepository {

    private final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(PostsRepositoryImpl.class);

    @Autowired
    public PostsRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Post create(Post post) {
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
    @Transactional
    public void addLike(Post post, User user) {
        Session session = sessionFactory.getCurrentSession();

        // Ensure the post is managed
        post = session.get(Post.class, post.getId());
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }

        // Check if the like already exists
        String hql = "SELECT l FROM Like l WHERE l.post = :post AND l.user = :user";
        Query<Like> query = session.createQuery(hql, Like.class);
        query.setParameter("post", post);
        query.setParameter("user", user);
        Like existingLike = query.uniqueResult();

        try {
            if (existingLike != null) {
                removeLike(existingLike);
                post.setLikeCount(post.getLikeCount() - 1);
            } else {
                Like newLike = new Like();
                newLike.setPost(post);
                newLike.setUser(user);
                session.persist(newLike);
                post.setLikeCount(post.getLikeCount() + 1);
                session.merge(post);
            }
        } catch (Exception e) {
            logger.error("Error while processing the like: " + e.getMessage());
            throw new LikeException("Error while processing the like: " + e.getMessage());
        }
    }


    @Override
    public List<Post> findTop10ByOrderByCommentsCountDesc() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post p order by size(p.comments) desc", Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }

    private void removeLike(Like like) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            if (like != null) {
                session.remove(like);
                transaction.commit();
            } else {
                throw new EntityNotFoundException("Like with ID " + like.getId() + " does not exist.");
            }
        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Constraint violation during delete operation", e);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error removing like", e);
        }
    }
}
