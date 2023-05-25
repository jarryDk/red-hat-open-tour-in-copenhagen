package dk.jarry.quiz.boundary;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("begin")
public class BeginResource {

    @Inject
    Template begin;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return begin.instance();
    }

}
