package com.invex.controllers;

import com.invex.Response.PaginatedResponse;
import com.invex.entities.profile.Profile;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

import com.invex.entities.flow.Flow;
import com.invex.services.FlowService;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@Path("onboarding/flujos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlowController {

  @Inject
  FlowService flujoService;

    private static final Logger LOGGER = Logger.getLogger(FlowController.class.getName()); // Crea una instancia de Logger

    private static Response apply(Integer updated) {
        if (updated > 0) {
            return Response.ok("Asociación realizada correctamente.").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al actualizar el flujo.").build();
        }
    }

    /**
   * Obtiene la lista de todos los flujos.
   */
  @GET
  @Path("/listar")
  public Uni<List<Flow>> obtenerFlujos() {
    return flujoService.obtenerTodosLosFlujos();
  }

    /**
     * Obtiene la lista de todos los flujos y paginación.
     */
    @GET
    @Path("/paginado")
    public Uni<PaginatedResponse<Flow>> obtenerFlujosPaginados(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("20") int size) {
        // Validación de 'size'
        if (size != 10 && size != 15 && size != 20) {
            throw new WebApplicationException("El parámetro 'size' no es válido. Debe ser 10, 15 o 20.", 400); // 400 es el código de estado para "Bad Request"
        }

        return flujoService.obtenerFlujosPaginados(page, size)
                .onItem().transformToUni(flujos -> {
                    return Flow.count().onItem().transform(totalRecords -> {
                        int totalPages = (int) Math.ceil((double) totalRecords / size);
                        return new PaginatedResponse<>(flujos, totalRecords, totalPages);
                    });
                });
    }

  /**
   * Crea un nuevo flujo.
   * Si el flujo ya existe, retorna un error de solicitud inválida.
   */
  @POST
  @Path("/crear_flujo")
  public Uni<Response> create(Flow flujo) {
      return flujoService.findByName(flujo.getNombreFlujo())
              .onItem().transformToUni(foundFlujo -> {
                  if (foundFlujo != null) {
                      return Uni.createFrom().item(() -> Response.status(Response.Status.BAD_REQUEST)
                              .entity("El nombre del flujo ya existe.").build());
                  }
                  return flujoService.createFlujo(flujo)
                          .onItem().transform(persistedFlujo -> Response.status(Response.Status.CREATED)
                                  .entity(persistedFlujo).build());
              });
  }

    @PUT
    @Path("{id}/asociarPerfiles")
    public Uni<Response> asociarPerfiles(@PathParam("id") Long id, List<Long> perfilIds) {
        LOGGER.info(String.format("Ingresando al método asociarPerfiles con ID: %d", id));
        LOGGER.info(String.format("IDs de perfiles a asociar: %s", perfilIds));

        return flujoService.asociarPerfilesAFlujo(id, perfilIds)
                .onItem().transform(updatedFlujo -> {
                    if (updatedFlujo != null) {
                        LOGGER.info(String.format("Perfiles asociados exitosamente al flujo con ID: %d", id));
                        return Response.ok("Perfiles asociados correctamente").build();
                    } else {
                        LOGGER.warning(String.format("Error asociando perfiles al flujo con ID: %d", id));
                        return Response.status(Response.Status.BAD_REQUEST).entity("Error asociando perfiles al flujo").build();
                    }
                });
    }


    @GET
  @Path("/ordenar")
  public Uni<List<Flow>> ordenarFlujos(@QueryParam("columna") String columna,
      @QueryParam("ascendente") boolean ascendente) {
    return flujoService.ordenarFlujos(columna, ascendente);
  }

  @GET
  @Path("/localizar_especifico")
  public Uni<List<Flow>> listarFlujos(@QueryParam("search") String searchTerm) {
    if (searchTerm != null && !searchTerm.trim().isEmpty()) {
      return flujoService.buscarFlujosPorParametro(searchTerm);
    } else {
      return flujoService.obtenerTodosLosFlujos();
    }
  }

  /**
   * Desactiva un flujo por su ID.
   * Retorna una respuesta de éxito si se desactiva correctamente, o una respuesta
   * de error si falla.
   */
  @PUT
  @Path("{id}/desactivar")
  public Uni<Response> desactivarFlujo(@PathParam("id") Long id) {
    return flujoService.desactivarFlujo(id)
        .onItem().transform(ignored -> Response.ok("Flujo desactivado correctamente").build())
        .onFailure()
        .recoverWithItem(Response.status(Response.Status.BAD_REQUEST).entity("No se pudo desactivar el flujo").build());
  }

  /**
   * Exporta la lista de flujos en formato XLS.
   * Genera un archivo XLS para descargar o una respuesta de error en caso de
   * problemas.
   */
  @GET
  @Path("/exportar/xls")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Uni<Response> exportarFlujosToXLS() {
    return flujoService.obtenerTodosLosFlujos()
        .onItem().transformToUni(new Function<List<Flow>, Uni<? extends Response>>() {
          @Override
          public Uni<? extends Response> apply(List<Flow> flujos) {
            byte[] xlsBytes = flujoService.exportarFlujosToXLS(flujos);
            if (xlsBytes != null) {
              return Uni.createFrom().item(() -> Response.ok(xlsBytes)
                  .header("Content-Disposition", "attachment; filename=flujos.xlsx")
                  .build());
            } else {
              return Uni.createFrom().item(() -> Response.serverError().entity("Error generando archivo XLS.").build());
            }
          }
        });
  }

  /**
   * Exporta la lista de flujos en formato TXT.
   * Genera un archivo TXT para descargar o una respuesta de error en caso de
   * problemas.
   */
  @GET
  @Path("/exportar/txt")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Uni<Response> exportarFlujosToTXT() {
    return flujoService.obtenerTodosLosFlujos()
        .onItem().transform(flujos -> {
          byte[] txtBytes = flujoService.exportarFlujosToTXT(flujos);
          if (txtBytes != null) {
            return Response.ok(txtBytes)
                .header("Content-Disposition", "attachment; filename=flujos.txt")
                .build();
          } else {
            return Response.serverError().entity("Error generando archivo TXT.").build();
          }
        });
  }

  /**
   * Exporta la lista de flujos en formato CSV.
   * Genera un archivo CSV para descargar o una respuesta de error en caso de
   * problemas.
   */
  @GET
  @Path("/exportar/csv")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Uni<Response> exportarFlujosToCSV() {
    return flujoService.obtenerTodosLosFlujos()
        .onItem().transform(flujos -> {
          byte[] csvBytes = flujoService.exportarFlujosToCSV(flujos);
          if (csvBytes != null) {
            return Response.ok(csvBytes)
                .header("Content-Disposition", "attachment; filename=flujos.csv")
                .build();
          } else {
            return Response.serverError().entity("Error generando archivo CSV.").build();
          }
        });
  }
}