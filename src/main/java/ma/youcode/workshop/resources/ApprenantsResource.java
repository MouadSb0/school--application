package ma.youcode.workshop.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.youcode.workshop.dao.ApprenantDao;
import ma.youcode.workshop.models.Apprenant;

import java.util.List;

@Path("/apprenants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApprenantsResource {

    private final ApprenantDao dao = new ApprenantDao();

    // GET /api/apprenants
    @GET
public List<Apprenant> getAll() {
    System.out.println(">>> [REST] getAll() appelé");
    ApprenantDao dao = new ApprenantDao();
    List<Apprenant> list = dao.findAll();
    System.out.println(">>> [REST] Retour : " + list.size() + " apprenants");
    return list;
}

    // GET /api/apprenants/{id}
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        Apprenant apprenant = dao.findById(id);
        if (apprenant == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(apprenant).build();
    }

    // POST /api/apprenants
    @POST
    public Response create(Apprenant apprenant) {
        Apprenant created = dao.create(apprenant);
        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();
    }

    @PUT
@Path("/{id}")
public Response update(@PathParam("id") int id, Apprenant apprenant) {
    Apprenant existing = dao.findById(id);
    if (existing == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    apprenant.setId(id);
    dao.update(apprenant);
    return Response.ok(apprenant).build();
}

@DELETE
@Path("/{id}")
public Response delete(@PathParam("id") int id) {
    Apprenant existing = dao.findById(id);
    if (existing == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    dao.delete(id);
    return Response.noContent().build();
}
}