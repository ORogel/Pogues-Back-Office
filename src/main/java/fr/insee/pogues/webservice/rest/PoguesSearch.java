package fr.insee.pogues.webservice.rest;

import fr.insee.pogues.search.model.PoguesHit;
import fr.insee.pogues.search.model.PoguesQuery;
import fr.insee.pogues.search.model.Questionnaire;
import fr.insee.pogues.search.service.SearchService;
import fr.insee.pogues.search.source.ColecticaSourceImporter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Component
@Path("/search")
@Api(value = "Pogues Search")
public class PoguesSearch {

    @Autowired
    SearchService searchService;

    @Autowired
    ColecticaSourceImporter colecticaSourceImporter;


    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @ApiOperation(
            value = "Search Item",
            notes = "Search the application index for item across types`"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    public List<PoguesHit> searchQuestionnaire(PoguesQuery query) throws Exception {
        try {
            String[] types = query.getTypes().toArray(new String[query.getTypes().size()]);
            return searchService.searchByLabel(query.getFilter(), types);
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("questionnaire")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @ApiOperation(
            value = "Index Questionnaire",
            notes = "Index a new `Questionnaire`"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Entity already exists"),
            @ApiResponse(code = 500, message = "Unexpected error")

    })
    public Response indexQuestionnaire(Questionnaire item) throws Exception {
        try {
            IndexResponse response = searchService.save("questionnaire", item);
            return Response.status(CREATED).entity(response).build();
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DELETE
    @Path("questionnaire/{id}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @ApiOperation(
            value = "Delete Questionnaire from Index",
            notes = "Index a new `Questionnaire`"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    public Response deleteQuestionnaire(
            @PathParam(value = "id") String id
    ) throws Exception {
        try {
            DeleteResponse response = searchService.delete("questionnaire", id);
            return Response.status(NO_CONTENT).entity(response).build();
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @GET
    @Path("import")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Import indexes from Colectica",
            notes = "This require a living instance of colectica aswell as a up and running elasticsearch cluster",
            response = String.class)
    public Response getQuestionnaire() {
        try {
            colecticaSourceImporter.source();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }


}