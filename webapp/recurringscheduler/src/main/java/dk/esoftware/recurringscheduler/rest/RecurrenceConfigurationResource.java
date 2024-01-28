package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.domain.RecurringSchedulerAdministration;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import dk.esoftware.recurringscheduler.rest.dto.RecurrenceConfigurationDTO;
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

@Path("/recurrenceConfigurations")
public class RecurrenceConfigurationResource {

    @Inject
    ManagerProvider managerProvider;

    @Inject
    RecurringSchedulerAdministration recurringSchedulerAdministration;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createRecurrenceConfiguration(@Context HttpHeaders headers, RecurrenceConfigurationDTO configurationDTO) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to create a recurrence configuration").build();
        }

        final RecurrenceConfiguration newConfiguration = new RecurrenceConfiguration(configurationDTO.name(), configurationDTO.timeUnit(), configurationDTO.occurrencesPerTimePeriod());
        managerProvider.getRecurrenceConfigurationManager().createEntity(newConfiguration);

        return Response.status(201).entity(RecurrenceConfigurationDTO.createRecurrenceConfigurationDTO(newConfiguration)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getRecurrenceConfigurations(@Context HttpHeaders headers) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to get recurrence configurations").build();
        }

        final List<RecurrenceConfiguration> entities = managerProvider.getRecurrenceConfigurationManager().getEntities();
        return Response.status(200).entity(entities.stream().map(RecurrenceConfigurationDTO::createRecurrenceConfigurationDTO).collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response getRecurrenceConfiguration(@Context HttpHeaders headers, @PathParam("id") UUID id) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to get recurrence configurations").build();
        }

        final RecurrenceConfiguration entity = managerProvider.getRecurrenceConfigurationManager().getEntity(id);

        if (entity != null) {
            return Response.ok().entity(RecurrenceConfigurationDTO.createRecurrenceConfigurationDTO(entity)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @Transactional
    public Response updateRecurrenceConfiguration(@Context HttpHeaders headers, @PathParam("id") UUID id, RecurrenceConfigurationDTO payload) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to update a recurrence configuration").build();
        }

        if (!id.equals(payload.id())) {
            return Response.status(400).entity("Id from path and payload must match").build();
        }

        final RecurrenceConfiguration entity = managerProvider.getRecurrenceConfigurationManager().getEntity(id);
        entity.setName(payload.name());
        entity.setOccurrencesPerTimePeriod(payload.occurrencesPerTimePeriod());
        entity.setTimeUnit(payload.timeUnit());

        return Response.status(201).entity(entity).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response deleteRecurrenceConfiguration(@Context HttpHeaders headers, @PathParam("id") UUID id) {
        final String token = HeaderUtilities.getAuthorizationHeader(headers);
        final boolean isAuthenticated = recurringSchedulerAdministration.isUserAuthenticated(token);

        if (!isAuthenticated) {
            return Response.status(401).entity("Must be authenticated to delete a recurrence configuration").build();
        }

        managerProvider.getRecurrenceConfigurationManager().deleteEntity(id);
        return Response.ok().build();
    }

}
