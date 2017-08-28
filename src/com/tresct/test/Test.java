package com.tresct.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.tresct.dto.Tramite;
import com.tresct.util.HibernateUtil;

public class Test {

	public static void main(String[] args) {

		// Crear un menu iterativo con JOPTION PANE
		// Opciones:
		/*
		 * 1 - Consultar todos los registros por medio de HQL 2 - Consultar los
		 * registros por medio de un filtro al tipoTramite con HQL
		 * 
		 * 3 - Consultar todos los registros por medio de Criteria 4 - Consultar los
		 * registros por medio de un filtro de tipoTramite connCriteria
		 * 
		 * 5 - Operacion Insert 6 - Operacion Update 7 - Operacion delete
		 * 
		 */

		int opcion = 0;
		String menu = "";
		do {

			menu = "";
			menu += "***** Operaciones con Hibernate HQL y Criteria *******\n";
			menu += " 1 - Consulta todos los registros por medio de HQL \n  ";
			menu += " 2 - Consulta los registros por medio de filtro de HQL \n  ";
			menu += " 3 - Consulta todos los registros por medio de Criteria \n  ";
			menu += " 4 - Consulta los registros por medio de filtro de Criteria \n  ";
			menu += " 5 - Insert \n  ";
			menu += " 6 - Update \n  ";
			menu += " 7 - Delete \n  ";
			menu += " 8 - Salir \n  ";

			opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));

