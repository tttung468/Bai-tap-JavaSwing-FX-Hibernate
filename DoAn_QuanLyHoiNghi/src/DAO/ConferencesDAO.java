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
import pojos.Conferences;

/**
 *
 * @author ThanhTung
 */
public class ConferencesDAO {

    public static Conferences getByID(int id) {
        Conferences conference = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            conference = (Conferences) session.get(Conferences.class, id);     //get
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return conference;
    }

    public static List<Conferences> getAll() {
        List<Conferences> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            String hql = "from Conferences";
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
    
    public static boolean insert(Conferences conference) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
        try {
            tx = session.beginTransaction();
            session.save(conference);    //save conference
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

    public static boolean update(Conferences conference) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
        //kiem tra conference co ton tai trong DB
        if(ConferencesDAO.getByID(conference.getConferenceId()) == null){
            return false;   //khong ton tai
        }
                
        try {
            tx = session.beginTransaction();
            session.update(conference);    //update conference
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

    public static boolean delete(Conferences conference) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
         //kiem tra conference co ton tai trong DB
        if(ConferencesDAO.getByID(conference.getConferenceId()) == null){
            return false;   //khong ton tai
        }

        try {
            tx = session.beginTransaction();
            session.delete(conference);    //delete conference
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
