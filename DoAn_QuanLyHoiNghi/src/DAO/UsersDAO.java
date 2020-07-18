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
import pojos.Users;

/**
 *
 * @author ThanhTung
 */
public class UsersDAO {
    
    public static Users getByID(int id) {
        Users user = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            user = (Users) session.get(Users.class, id);     //get
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return user;
    }

    public static List<Users> getAll() {
        List<Users> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            String hql = "from Users";
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
    
    public static boolean insert(Users user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
        try {
            tx = session.beginTransaction();
            session.save(user);    //save user
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

    public static boolean update(Users user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
        //kiem tra user co ton tai trong DB
        if(UsersDAO.getByID(user.getUserId()) == null){
            return false;   //khong ton tai
        }
                
        try {
            tx = session.beginTransaction();
            session.update(user);    //update user
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

    public static boolean delete(Users user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
         //kiem tra user co ton tai trong DB
        if(UsersDAO.getByID(user.getUserId()) == null){
            return false;   //khong ton tai
        }

        try {
            tx = session.beginTransaction();
            session.delete(user);    //delete user
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
