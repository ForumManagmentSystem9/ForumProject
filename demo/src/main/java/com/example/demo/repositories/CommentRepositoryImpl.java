package com.example.demo.repositories;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository{

    private final SessionFactory sessionFactory;

    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comment> getAll(int postId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Comment where post.id = :postId", Comment.class)
                    .setParameter("postId", postId)
                    .list();
        }
    }
    @Override
    public Comment getById(int postId, int commentId) throws EntityNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.createQuery("from Comment where id = :commentId and post.id = :postId", Comment.class)
                    .setParameter("commentId", commentId)
                    .setParameter("postId", postId)
                    .uniqueResult();

            if (comment == null) {
                throw new EntityNotFoundException("Comment does not exist or does not belong to the specified Post", commentId);
            }

            return comment;
        }
    }

    @Override
    public void create(Comment comment) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }

    }

    @Override
    public void update(Comment comment) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }

    }

    @Override
    public void delete(int post_id, int comment_id) {
        Comment commentToDelete = getById(post_id, comment_id);
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }

    }

    @Override
    public void getLikes(int id){
//        try(Session session = sessionFactory.openSession()){
//            Transaction transaction = session.beginTransaction();
//            Comment comment = session.get(Comment.class, id);
//            if (comment != null){
//                comment.setLikes(comment.getLikes() + 1);
//                session.update(comment);
//            }
//            transaction.commit();
//        }
    }
}
