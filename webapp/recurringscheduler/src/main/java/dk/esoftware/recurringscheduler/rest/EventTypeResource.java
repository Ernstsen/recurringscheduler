package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import dk.esoftware.recurringscheduler.rest.dto.EventTypeDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/eventType")
public class EventTypeResource {

    @Inject
    ManagerProvider managerProvider;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<EventTypeDTO> getEventTypes() {
        final List<EventType> entities = managerProvider.getEventTypeManager().getEntities();
        return entities.stream().map(EventTypeDTO::createEventTypeDTO).collect(Collectors.toList());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public EventTypeDTO getEventType(@PathParam("id") UUID id) {
        final EventType entity = managerProvider.getEventTypeManager().getEntity(id);
        return EventTypeDTO.createEventTypeDTO(entity);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createEventType(EventTypeDTO configurationDTO) {
        final RecurrenceConfiguration recurrenceConfiguration = managerProvider.getRecurranceConfigurationManager().getEntity(configurationDTO.recurrenceConfiguration().id());

        final EventType eventType = new EventType(configurationDTO.name(), recurrenceConfiguration);
        managerProvider.getEventTypeManager().createEntity(eventType);

        return Response.status(201).entity(EventTypeDTO.createEventTypeDTO(eventType)).build();
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @Transactional
    public Response updateEventType(@PathParam("id") UUID id, EventTypeDTO payload) {
        if (!id.equals(payload.id())) {
            return Response.status(400).entity("Id from path and payload must match").build();
        }

        final RecurrenceConfiguration targetRecurrenceConfiguration = managerProvider.getRecurranceConfigurationManager().getEntity(payload.recurrenceConfiguration().id());

        final EventType entity = managerProvider.getEventTypeManager().getEntity(id);
        entity.setName(payload.name());
        entity.setRecurrenceConfiguration(targetRecurrenceConfiguration);

        return Response.status(201).entity(EventTypeDTO.createEventTypeDTO(entity)).build();
    }

}
