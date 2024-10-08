
package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.AuthenticationManager;
import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.domain.RecurringSchedulerAdministration;
import dk.esoftware.recurringscheduler.persistence.Event;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import dk.esoftware.recurringscheduler.rest.dto.EventDTO;
import dk.esoftware.recurringscheduler.rest.dto.EventTypeDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/events")
public class EventResource {

    @Inject
    RecurringSchedulerAdministration recurringSchedulerAdministration;

    @Inject
    ManagerProvider managerProvider;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createEvent(@Context HttpHeaders headers, EventDTO newEvent) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to create an event").build();
        }

        final EventTypeDTO eventTypeDTO = newEvent.type();

        EventType eventType = null;
        if (eventTypeDTO != null) {
            eventType = managerProvider.getEventTypeManager().getEntity(eventTypeDTO.getId());
        }

        UserEntity owner = null;
        if (newEvent.owner() != null) {
            owner = managerProvider.getUserManager().getEntity(newEvent.owner().id());
        }

        final Event newEventEntity = new Event(
                newEvent.name(),
                eventType,
                owner,
                newEvent.possibleTimes(),
                newEvent.chosenTime()
        );

        managerProvider.getEventManager().createEntity(newEventEntity);

        return Response.status(201).entity(EventDTO.createEventTypeDTO(newEventEntity)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getEvents(@Context HttpHeaders headers) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to get events").build();
        }

        final List<Event> entities = managerProvider.getEventManager().getEntities();
        return Response.status(201).entity(entities.stream().map(EventDTO::createEventTypeDTO).collect(Collectors.toList())).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response getEvent(@Context HttpHeaders headers, @PathParam("id") UUID id) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to get event").build();
        }

        final Event entity = managerProvider.getEventManager().getEntity(id);

        if (entity != null) {
            final EventDTO eventTypeDTO = EventDTO.createEventTypeDTO(entity);
            return Response.ok().entity(eventTypeDTO).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @Transactional
    public Response updateEvent(@Context HttpHeaders headers, @PathParam("id") UUID id, EventDTO payload) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to update event").build();
        }

        if (!id.equals(payload.id())) {
            return Response.status(400).entity("Id from path and payload must match").build();
        }

        final Event entity = managerProvider.getEventManager().getEntity(payload.getId());

        EventType eventType = null;
        if (payload.type() != null) {
            eventType = managerProvider.getEventTypeManager().getEntity(payload.type().id());
        }

        UserEntity user = null;
        if (payload.owner() != null) {
            user = managerProvider.getUserManager().getEntity(payload.owner().id());
        }

        entity.setName(payload.name());
        entity.setEventType(eventType);
        entity.setOwner(user);
        entity.setChosenTime(payload.chosenTime());
        entity.setPossibleTimes(payload.possibleTimes());

        return Response.status(201).entity(EventDTO.createEventTypeDTO(entity)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response deleteEvent(@Context HttpHeaders headers, @PathParam("id") UUID id) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to delete event").build();
        }

        managerProvider.getEventManager().deleteEntity(id);
        return Response.ok().build();
    }

}
