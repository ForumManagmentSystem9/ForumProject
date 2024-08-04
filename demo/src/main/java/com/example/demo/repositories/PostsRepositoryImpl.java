package com.example.demo.repositories;

import com.example.demo.exceptions.LikeException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

        // Check if the like already exists
        String hql = "SELECT l FROM Like l WHERE l.post = :post AND l.user = :user";
        Query<Like> query = session.createQuery(hql, Like.class);
        query.setParameter("post", post);
        query.setParameter("user", user);
        Like existingLike = query.uniqueResult();

        try {
            if (existingLike != null) {
                // If the like exists, remove it
                session.delete(existingLike);
                post.setLikes(post.getLikeCount() - 1); // Decrement the likes count
            } else {
                // If the like does not exist, add it
                Like newLike = new Like();
                newLike.setPost(post);
                newLike.setUser(user);
                session.persist(newLike); // Persist the new like
                post.setLikes(post.getLikeCount() + 1); // Increment the likes count
            }

            // Reattach the post and update it
            session.merge(post);

        } catch (Exception e) {
            logger.debug("Error while processing the like: " + e.getMessage());
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
}
