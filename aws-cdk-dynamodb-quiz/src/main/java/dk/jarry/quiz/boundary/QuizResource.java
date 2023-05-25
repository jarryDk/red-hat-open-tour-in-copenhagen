package dk.jarry.quiz.boundary;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.openapi.annotations.Operation;

import dk.jarry.quiz.entity.Quiz;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/quizs")
public class QuizResource {

	@Inject
	QuizService quizService;

	@POST
	@Operation(description = "Create a new Quiz")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Quiz Quiz) {
		return Response.status(201) //
				.entity(quizService.create(Quiz)) //
				.build();
	}

	@GET
	@Path("{uuid}")
	@Operation(description = "Get a specific Quiz by uuid")
	public Quiz read(@PathParam("uuid") String uuid) {
		Optional<Quiz> read = quizService.read(uuid);
		return read.map(v -> {
			return v;
		}).orElseThrow(() -> new WebApplicationException( //
				"Quiz with uuid of " + uuid + " does not exist.", //
				Response.Status.NOT_FOUND));
	}

	@GET
	@Path("/tablename")
	@Operation(description = "Get tablename")
	public String getTableName() {
		return quizService.getTableName();
	}

	@PUT
	@Path("{uuid}")
	@Operation(description = "Update an exiting Quiz")
	public Quiz update(@PathParam("uuid") String uuid, Quiz Quiz) {
		Optional<Quiz> update = quizService.update(uuid, Quiz);
		return update.map(v -> {
			return v;
		}).orElseThrow(() -> new WebApplicationException( //
				"Quiz with uuid of " + uuid + " does not exist.", //
				Response.Status.NOT_FOUND));
	}

	@DELETE
	@Path("{uuid}")
	@Operation(description = "Delete a specific Quiz by uuid")
	public void delete(@PathParam("uuid") String uuid) {
		quizService.delete(uuid);
	}

	@GET
	@Operation(description = "Get all the Quizs")
	public List<Quiz> findAll( //
			@DefaultValue("0") @QueryParam("from") Integer from, //
			@DefaultValue("100") @QueryParam("limit") Integer limit) {
		return quizService.findAll();
	}
}