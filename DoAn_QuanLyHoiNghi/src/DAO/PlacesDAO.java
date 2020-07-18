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
import pojos.Places;

/**
 *
 * @author ThanhTung
 */
public class PlacesDAO {

    public static Places getByID(int id) {
        Places place = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            place = (Places) session.get(Places.class, id);     //get
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return place;
    }

    public static List<Places> getAll() {
        List<Places> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            String hql = "from Places";
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
    
    public static boolean insert(Places place) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
        try {
            tx = session.beginTransaction();
            session.save(place);    //save place
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

    public static boolean update(Places place) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
        //kiem tra place co ton tai trong DB
        if(PlacesDAO.getByID(place.getPlaceId()) == null){
            return false;   //khong ton tai
        }
                
        try {
            tx = session.beginTransaction();
            session.update(place);    //update place
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

    public static boolean delete(Places place) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean check = true;
        
         //kiem tra place co ton tai trong DB
        if(PlacesDAO.getByID(place.getPlaceId()) == null){
            return false;   //khong ton tai
        }

        try {
            tx = session.beginTransaction();
            session.delete(place);    //delete place
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
