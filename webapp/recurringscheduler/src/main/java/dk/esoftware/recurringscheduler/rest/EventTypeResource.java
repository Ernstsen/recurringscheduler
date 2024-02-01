package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.domain.RecurringSchedulerAdministration;
import dk.esoftware.recurringscheduler.persistence.Event;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
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

@Path("/eventTypes")
public class EventTypeResource {

    @Inject
    ManagerProvider managerProvider;

    @Inject
    RecurringSchedulerAdministration recurringSchedulerAdministration;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createEventType(@Context HttpHeaders headers, EventTypeDTO eventTypeDTO) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to create an event type").build();
        }

        final RecurrenceConfiguration recurrenceConfiguration = managerProvider.getRecurrenceConfigurationManager().getEntity(eventTypeDTO.recurrenceConfiguration().id());

        final EventType eventType = new EventType(eventTypeDTO.name(), recurrenceConfiguration);
        managerProvider.getEventTypeManager().createEntity(eventType);

        addUsersToEventType(eventTypeDTO, eventType);

        return Response.status(201).entity(EventTypeDTO.createEventTypeDTO(eventType)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getEventTypes(@Context HttpHeaders headers) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to get event types").build();
        }

        final List<EventType> entities = managerProvider.getEventTypeManager().getEntities();
        return Response.status(200).entity(entities.stream().map(EventTypeDTO::createEventTypeDTO).collect(Collectors.toList())).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response getEventType(@Context HttpHeaders headers, @PathParam("id") UUID id) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to get event type").build();
        }

        final EventType entity = managerProvider.getEventTypeManager().getEntity(id);

        if (entity != null) {
            return Response.ok().entity(EventTypeDTO.createEventTypeDTO(entity)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @Transactional
    public Response updateEventType(@Context HttpHeaders headers, @PathParam("id") UUID id, EventTypeDTO payload) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to update event type").build();
        }

        if (!id.equals(payload.id())) {
            return Response.status(400).entity("Id from path and payload must match").build();
        }

        final RecurrenceConfiguration targetRecurrenceConfiguration = managerProvider.getRecurrenceConfigurationManager().getEntity(payload.recurrenceConfiguration().id());

        final EventType entity = managerProvider.getEventTypeManager().getEntity(id);
        entity.setName(payload.name());
        entity.setRecurrenceConfiguration(targetRecurrenceConfiguration);

        addUsersToEventType(payload, entity);

        return Response.status(201).entity(EventTypeDTO.createEventTypeDTO(entity)).build();
    }

    private void addUsersToEventType(EventTypeDTO payload, EventType entity) {
        if (payload.participatingUsers() != null) {
            payload.participatingUsers().stream()
                    .map(t -> managerProvider.getUserManager().getEntity(t.getId()))
                    .forEach(t -> entity.getParticipatingUsers().add(t));

            entity.getParticipatingUsers().removeIf(t -> payload.participatingUsers().stream().noneMatch(u -> u.getId().equals(t.getId())));
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response deleteEventType(@Context HttpHeaders headers, @PathParam("id") UUID id) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to delete event type").build();
        }

        managerProvider.getEventTypeManager().deleteEntity(id);
        return Response.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    @Path("/{id}/createEvent")
    @Transactional
    public Response createEventFromType(@Context HttpHeaders headers, @PathParam("id") UUID id) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to create event from type").build();
        }

        final EventType entity = managerProvider.getEventTypeManager().getEntity(id);

        if (entity != null) {
            final Event newEvent = recurringSchedulerAdministration.createEventFromEventType(entity);

            return Response.ok().entity(EventDTO.createEventTypeDTO(newEvent)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
