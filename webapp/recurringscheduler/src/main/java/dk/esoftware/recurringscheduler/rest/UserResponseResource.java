
package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.domain.RecurringSchedulerAdministration;
import dk.esoftware.recurringscheduler.persistence.Event;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import dk.esoftware.recurringscheduler.persistence.UserResponse;
import dk.esoftware.recurringscheduler.rest.dto.EventDTO;
import dk.esoftware.recurringscheduler.rest.dto.UserResponseDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/userResponse")
public class UserResponseResource {

    @Inject
    RecurringSchedulerAdministration recurringSchedulerAdministration;

    @Inject
    ManagerProvider managerProvider;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createUserResponse(@Context HttpHeaders headers, UserResponseDTO newResponse) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to create an event").build();
        }


        final UserResponse userResponseEntity = new UserResponse(
                managerProvider.getEventManager().getEntity(newResponse.eventId()),
                managerProvider.getUserManager().getEntity(newResponse.userEntityId()),
                newResponse.chosenDates()
        );
        managerProvider.getUserResponseManager().createEntity(userResponseEntity);

        return Response.status(201).entity(UserResponseDTO.createUserResponseDTO(userResponseEntity)).build();
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    @Path("/{eventId}")
//    public Response getUserResponsesFromEvent(@Context HttpHeaders headers, @PathParam("eventId") UUID eventId) {
//        final String token = HeaderUtilities.getAuthorizationHeader(headers);
//        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);
//
//        if (!isAuthenticated) {
//            return Response.status(401).entity("Must be authenticated to get events").build();
//        }
//
//        final Event event = managerProvider.getEventManager().getEntity(eventId);
//
//        return Response.status(201).entity(event.getUserResponses().stream().map(UserResponseDTO::createUserResponseDTO).collect(Collectors.toList())).build();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response getUserResponse(@Context HttpHeaders headers, @PathParam("id") UUID id) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to get event").build();
        }

        final UserResponse entity = managerProvider.getUserResponseManager().getEntity(id);

        if (entity != null) {
            final UserResponseDTO userResponseDTO = UserResponseDTO.createUserResponseDTO(entity);
            return Response.ok().entity(userResponseDTO).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @Transactional
    public Response updateUserResponse(@Context HttpHeaders headers, @PathParam("id") UUID id, UserResponseDTO payload) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to update event").build();
        }

        if (!id.equals(payload.id())) {
            return Response.status(400).entity("Id from path and payload must match").build();
        }

        final UserResponse entity = managerProvider.getUserResponseManager().getEntity(payload.id());

        if (entity == null) {
            return Response.status(404).entity("No event with id " + payload.id()).build();
        }

        entity.setUserEntity(managerProvider.getUserManager().getEntity(payload.userEntityId()));
        entity.setEvent(managerProvider.getEventManager().getEntity(payload.eventId()));
        entity.setChosenDates(payload.chosenDates());

        return Response.status(201).entity(UserResponseDTO.createUserResponseDTO(entity)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response deleteUserResponse(@Context HttpHeaders headers, @PathParam("id") UUID id) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to delete event").build();
        }

        managerProvider.getUserResponseManager().deleteEntity(id);
        return Response.ok().build();
    }

}
