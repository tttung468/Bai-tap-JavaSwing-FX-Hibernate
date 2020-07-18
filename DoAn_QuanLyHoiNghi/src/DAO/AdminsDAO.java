/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import pojos.Admins;
import connection.HibernateUtil;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ThanhTung
 */
public class AdminsDAO {

    public static Admins getByID(int id) {
        Admins admin = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            admin = (Admins) session.get(Admins.class, id);     //get
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return admin;
    }

    public static List<Admins> getAll() {
        List<Admins> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            String hql = "from Admins";
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
    
    public static boolean insert(Admins admin) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
        try {
            tx = session.beginTransaction();
            session.save(admin);    //save admin
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

    public static boolean update(Admins admin) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
        //kiem tra admin co ton tai trong DB
        if(AdminsDAO.getByID(admin.getAdminId()) == null){
            return false;   //khong ton tai
        }
                
        try {
            tx = session.beginTransaction();
            session.update(admin);    //update admin
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

    public static boolean delete(Admins admin) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
         //kiem tra admin co ton tai trong DB
        if(AdminsDAO.getByID(admin.getAdminId()) == null){
            return false;   //khong ton tai
        }

        try {
            tx = session.beginTransaction();
            session.delete(admin);    //delete admin
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
