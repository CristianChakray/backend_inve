package com.invex.dto.user;

import com.invex.entities.profile.Profile;
import com.invex.entities.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DTOConverter {

    public static User fromUserDTO(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setUserName(userDTO.getUserName());
        user.setUserIdentifier(userDTO.getUserIdentifier());
        user.setCreationDate(userDTO.getCreationDate());
        user.setLastModificationDate(userDTO.getLastModificationDate());
        user.setStatus(User.UserStatus.valueOf(userDTO.getStatus().name()));

        Profile profile = fromProfileSummaryDTO(userDTO.getProfile());
        user.setProfile(profile);

        return user;
    }

    public static User fromUserSummaryDTO(UserSummaryDTO userSummaryDTO) {
        if (userSummaryDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userSummaryDTO.getId());
        user.setUserName(userSummaryDTO.getUserName());
        user.setStatus(User.UserStatus.valueOf(userSummaryDTO.getStatus().name()));

        return user;
    }

    public static Profile fromProfileSummaryDTO(ProfileSummaryDTO profileDTO) {
        if (profileDTO == null) {
            return null;
        }

        Profile profile = new Profile();
        profile.setId(profileDTO.getId());
        profile.setName(profileDTO.getName());

        return profile;
    }

    public static Profile fromProfileDTO(ProfileDTO profileDTO) {
        Profile profile = fromProfileSummaryDTO(profileDTO);
        if (profile == null) {
            return null;
        }

        profile.setCreateRequests(profileDTO.getCreateRequests());
        profile.setActive(profileDTO.getActive());
        profile.setDateDisabled(profileDTO.getDateDisabled());
        profile.setDateAuthorization(profileDTO.getDateAuthorization());
        profile.setUpdatedAt(profileDTO.getUpdatedAt());
        profile.setCreatedAt(profileDTO.getCreatedAt());

        // Conversion of list of users in the DTO to entities.
        List<User> users = fromUserSummaryDTOList(profileDTO.getUsers());
        for (User user : users) {
            profile.addUser(user);
        }

        return profile;
    }

    public static List<User> fromUserSummaryDTOList(List<UserSummaryDTO> userDTOList) {
        if (userDTOList == null) {
            return Collections.emptyList();
        }

        List<User> users = new ArrayList<>();
        for (UserSummaryDTO userDTO : userDTOList) {
            User user = fromUserSummaryDTO(userDTO);
            users.add(user);
        }
        return users;
    }

    // ... Additional converters for Flow
}
