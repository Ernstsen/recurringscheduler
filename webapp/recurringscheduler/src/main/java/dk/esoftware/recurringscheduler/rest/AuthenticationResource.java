package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.persistence.AuthenticatedSession;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import dk.esoftware.recurringscheduler.rest.dto.AuthenticationResponse;
import dk.esoftware.recurringscheduler.rest.dto.LoginRequest;
import dk.esoftware.recurringscheduler.rest.dto.UserDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/authentication")
public class AuthenticationResource {

    @Inject
    ManagerProvider managerProvider;

    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response login(LoginRequest loginRequest) {
        if (loginRequest.email() == null || loginRequest.email().isBlank()
                || loginRequest.password() == null || loginRequest.password().isBlank()) {
            return Response.status(400).entity("Must supply both email and password to sign in").build();
        }

        final UserEntity userEntity = managerProvider.getUserManager().getUserByEmail(loginRequest.email());

        if (userEntity == null) {
            return Response.status(401).entity("Wrong username or password").build();
        }

        final boolean passwordMatches = userEntity.getUserCredentialses().stream()//TODO: THIS IS HORRIBLE
                .anyMatch(userCredentialsEntity -> userCredentialsEntity.getValue().equals(loginRequest.password()));

        if (!passwordMatches) {
            return Response.status(401).entity("Wrong username or password").build();
        }

        final AuthenticatedSession entity = new AuthenticatedSession(userEntity);
        managerProvider.getAuthenticatedSessionManager().createEntity(entity);

        return Response.status(201).entity(new AuthenticationResponse(entity.getId().toString(), UserDTO.createUserDTO(userEntity))).build();
    }

    @GET
    @Path("/isAuthenticated")
    @Produces({MediaType.APPLICATION_JSON})
    public Response isAuthenticated(@Context HttpHeaders headers) {
        final String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return Response.status(401).entity(false).build();
        }

        final String token = authorizationHeader.replace("Bearer ", "");

        final AuthenticatedSession sessions = managerProvider.getAuthenticatedSessionManager().getEntity(UUID.fromString(token));

        if(sessions == null) {
            return Response.status(401).entity(false).build();
        } else {
            return Response.status(200).entity(true).build();
        }
    }
}
