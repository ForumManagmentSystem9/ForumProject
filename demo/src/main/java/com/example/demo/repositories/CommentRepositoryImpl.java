package com.example.demo.repositories;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Comment;
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
    public List<Comment> getAll() {
        try (Session session = sessionFactory.openSession()){
            return session.createQuery("from Comment", Comment.class).list();
        }
    }

    @Override
    public Comment getById(int id) throws EntityNotFoundException {
        try (Session session = sessionFactory.openSession()){
            Comment comment = session.get(Comment.class, id);
            if (comment == null){
                throw new EntityNotFoundException("Comment", id);
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
    public void delete(int id) {
        Comment commentToDelete = getById(id);
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }

    }

    @Override
    public void getLikes(int id){
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            Comment comment = session.get(Comment.class, id);
            if (comment != null){
                comment.setLikes(comment.getLikes() + 1);
                session.update(comment);
            }
            transaction.commit();
        }
    }
}
