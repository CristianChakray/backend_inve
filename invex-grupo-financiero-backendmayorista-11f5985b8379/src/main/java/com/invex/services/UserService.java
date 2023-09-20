package com.invex.services;

import com.invex.entities.user.User;
import com.invex.repositories.ProfileRepository;
import com.invex.repositories.UserRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    ProfileRepository profileRepository;

    public Uni<List<User>> getAllUsers() {
        return userRepository.listAll();
    }
    public Uni<List<User>> getPaginatedUsers(int page, int size) {
        return userRepository.findPaginated(page, size);
    }

    /**
     * Crea un nuevo Usuario.
     * @param user El user a crear.
     * @return Un objeto Uni que eventualmente contiene el user creado.
     */
    public Uni<Object> createUser(User user) {
        user.setCreationDate(LocalDateTime.now());
        user.setLastModificationDate(LocalDateTime.now());
        return userRepository.persistAndFlush(user)
                .onItem().ignore().andSwitchTo(Uni.createFrom().item(() -> user));
    }

    @Transactional
    public Uni<Uni<User>> createUserWithProfile(User user, Long profileId) {
        return profileRepository.findById(profileId)
                .onItem().ifNotNull().transform(profile -> {
                    user.setProfile(profile);
                    user.setCreationDate(LocalDateTime.now());
                    user.setLastModificationDate(LocalDateTime.now());
                    return userRepository.persistAndFlush(user)
                            .onItem().ignore().andSwitchTo(Uni.createFrom().item(() -> user));
                })
                .onItem().ifNull().continueWith(Uni.createFrom().nullItem());
    }

    @Transactional
    public Uni<Uni<User>> updateUserProfile(User user, Long newProfileId) {
        return profileRepository.findById(newProfileId)
                .onItem().ifNotNull().transform(profile -> {
                    user.setProfile(profile);
                    return userRepository.persistAndFlush(user);
                });
    }

    /**
     * Ordena de manera ascendente y descendente el contenido de la tabla en cada columna
     */
    public Uni<List<User>> sortUsers(String columna, boolean ascendente) {
        String orden = ascendente ? "ASC" : "DESC";
        return userRepository.list("ORDER BY " + columna + " " + orden);
    }

    /**
     * Realizar busqueda con cualquier parametro
     * @param parameter
     * @return Objetos relacionados al campo ingresado
     */
    public Uni<List<User>> searchUsersByParameter(String parameter) {
        return userRepository.list("UPPER(userIdentifier) LIKE UPPER(?1) OR UPPER(status) LIKE UPPER(?1)", "%" + parameter + "%");
    }

    public Uni<User> findByName(String identifier) {
        return userRepository.find("userIdentifier", identifier).firstResult();
    }

    @Transactional
    public Uni<Void> desactivateUser(Long id) {
        return userRepository.findById(id)
                .onItem().ifNotNull().transformToUni(user -> {
                    user.setStatus(User.UserStatus.INACTIVO);
                    return userRepository.persistAndFlush(user);
                })
                .onItem().ignore().andSwitchTo(Uni.createFrom().voidItem());
    }

    public Uni<User> findUserById(Long userId) {
        return User.findById(userId);
    }

    /**
     * Exporta la lista de usuarios en formato XLS.
     * @param users Lista de usuarios a exportar.
     * @return Un arreglo de bytes que contiene los datos del archivo XLS.
     */
    public byte[] exportUsersToXLS(List<User> users) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nombre");
            headerRow.createCell(1).setCellValue("Usuario");
            headerRow.createCell(2).setCellValue("Fecha de última modificación");
            headerRow.createCell(3).setCellValue("Estatus");

            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getUserName());
                row.createCell(1).setCellValue(user.getUserIdentifier());
                row.createCell(2).setCellValue(user.getLastModificationDate().toString());  // Convertir LocalDateTime a String
                row.createCell(3).setCellValue(user.getStatus().toString());

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
    public byte[] exportUsersToTXT(List<User> users) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String txtHeader = "Nombre, Usuario, Fecha de Última Modificación, Estatus" + System.lineSeparator();
            outputStream.write(txtHeader.getBytes());
            for (User user : users) {
                String txtLine = String.format("%s, %s, %s, %s%n", user.getUserName(), user.getUserIdentifier(), user.getLastModificationDate().toString(), user.getStatus().toString());
                outputStream.write(txtLine.getBytes());
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] exportUsersToCSV(List<User> users) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String csvHeader = "Nombre, Usuario, Fecha de Última Modificación, Estatus" + System.lineSeparator();
            outputStream.write(csvHeader.getBytes());
            for (User user : users) {
                String csvLine = String.format("%s, %s, %s, %s%n", user.getUserName(), user.getUserIdentifier(), user.getLastModificationDate().toString(), user.getStatus().toString());
                outputStream.write(csvLine.getBytes());
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}