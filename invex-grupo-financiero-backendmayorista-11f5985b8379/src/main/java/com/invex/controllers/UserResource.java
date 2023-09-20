package com.invex.controllers;

import com.invex.Response.PaginatedResponse;
import com.invex.entities.user.User;
import com.invex.services.UserService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    /**
     * Obtiene la lista de todos los Usuarios.
     */
    @GET
    @Path("/lista")
    public Uni<List<User>> getUsers() {
        return userService.getAllUsers();
    }

    /**
     * Obtiene la lista de todos los flujos y paginación.
     */
    @GET
    @Path("/paginado")
    public Uni<PaginatedResponse<User>> getPaginatedUsers(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("20") int size) {
        // Validación de 'size'
        if (size != 10 && size != 15 && size != 20) {
            throw new WebApplicationException("El parámetro 'size' no es válido. Debe ser 10, 15 o 20.", 400); // 400 es el código de estado para "Bad Request"
        }

        return userService.getPaginatedUsers(page, size)
                .onItem().transformToUni(user -> {
                    return User.count().onItem().transform(totalRecords -> {
                        int totalPages = (int) Math.ceil((double) totalRecords / size);
                        return new PaginatedResponse<>(user, totalRecords, totalPages);
                    });
                });
    }

    /**
     * Crea un nuevo usuario.
     * Si el usuario ya existe, retorna un error de solicitud inválida.
     */
    @POST
    @Path("/create")
    public Uni<Response> createUser(User user) {

        // Validación del dominio en el campo getUserIdentifier.
        if (!isValidInvexIdentifier(user.getUserIdentifier())) {
            return Uni.createFrom().item(() -> Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El identificador del usuario debe terminar con @invex.com.\"}").build());
        }

        return userService.findByName(user.getUserIdentifier())
                .onItem().transformToUni(foundUser -> {
                    if (foundUser != null) {
                        return Uni.createFrom().item(() -> Response.status(Response.Status.CONFLICT)
                                .entity("{\"error\": \"No es posible completar la operación, el usuario ya existe.\"}").build());
                    }
                    return userService.createUser(user)
                            .onItem().transform(createdUser -> Response.status(Response.Status.CREATED)
                                    .entity(createdUser).build());
                });
    }

    private boolean isValidInvexIdentifier(String identifier) {
        return identifier != null && identifier.toLowerCase().endsWith("@invex.com");
    }

    /**
     * Ordena la lista de todos los usuarios de forma ascendente o descendente.
     */
    @GET
    @Path("/ordenar")
    public Uni<List<User>> sortUsers(@QueryParam("columna") String columna, @QueryParam("ascendente") boolean ascendente) {
        return userService.sortUsers(columna, ascendente);
    }

    /**
     * Localizar usuario introduciendo cualquier letra.
     */
    @GET
    @Path("/localizar_especifico")
    public Uni<List<User>> listUsers(@QueryParam("search") String searchTerm) {
        if(searchTerm != null && !searchTerm.trim().isEmpty()) {
            return userService.searchUsersByParameter(searchTerm);
        } else {
            return userService.getAllUsers();
        }
    }

    /**
     * Desactiva un Usuario por su ID.
     * Retorna una respuesta de éxito si se desactiva correctamente, o una respuesta de error si falla.
     */
    @PUT
    @Path("{id}/desactivar")
    public Uni<Response> desactivateUser(@PathParam("id") Long id) {
        return userService.desactivateUser(id)
                .onItem().transform(ignored -> Response.ok("Usuario desactivado correctamente").build())
                .onFailure().recoverWithItem(Response.status(Response.Status.BAD_REQUEST).entity("No se pudo desactivar el Usuario").build());
    }

    /**
     * Exporta la lista de Usuarios en formato XLS.
     * Genera un archivo XLS para descargar o una respuesta de error en caso de problemas.
     */
    @GET
    @Path("/exportar/xls")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> exportUsersToXLS() {
        return userService.getAllUsers()
                .onItem().transform(users -> {
                    byte[] xlsBytes = userService.exportUsersToXLS(users);
                    if (xlsBytes != null) {
                        return Response.ok(xlsBytes)
                                .header("Content-Disposition", "attachment; filename=usuarios.xlsx")
                                .build();
                    } else {
                        return Response.serverError().entity("Error generando archivo XLS.").build();
                    }
                });
    }

    /**
     * Exporta la lista de Usuarios en formato TXT.
     * Genera un archivo TXT para descargar o una respuesta de error en caso de problemas.
     */
    @GET
    @Path("/exportar/txt")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> exportUsersToTXT() {
        return userService.getAllUsers()
                .onItem().transform(users -> {
                    byte[] txtBytes = userService.exportUsersToTXT(users);
                    if (txtBytes != null) {
                        return Response.ok(txtBytes)
                                .header("Content-Disposition", "attachment; filename=usuarios.txt")
                                .build();
                    } else {
                        return Response.serverError().entity("Error generando archivo TXT.").build();
                    }
                });
    }

    /**
     * Exporta la lista de Usuarios en formato CSV.
     * Genera un archivo CSV para descargar o una respuesta de error en caso de problemas.
     */
    @GET
    @Path("/exportar/csv")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> exportUsersToCSV() {
        return userService.getAllUsers()
                .onItem().transform(users -> {
                    byte[] csvBytes = userService.exportUsersToCSV(users);
                    if (csvBytes != null) {
                        return Response.ok(csvBytes)
                                .header("Content-Disposition", "attachment; filename=usuarios.csv")
                                .build();
                    } else {
                        return Response.serverError().entity("Error generando archivo CSV.").build();
                    }
                });
    }
}