			switch (opcion) {

			case 1:
				System.out.println("Consultando por ... 1");
				JOptionPane.showMessageDialog(null, "Consultando registros por HQL: " + consultarHQL());
				
				break;

			case 2:
				System.out.println("Consultando por ... 2");
				System.out.println("Consulta de filtro: " +  consultarHQLpersonalizado());
				JOptionPane.showMessageDialog( null, "Consulta de filtro: " +  consultarHQLpersonalizado());
				break;
				
			case 3:
				System.out.println("Consultando por ... 3");
				System.out.println("Consulta criteria * " + consultarCriteria());
				JOptionPane.showMessageDialog(null , "Consulta criteria * " + consultarCriteria());
				break;
				
			case 4:
				System.out.println("Consultando por ... 4");
				System.out.println("Consulta criteria personalizada  " + consultarCriteriaPersonalizada());
				JOptionPane.showMessageDialog(null , "Consulta criteria personalizada * " + consultarCriteriaPersonalizada());				
				break;
				
			case 5:
				System.out.println("Insertando datos");
				Date date = new Date();
				Tramite t = new Tramite( "Terminado" , new Timestamp( date.getTime() ) );
				inserta(t);
				break;
				
			case 6:
				System.out.println("Actualizando datos");
				Date dateActualiza = new Date();
				Tramite tramiteBusqueda = new Tramite();
				tramiteBusqueda.setIdTramite(15);
				tramiteBusqueda.setTipoTramite("No terminado");
				tramiteBusqueda.setFechaTramite( new Timestamp( dateActualiza.getTime() ));
				actualiza(tramiteBusqueda);
				break;
				
			case 7:
				System.out.println("Eliminando ");
				Tramite tramiteElimina = new Tramite();
				tramiteElimina.setIdTramite(15);
				elimina(tramiteElimina);
				break;
				
			case 8:
				System.out.println("Hasta pronto");
				break;

			}

		} while (opcion != 8);

	}// Fin del metodo main
	
	//Metodo para consultar todos los registros por HQL
	public static String consultarHQL() {
		
		Session sesion = null;
		List<Tramite> listaTramites = null;
		
		try {
			sesion = HibernateUtil.getSessionFactory().openSession();
			sesion.beginTransaction();
			
			//Crear consulta HQL
			Query<Tramite> query = sesion.createQuery("from Tramite"); //Select * from Tramite
			listaTramites = query.getResultList();					
			sesion.getTransaction().commit();
			System.out.println(listaTramites.toString());				
			
		} catch( HibernateException hbe ) {
			
			System.out.println("Excepcion en ConsultarHQL: " + hbe.getMessage());
			sesion.close();		
			
		} finally {
			
			sesion.close();
		}
		
		return listaTramites.toString();		
	} //Fin del metodo consultarHQL
	
	
	public static String consultarHQLpersonalizado() {
		
		List<Tramite>  listaTramites = null;
		Session sesion = null;
		
		try {
			sesion = HibernateUtil.getSessionFactory().openSession();
			sesion.beginTransaction();
			
			//Crear consulta HQL
			Query<Tramite> query = sesion.createQuery("from Tramite where tipoTramite = :tipoTramite"); //Select * from Tramite
			query.setParameter("tipoTramite", "Credito aprobado");
			listaTramites = query.getResultList();						
			sesion.getTransaction().commit();
			System.out.println(listaTramites.toString());			
			
		} catch( HibernateException hbe ) {
			
			System.out.println("Excepcion Hibernate : " + hbe.getMessage());
			sesion.close();		
			
		} finally {
			
			sesion.close();
		}
		
		return listaTramites.toString();		
	}
	
	public static String consultarCriteria() {
				
		List<Tramite> listaTramites = null;
		Session sesion = null;
		
		try {
			
			sesion = HibernateUtil.getSessionFactory().openSession();
			sesion.beginTransaction();
			
			// Fábrica para las piezas individuales de la criteria
			CriteriaBuilder builder = sesion.getCriteriaBuilder();
			CriteriaQuery<Tramite> criteria = builder.createQuery(Tramite.class);
			
			// Definir el tipo de entidad que retorna la consulta
			Root<Tramite> root = criteria.from(Tramite.class);			
			listaTramites = sesion.createQuery(criteria).getResultList();			
			
		}catch( HibernateException hbe) {			
			System.out.println("Excpecion consulta Criteria: " + hbe.getMessage());	
			sesion.close();		
		}finally {			
			sesion.close();
		}		
		return listaTramites.toString();
	} //Fin de consulta criteria
	
	public static String consultarCriteriaPersonalizada() {		
		List<Tramite> listaTramites = null;
		Session sesion = null;		
		try {			
			sesion = HibernateUtil.getSessionFactory().openSession();
			sesion.beginTransaction();
			
			// Fábrica para las piezas individuales de la criteria
			CriteriaBuilder builder = sesion.getCriteriaBuilder();
			CriteriaQuery<Tramite> criteria = builder.createQuery(Tramite.class);
			
			// Definir el tipo de entidad que retorna la consulta
			Root<Tramite> root = criteria.from(Tramite.class);	
			
			//Construyendo la consulta personalizada
			criteria.select(root);
			criteria.where( builder.equal( root.get("tipoTramite") , "Olvidado") );		
			
			listaTramites = sesion.createQuery(criteria).getResultList();			
			
		}catch( HibernateException hbe) {			
			System.out.println("Excpecion consulta Criteria: " + hbe.getMessage());	
			sesion.close();		
		}finally {			
			sesion.close();
		}		
		return listaTramites.toString();
	} //Fin de consulta criteria
	
	//Soluciona el problema del codigo no parametrizado
	/* Hibernate JPA Metamodel Generator
	 * 1 - Descargar la dependencia:  hibernate-jpamodelgen
	 * 2 - Configurar el proyecto con Anotattion processing : Enable project specific settings, Automatically configure JDT APT
	 * */
	public static String consultaCriteriaPersonalizadaMetaModel() {
		List<Tramite> listaTramites = null;
		Session sesion = null;		
		try {			
			sesion = HibernateUtil.getSessionFactory().openSession();
			sesion.beginTransaction();
			
			// Fábrica para las piezas individuales de la criteria
			CriteriaBuilder builder = sesion.getCriteriaBuilder();
			CriteriaQuery<Tramite> criteria = builder.createQuery(Tramite.class);
			
			// Definir el tipo de entidad que retorna la consulta
			Root<Tramite> root = criteria.from(Tramite.class);	
			
			//Construyendo la consulta personalizada
			criteria.select(root);
			criteria.where( builder.equal( root.get("tipoTramite") , "Olvidado") );		
			
			listaTramites = sesion.createQuery(criteria).getResultList();			
			
		}catch( HibernateException hbe) {			
			System.out.println("Excpecion consulta Criteria: " + hbe.getMessage());	
			sesion.close();		
		}finally {			
			sesion.close();
		}		
		return listaTramites.toString();
	}
	
	public static void inserta(Tramite t) {
		Session sesion = null;
		try {
			sesion = HibernateUtil.getSessionFactory().openSession();
			sesion.beginTransaction();			
			sesion.save(t);
			sesion.getTransaction().commit();			
		}catch( HibernateException hbe  ) {
			System.out.println("Hibernate Exception: " + hbe.getMessage());
			sesion.getTransaction().rollback();
			sesion.close();		
		} finally {
			sesion.close();			
		}		
	}
	
	public static void actualiza(Tramite t) {
		Session sesion = null;
		try {
			sesion = HibernateUtil.getSessionFactory().openSession();
			sesion.beginTransaction();
			sesion.update(t);
			sesion.getTransaction().commit();
		}catch(HibernateException hbe) {
			System.out.println("HibernateException: " + hbe.getMessage());
			sesion.getTransaction().rollback();
			sesion.close();
		}finally {
			sesion.close();			
		}		
	}
	
	public static void elimina(Tramite t) {
		Session sesion = null;
		try {
			sesion = HibernateUtil.getSessionFactory().openSession();
			sesion.beginTransaction();
			sesion.delete(t);
			sesion.getTransaction().commit();
		}catch( HibernateException hbe ) {
			System.out.println("HibernateExcepcion: " + hbe.getMessage());
			sesion.getTransaction().rollback();
			sesion.close();
		}finally {
			sesion.close();		
		}
	}
	
	
}//Fin de la clase
