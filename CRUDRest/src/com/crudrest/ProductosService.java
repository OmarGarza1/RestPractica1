package com.crudrest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.crudrest.model.Producto;
import com.google.gson.Gson;

@Path("/productos")
public class ProductosService {

		@GET
		@Path("/{idProducto}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.TEXT_PLAIN)
		public String leerProducto(@PathParam("idProducto") String idProducto) throws IOException
		{
			Properties props = new Properties();
			InputStream miStream=null;
			String miArchivoProps = "config.properties";
			miStream = getClass().getClassLoader().getResourceAsStream(miArchivoProps);
			if(miStream!=null)
			{
				props.load(miStream);
			}
			else
			{
				throw new FileNotFoundException("Archivo de Propiedades:"+miArchivoProps+" no se encuentra");
			}
			
			//Paso 1. Declarar variables
			String user = props.getProperty("user");
			String pass = props.getProperty("pass");
			String urlServidor = props.getProperty("urlServidor");
			String miDriver = props.getProperty("driver");
			String sentenciaSQL = props.getProperty("leerRegistro");
			String resultadoJson="";
			
			//Paso 2. Declarar objetos conexión
			Connection conn = null;
			PreparedStatement pstmnt = null;
			ResultSet rs = null;
			Producto miProducto = new Producto(idProducto,null,0.0,0);
			
			try {
				//Paso 3. Instanciar el driver
				Class.forName(miDriver).getDeclaredConstructor().newInstance();
				//Paso 4. Abrir la conexión
				conn = DriverManager.getConnection(urlServidor, user, pass);
				//Paso 5. Configurar el Prepared Statement
				pstmnt = conn.prepareStatement(sentenciaSQL);
				//Paso 6. Pasar los parámetros al Prepared Statement
				pstmnt.setString(1, miProducto.getIdProducto());
				//Paso 7. Ejecutar la consulta
				rs=pstmnt.executeQuery();
				//Paso 8. Mostrar resultados
				rs.next();
				Producto miProducto2 = new Producto(rs.getString("idProducto"),rs.getString("nombreProducto"),rs.getDouble(3),rs.getInt(4));
				
				Gson miGson = new Gson();
				resultadoJson = miGson.toJson(miProducto2);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try {
					rs.close();
					pstmnt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return resultadoJson;
		}
		
		@POST
		@Path("/{idProducto}/{nombreProducto}/{precioProducto}/{existencias}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.TEXT_PLAIN)
		public String crearProducto(@PathParam("idProducto") String idProducto, @PathParam("nombreProducto") String nombreProducto, @PathParam("precioProducto") double precioProducto,@PathParam("existencias") int existencias) throws IOException
		{
			Properties props = new Properties();
			InputStream miStream=null;
			String miArchivoProps = "config.properties";
			miStream = getClass().getClassLoader().getResourceAsStream(miArchivoProps);
			if(miStream!=null)
			{
				props.load(miStream);
			}
			else
			{
				throw new FileNotFoundException("Archivo de Propiedades:"+miArchivoProps+" no se encuentra");
			}
			System.out.println("antes de base de datos");
			//Paso 1. Declarar variables
			String user = props.getProperty("user");
			String pass = props.getProperty("pass");
			String urlServidor = props.getProperty("urlServidor");
			String miDriver = props.getProperty("driver");
			String sentenciaSQL = props.getProperty("crearRegistro");
			String resultadoJson="";
			
			//Paso 2. Declarar objetos conexión
			Connection conn = null;
			PreparedStatement pstmnt = null;
			//ResultSet rs = null;
			int nRegistros=0;
			Producto miProducto = new Producto(idProducto,nombreProducto,precioProducto,existencias);
			
			try {
				//Paso 3. Instanciar el driver
				Class.forName(miDriver).getDeclaredConstructor().newInstance();
				//Paso 4. Abrir la conexión
				conn = DriverManager.getConnection(urlServidor, user, pass);
				//Paso 5. Configurar el Prepared Statement
				pstmnt = conn.prepareStatement(sentenciaSQL);
				//Paso 6. Pasar los parámetros al Prepared Statement
				pstmnt.setString(1, miProducto.getIdProducto());
				pstmnt.setString(2, miProducto.getNombreProducto());
				pstmnt.setDouble(3, miProducto.getPrecioProducto());
				pstmnt.setInt(4, miProducto.getExistencias());
				
				//Paso 7. Ejecutar la consulta
				nRegistros=pstmnt.executeUpdate();
				//Paso 8. Mostrar resultados
				/*rs.next();
				Productos miProducto2 = new Productos(rs.getString("idProducto"),rs.getString("nombreProducto"),rs.getDouble(3),rs.getInt(4));
				*/
				Gson miGson = new Gson();
				resultadoJson = miGson.toJson(miProducto);
				resultadoJson+="Registro Creado con exito";
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try {
					//rs.close();
					pstmnt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("resultado");
			return resultadoJson;
		}
		
		@DELETE
		@Path("/{idProducto}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.TEXT_PLAIN)
		public String borrarProducto(@PathParam("idProducto") String idProducto) throws IOException
		{
			Properties props = new Properties();
			InputStream miStream=null;
			String miArchivoProps = "config.properties";
			miStream = getClass().getClassLoader().getResourceAsStream(miArchivoProps);
			if(miStream!=null)
			{
				props.load(miStream);
			}
			else
			{
				throw new FileNotFoundException("Archivo de Propiedades:"+miArchivoProps+" no se encuentra");
			}
			
			//Paso 1. Declarar variables
			String user = props.getProperty("user");
			String pass = props.getProperty("pass");
			String urlServidor = props.getProperty("urlServidor");
			String miDriver = props.getProperty("driver");
			String sentenciaSQL = props.getProperty("borrarRegistro");
			String resultadoJson="";
			
			//Paso 2. Declarar objetos conexión
			Connection conn = null;
			PreparedStatement pstmnt = null;
			//ResultSet rs = null;
			int nRegistros =0;
			Producto miProducto = new Producto(idProducto,null,0.0,0);
			
			try {
				//Paso 3. Instanciar el driver
				Class.forName(miDriver).getDeclaredConstructor().newInstance();
				//Paso 4. Abrir la conexión
				conn = DriverManager.getConnection(urlServidor, user, pass);
				//Paso 5. Configurar el Prepared Statement
				pstmnt = conn.prepareStatement(sentenciaSQL);
				//Paso 6. Pasar los parámetros al Prepared Statement
				pstmnt.setString(1, miProducto.getIdProducto());
				//Paso 7. Ejecutar la consulta
				nRegistros=pstmnt.executeUpdate();
				//Paso 8. Mostrar resultados
				/*
				rs.next();
				Producto miProducto2 = new Producto(rs.getString("idProducto"),rs.getString("nombreProducto"),rs.getDouble(3),rs.getInt(4));
				*/
				
				Gson miGson = new Gson();
				if (nRegistros > 0) {
					resultadoJson =  "Registros borrados " + nRegistros;
				}
				
				//resultadoJson = miGson.toJson(miProducto2);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try {
				//	rs.close();
					pstmnt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return resultadoJson;
		}
	}
