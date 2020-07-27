/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import connection.HibernateUtil;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pojos.RegisteredUsers;
import pojos.RegisteredUsersId;

/**
 *
 * @author ThanhTung
 */
public class RegisteredUsersDAO {

    public static RegisteredUsers getByID(RegisteredUsersId id) {
        RegisteredUsers registeredUser = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();  
            registeredUser = (RegisteredUsers)session.get(RegisteredUsers.class, id);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return registeredUser;
    }

    public static List<RegisteredUsers> getAll() {
        List<RegisteredUsers> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            String hql = "from RegisteredUsers";
            Query query = session.createQuery(hql);

            tx = session.beginTransaction();
            list = query.list();    //get all
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return list;
    }
    
    public static List<RegisteredUsers> getAllByIDUser(int id) {
        List<RegisteredUsers> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            String hql = "from RegisteredUsers where user_id like :id";
            Query query = session.createQuery(hql);
            query.setString("id", "%" + id + "%");

            tx = session.beginTransaction();
            list = query.list();    //get all
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return list;
    }

    public static boolean insert(RegisteredUsers registeredUser) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;

        try {
            tx = session.beginTransaction();
            session.save(registeredUser);    //save registeredUser
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            check = false;
        } finally {
            session.close();
        }

        return check;
    }

    public static boolean update(RegisteredUsers registeredUser) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;

        //kiem tra registeredUser co ton tai trong DB
        if (RegisteredUsersDAO.getByID(registeredUser.getId()) == null) {
            return false;   //khong ton tai
        }

        try {
            tx = session.beginTransaction();
            session.update(registeredUser);    //update registeredUser
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            check = false;
        } finally {
            session.close();
        }

        return check;
    }

    public static boolean delete(RegisteredUsers registeredUser) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;

        //kiem tra registeredUser co ton tai trong DB
        if (RegisteredUsersDAO.getByID(registeredUser.getId()) == null) {
            return false;   //khong ton tai
        }

        try {
            tx = session.beginTransaction();
            session.delete(registeredUser);    //delete registeredUser
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            check = false;
        } finally {
            session.close();
        }

        return check;
    }
}
