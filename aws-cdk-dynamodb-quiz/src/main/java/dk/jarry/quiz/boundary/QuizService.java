package dk.jarry.quiz.boundary;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dk.jarry.quiz.entity.Quiz;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

@ApplicationScoped
public class QuizService extends AbstractService {

	@Inject
	DynamoDbClient dynamoDB;

	public Quiz create(Quiz quiz) {
		dynamoDB.putItem(getPutItemRequest(quiz));
		return quiz;
	}

	public Optional<Quiz> read(String uuid) {
		GetItemResponse getItem = dynamoDB.getItem(getGetItemRequest(uuid));
		if (getItem.hasItem()) {
			return Optional.of(Quiz.from(getItem.item()));
		}
		return Optional.empty();
	}

	public Optional<Quiz> update(String uuid, Quiz quiz) {
		UpdateItemResponse updateItem = dynamoDB.updateItem(getUpdateItemRequest(uuid, quiz));
		if (updateItem.hasAttributes()) {
			return Optional.of(Quiz.from(updateItem.attributes()));
		}
		return Optional.empty();
	}

	public void delete(String uuid) {
		dynamoDB.deleteItem(getDeleteItemRequest(uuid));
	}

	public List<Quiz> findAll() {
		return dynamoDB.scanPaginator(getScanRequest()).items().stream() //
				.map(Quiz::from) //
				.collect(Collectors.toList());
	}

	@Provider
	public static class ErrorMapper implements ExceptionMapper<Exception> {

		@Override
		public Response toResponse(Exception exception) {
			int code = 500;
			if (exception instanceof WebApplicationException) {
				code = ((WebApplicationException) exception).getResponse().getStatus();
			}
			return Response.status(code).entity(Json.createObjectBuilder() //
					.add("error", (exception.getMessage() != null ? exception.getMessage() : "")) //
					// .add("stackTrace", stackTrace(exception)) //
					.add("code", code).build()).build();
		}

		String stackTrace(Exception exception) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			exception.printStackTrace(printWriter);
			return writer.toString();
		}

	}

}
