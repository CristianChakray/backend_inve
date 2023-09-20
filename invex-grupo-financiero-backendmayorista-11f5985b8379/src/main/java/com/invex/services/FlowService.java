package com.invex.services;

import com.invex.entities.profile.Profile;
import com.invex.repositories.ProfileRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.invex.entities.flow.Flow;
import com.invex.entities.flow.Flow.EstadoFlujo;
import com.invex.repositories.FlowRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class FlowService {

    @Inject
    FlowRepository flujoRepository;

    @Inject
    ProfileRepository profileRepository;

    private static final Logger LOGGER = Logger.getLogger(FlowService.class.getName()); // Crea una instancia de Logger

    /**
     * Crea un nuevo flujo.
     * @param flujo El flujo a crear.
    * @return Un objeto Uni que eventualmente contiene el flujo creado.
    */
    public Uni<Flow> createFlujo(Flow flujo) {
        flujo.setFechaCreacion(LocalDateTime.now());
        flujo.setFechaUltimaModificacion(LocalDateTime.now());
        return flujoRepository.persistAndFlush(flujo)
                .onItem().ignore().andSwitchTo(Uni.createFrom().item(() -> flujo));
    }

    public Uni<Flow> findByName(String name) {
        return flujoRepository.find("nombreFlujo", name).firstResult();
    }

    public Uni<List<Flow>> obtenerTodosLosFlujos() {
        return flujoRepository.listAll();
    }

    public Uni<List<Flow>> obtenerFlujosPaginados(int page, int size) {
        return Flow.findAll().page(Page.of(page, size)).list();
    }

    public Uni<Flow> obtenerFlujoPorId(Long id) {
        return flujoRepository.findById(id);
    }

    //Desactivar Flujos
    @Transactional
    public Uni<Void> desactivarFlujo(Long id) {
        return flujoRepository.findById(id)
                .onItem().ifNotNull().transformToUni(flujo -> {
                    flujo.setEstado(EstadoFlujo.INACTIVO);
                    return flujoRepository.persistAndFlush(flujo);
                })
                .onItem().ignore().andSwitchTo(Uni.createFrom().voidItem());
    }

    //Ordenar Flujos por una columna
    public Uni<List<Flow>> ordenarFlujos(String columna, boolean ascendente) {
        String orden = ascendente ? "ASC" : "DESC";
        return flujoRepository.list("ORDER BY " + columna + " " + orden);
    }

    //Buscar Flujo por un parametro ingresado en cualquier de las columnas
    public Uni<List<Flow>> buscarFlujosPorParametro(String parametro) {
        return flujoRepository.list("UPPER(nombreFlujo) LIKE UPPER(?1) OR UPPER(estado) LIKE UPPER(?1)", "%" + parametro + "%");
    }

    public Uni<List<Flow>> buscarFlujosPorNombre(String nombreFlujo) {
        return flujoRepository.<Flow>find("nombreFlujo LIKE ?1", "%" + nombreFlujo + "%")
                .list();
    }

    /**
     * Exporta la lista de flujos en formato XLS.
     * @param flujos Lista de flujos a exportar.
     * @return Un arreglo de bytes que contiene los datos del archivo XLS.
     */
    public byte[] exportarFlujosToXLS(List<Flow> flujos) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Flujos");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Flujo");
            headerRow.createCell(1).setCellValue("Fecha de última modificación");
            headerRow.createCell(2).setCellValue("Estado");

            int rowNum = 1;
            for (Flow flujo : flujos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(flujo.getNombreFlujo());
                row.createCell(1).setCellValue(flujo.getFechaUltimaModificacion().toString());  // Convertir LocalDateTime a String
                row.createCell(2).setCellValue(flujo.getEstado().toString());

            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Exporta la lista de flujos en formato TXT.
     */
    public byte[] exportarFlujosToTXT(List<Flow> flujos) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String txtHeader = "Flujo, Fecha de Última Modificación, Estado" + System.lineSeparator();
            outputStream.write(txtHeader.getBytes());

            for (Flow flujo : flujos) {
                String txtLine = String.format("%s, %s, %s%n", flujo.getNombreFlujo(), flujo.getFechaUltimaModificacion().toString(), flujo.getEstado().toString());
                outputStream.write(txtLine.getBytes());
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Exporta la lista de flujos en formato CSV.
     */
    public byte[] exportarFlujosToCSV(List<Flow> flujos) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String csvHeader = "Flujo, Fecha de Última Modificación, Estado" + System.lineSeparator();
            outputStream.write(csvHeader.getBytes());

            for (Flow flujo : flujos) {
                String csvLine = String.format("%s, %s, %s%n", flujo.getNombreFlujo(), flujo.getFechaUltimaModificacion().toString(), flujo.getEstado().toString());
                outputStream.write(csvLine.getBytes());
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Asocia una lista de perfiles a un flujo específico.
     *
     * @param id        El ID del flujo al que se quieren asociar los perfiles.
     * @param perfilIds Una lista de IDs de perfiles que se quieren asociar al flujo.
     * @return Un objeto Uni que eventualmente contiene el flujo actualizado.
     */
    @Transactional
    public Uni<String> asociarPerfilesAFlujo(Long flowId, List<Long> profileIds) {
        LOGGER.info(String.format("Iniciando asociación de perfiles al flujo con ID: %d", flowId));
        LOGGER.info(String.format("IDs de perfiles a asociar: %s", profileIds));

        return flujoRepository.findById(flowId)
                .onItem().ifNotNull().transformToUni(flow -> {
                    return profileRepository.findByIds(profileIds)
                            .onItem().transform(profiles -> {
                                if (profiles != null && !profiles.isEmpty()) {
                                    profiles.forEach(profile -> flow.addProfile(profile));
                                    flujoRepository.persistAndFlush(flow); // Guarda los cambios en la base de datos
                                    LOGGER.info(String.format("Perfiles asociados exitosamente al flujo con ID: %d", flowId));
                                    return "Asociación realizada correctamente.";
                                } else {
                                    LOGGER.warning("Error: No se encontraron perfiles.");
                                    return "Error: No se encontraron perfiles.";
                                }
                            });
                })
                .onItem().ifNull().continueWith(() -> {
                    LOGGER.warning(String.format("Error: No se encontró el flujo con ID: %d", flowId));
                    return "Error: No se encontró el flujo.";
                });
    }
}
