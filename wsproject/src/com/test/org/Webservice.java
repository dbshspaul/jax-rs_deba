package com.test.org;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.test.bean.Ejb;
import com.test.model.Product;
import com.test.model.Users;

@Path("v1")
public class Webservice {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	@EJB
	private Ejb ejb;

	private StringWriter sw = new StringWriter();
	private JsonGeneratorFactory fact = Json.createGeneratorFactory(null);
	private JsonGenerator gen = fact.createGenerator(sw);

	/**
	 * Default constructor.
	 */
	public Webservice() {
		// TODO Auto-generated constructor stub
	}

	@GET
	@Path("user/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@PathParam("id") int id) {
		Response resp;
		try {
			if (ejb.getDataById(id, Users.class) != null) {
				Users usr=ejb.getDataById(id, Users.class);
				resp = Response.status(Response.Status.FOUND).entity(usr).build();
			} else {
				resp = Response.status(Response.Status.NOT_FOUND).build();
			}

		} catch (Exception e) {
			gen.writeStartObject().write("error", e.getMessage()).writeEnd().close();
			resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sw.toString()).build();
		}
		return resp;
	}

	@PUT
	@Path("user/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response upadateUser(@FormParam("name") String name, @FormParam("address") String address,
			@FormParam("email") String email, @PathParam("id") int id) throws IOException {
		Response resp;
		try {
			Users u = ejb.getDataById(id, Users.class);
			if(u!=null){
				u.setName(name);
				u.setEmail(email);
				u.setAddress(address);

				ejb.updateData(u);
				gen.writeStartObject().write("msg", "Data updated successfully.").writeEnd().close();
				resp = Response.status(Response.Status.CREATED).entity(sw.toString()).build();
			}else{
				resp = Response.status(Response.Status.NOT_FOUND).build();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			gen.writeStartObject().write("error", e.getMessage()).writeEnd().close();
			resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sw.toString()).build();
		}
		sw.close();
		return resp;
	}

	@GET
	@Path("users")
	@Produces("application/json")
	public Response getUsers() throws IOException {
		Response resp;
		List<Users> lst = ejb.getAllData(Users.class);
		try {
			if (lst.size() > 0) {
				GenericEntity<List<Users>> list = new GenericEntity<List<Users>>(lst) {
				};
				resp = Response.status(Response.Status.FOUND).entity(list).build();
			} else {
				gen.writeStartObject().write("Error", "No data found").writeEnd().close();
				resp = Response.status(Response.Status.NOT_FOUND).entity(sw.toString()).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			gen.writeStartObject().write("error", e.getMessage()).writeEnd().close();
			resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sw.toString()).build();
		}
		sw.close();

		return resp;
	}

	@POST
	@Path("users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setUser(@FormParam("name") String name, @FormParam("address") String address,
			@FormParam("email") String email) throws IOException {
		Response resp;
		try {
			Users u = new Users();
			u.setName(name);
			u.setEmail(email);
			u.setAddress(address);

			ejb.setData(u);
			gen.writeStartObject().write("msg", "Data created successfully.").writeEnd().close();
			resp = Response.status(Response.Status.CREATED).entity(sw.toString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			gen.writeStartObject().write("error", e.getMessage()).writeEnd().close();
			resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sw.toString()).build();
		}
		sw.close();
		return resp;
	}

	@POST
	@Path("product")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setProduct(@FormParam("name") String name, @FormParam("description") String description,
			@FormParam("userId") int uid) throws IOException {
		Response resp;
		try {
			Product p=new Product();
			p.setName(name);
			p.setDescription(description);
			p.setUsers(ejb.getDataById(uid, Users.class));
			ejb.setData(p);
			gen.writeStartObject().write("msg", "Data created successfully.").writeEnd().close();
			resp = Response.status(Response.Status.CREATED).entity(sw.toString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			gen.writeStartObject().write("error", e.getMessage()).writeEnd().close();
			resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sw.toString()).build();
		}
		sw.close();
		return resp;
	}
	
	
	@GET
	@Path("products")
	@Produces("application/json")
	public Response getProducts() throws IOException {
		Response resp=Response.ok().build();
		List<Product> lst = ejb.getAllData(Product.class);
		try {
			if (lst.size() > 0) {
				GenericEntity<List<Product>> list = new GenericEntity<List<Product>>(lst) {
				};
				resp = Response.status(Response.Status.FOUND).entity(list).build();
			} else {
				gen.writeStartObject().write("Error", "No data found").writeEnd().close();
				resp = Response.status(Response.Status.NOT_FOUND).entity(sw.toString()).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			gen.writeStartObject().write("error", e.getMessage()).writeEnd().close();
			System.out.println("error: "+ e.getMessage());
			resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sw.toString()).build();
		}
		sw.close();

		return resp;
	}

}