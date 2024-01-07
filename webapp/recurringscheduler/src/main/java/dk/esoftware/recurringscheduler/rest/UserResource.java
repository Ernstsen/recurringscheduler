package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.DomainEntityManager;
import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import dk.esoftware.recurringscheduler.rest.dto.EventTypeDTO;
import dk.esoftware.recurringscheduler.rest.dto.UserDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/users")
public class UserResource {

    @Inject
    ManagerProvider managerProvider;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createUser(UserDTO userDTO) {
        final UserEntity newUser = new UserEntity(userDTO.email(), userDTO.name());

        if(userDTO.eventTypes() != null){
            final DomainEntityManager<EventType> eventTypeManager = managerProvider.getEventTypeManager();
            userDTO.eventTypes().stream()
                    .map(t -> eventTypeManager.getEntity(t.getId()))
                    .forEach(t -> newUser.getEventTypes().add(t));
        }

        managerProvider.getUserManager().createEntity(newUser);

        return Response.status(201).entity(UserDTO.createUserDTO(newUser)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<UserDTO> getUsers() {
        final List<UserEntity> entities = managerProvider.getUserManager().getEntities();
        return entities.stream().map(UserDTO::createUserDTO).collect(Collectors.toList());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response getUser(@PathParam("id") UUID id) {
        final UserEntity entity = managerProvider.getUserManager().getEntity(id);

        if(entity != null){
            return Response.ok().entity(UserDTO.createUserDTO(entity)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @Transactional
    public Response updateUser(@PathParam("id") UUID id, UserDTO payload) {
        if (!id.equals(payload.id())) {
            return Response.status(400).entity("Id from path and payload must match").build();
        }

        final UserEntity entity = managerProvider.getUserManager().getEntity(id);
        entity.setName(payload.name());
        entity.setEmail(payload.email());

        if(payload.eventTypes() != null){
            final DomainEntityManager<EventType> eventTypeManager = managerProvider.getEventTypeManager();

            final Set<EventTypeDTO> newEventSet = payload.eventTypes();
            entity.getEventTypes().removeIf(t -> newEventSet.stream()
                    .map(EventTypeDTO::getId)
                    .noneMatch(nt -> nt.equals(t.getId())));

            final Set<UUID> currentTypes = entity.getEventTypes().stream().map(EventType::getId).collect(Collectors.toSet());
            newEventSet.stream()
                    .map(EventTypeDTO::getId)
                    .filter(eventTypeId -> !currentTypes.contains(eventTypeId))
                    .map(eventTypeManager::getEntity)
                    .forEach(evenType -> entity.getEventTypes().add(evenType));
        }

        return Response.status(201).entity(UserDTO.createUserDTO(entity)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") UUID id) {
        managerProvider.getUserManager().deleteEntity(id);
        return Response.ok().build();
    }

}
