package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
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
            return Response.status(400).build();
        }

        final UserEntity userEntity = managerProvider.getUserManager().getUserByEmail(loginRequest.email());

        return Response.status(201).entity(UserDTO.createUserDTO(userEntity)).build();
    }


}
