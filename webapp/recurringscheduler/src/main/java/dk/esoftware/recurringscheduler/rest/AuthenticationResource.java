package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import dk.esoftware.recurringscheduler.rest.dto.AuthenticationResponse;
import dk.esoftware.recurringscheduler.rest.dto.LoginRequest;
import dk.esoftware.recurringscheduler.rest.dto.UserDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/authentication")
public class AuthenticationResource {

    @Inject
    ManagerProvider managerProvider;

    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createEvent(LoginRequest loginRequest) {

        if (loginRequest.email() == null || loginRequest.email().isBlank()
                || loginRequest.password() == null || loginRequest.password().isBlank()) {
            return Response.status(400).entity("Must supply both email and password to sign in").build();
        }

        final UserEntity userEntity = managerProvider.getUserManager().getUserByEmail(loginRequest.email());

        if(userEntity == null) {
            return Response.status(401).entity("Wrong username or password").build();
        }

        final boolean passwordMatches = userEntity.getUserCredentialses().stream()
                .anyMatch(userCredentialsEntity -> userCredentialsEntity.getValue().equals(loginRequest.password()));

        if(!passwordMatches) {
            return Response.status(401).entity("Wrong username or password").build();
        }

        return Response.status(201).entity(new AuthenticationResponse("token", UserDTO.createUserDTO(userEntity))).build();
    }


}
