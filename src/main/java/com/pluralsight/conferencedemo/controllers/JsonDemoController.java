//change the package name for what makes sense for your project
package com.pluralsight.conferencedemo.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.GsonBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/v1/demo")
public class JsonDemoController {

    private static final String BOOKS_AS_JSON ="{\"books\":[{\"id\":1,\"title\":\"Hitcher's Guide to the Galaxy\",\"author\":\"Douglas Adams\",\"price\":15.99},{\"id\":2,\"title\":\"The Old Man and the Sea\",\"author\":\"Ernest Hemmingway\",\"price\":10.25},{\"id\":3,\"title\":\"Guide to Java\",\"author\":\"Some guy\",\"price\":20.00}]}";
    private static final String MARKO_AS_JSON = "{\"name\":\"Marko\",\"temperment\":\"intense\",\"talents\":[\"coding\",\"music\",\"mountain biking\"],\"location\":\"Somewhere in Croatia\",\"group\":\"MSSL Dev\",\"age\":35,\"married\":false}";
    private static final String MATT_AS_JSON = "{\"name\":\"Matt\",\"nickname\":\"Matty G\",\"favoriteBeers\":[\"Huss Koffee Kolsh\",\"Anderson Valley Summer Solstice\",\"Michelob Ultra\"],\"location\":\"2nd floor Office in Mesa, AZ\",\"group\":\"PKI Dev\",\"age\":40,\"married\":true}";
    static String SAMPLE_JSON = "{\"field1\":\"value1\",\"field2\":[\"value2\",\"value2\"]}";


    @GetMapping(path ="/marko", produces = MediaType.APPLICATION_JSON_VALUE)
    public String marko() throws Exception {

        //Create the mapper that will handle the json object parsing, creation and output
        ObjectMapper mapper = new ObjectMapper();
        JsonNode markoAsJson = mapper.readTree(MARKO_AS_JSON);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(markoAsJson);
    }

    @GetMapping(path ="/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public String test() throws Exception {

        //Create the mapper that will handle the json object parsing, creation and output
        ObjectMapper mapper = new ObjectMapper();
        JsonNode markoAsJson = mapper.readTree(SAMPLE_JSON);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(markoAsJson);
    }

    @GetMapping(path ="test3", produces = MediaType.APPLICATION_JSON_VALUE)
    public String mockv1ShopperApiGetCallTest3() throws Exception{

        return new GsonBuilder().setPrettyPrinting().create().toJson(SAMPLE_JSON);
    }

    @GetMapping(path ="/matt", produces = MediaType.APPLICATION_JSON_VALUE)
    public String matt() throws Exception {

        //Create the mapper that will handle the json object parsing, creation and output
        ObjectMapper mapper = new ObjectMapper();
        JsonNode mattAsJson = mapper.readTree(MATT_AS_JSON);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mattAsJson);
    }

    @GetMapping(path ="/broforce", produces = MediaType.APPLICATION_JSON_VALUE)
    public String broforce() throws Exception {

        //Goal: combine two separate json objects into 1 larger object
        // https://mkyong.com/java/jackson-tree-model-example/ very helpful
        //Create the mapper that will handle the json object parsing, creation and output
        ObjectMapper mapper = new ObjectMapper();

        JsonNode mattAsJson = mapper.readTree(MATT_AS_JSON);
        JsonNode markoAsJson = mapper.readTree(MARKO_AS_JSON);
        //root will serve as the root Json Object we will stuff everything in to
        ObjectNode root = mapper.createObjectNode();

        //Add matt and marko objects into the root node - leverage the "name" property of the objects
        //to give them both a distinct entry in the root object - basically like  { "marko":{...}, "matt":{...}}
        root.putPOJO(markoAsJson.get("name").asText(),markoAsJson);
        root.putPOJO(mattAsJson.get("name").asText(), mattAsJson);
        //if the "mission" field is missing, add it
        if(root.path("mission").isMissingNode()){
            root.put("mission", "Kick ass, and chew bubblegum if avaialble.");
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
    }


    //Just some Json filtering stuff I was playing around with
    //Very helpful articles: https://www.baeldung.com/jackson-json-node-tree-model & https://www.baeldung.com/jackson-json-to-jsonnode
    @GetMapping(path ="/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public String allbooks() throws Exception {

        //Create the mapper that will handle the json object parsing, creation and output
        ObjectMapper mapper = new ObjectMapper();
        JsonNode booksJsonAsJson = mapper.readTree(BOOKS_AS_JSON);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(booksJsonAsJson);
    }

    //Just the titles of the books
    @GetMapping(path ="/books/justtitles", produces = MediaType.APPLICATION_JSON_VALUE)
    public String justTitles() throws Exception{
        String bookJsonExampleString ="{\"books\":[{\"id\":1,\"title\":\"Hitcher's Guide to the Galaxy\",\"author\":\"Douglas Adams\",\"price\":15.99},{\"id\":2,\"title\":\"The Old Man and the Sea\",\"author\":\"Ernest Hemmingway\",\"price\":10.25},{\"id\":3,\"title\":\"Guide to Java\",\"author\":\"Some guy\",\"price\":20.00}]}";

        //Create the mapper that will handle the json object parsing, creation and output
        ObjectMapper mapper = new ObjectMapper();
        JsonNode booksJsonAsJson = mapper.readTree(BOOKS_AS_JSON);
        JsonNode booksArrayNode = booksJsonAsJson.get("books");

        //Using StreamSupport to make use of filter() - but could also do traditional way below - which would be better for multiple properties
        List<JsonNode> justTitles = StreamSupport.stream(booksArrayNode.spliterator(), false).map(e -> e.get("title")).collect(Collectors.toList());

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.createArrayNode().addAll(justTitles));

        //        ArrayNode bookTitleArrayNodeTraditional = mapper.createArrayNode();
        //        for (JsonNode arrayElement : booksArrayNode){
        //            bookTitleArrayNodeTraditional.add(arrayElement.get("title").asText());
        //        }
        //
        //        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.createArrayNode().addAll(bookTitleArrayNodeTraditional));
    }

    @GetMapping(path ="/books/titleandauthor", produces = MediaType.APPLICATION_JSON_VALUE)
    public String titleAndAuthor() throws Exception{
        String bookJsonExampleString ="{\"books\":[{\"id\":1,\"title\":\"Hitcher's Guide to the Galaxy\",\"author\":\"Douglas Adams\",\"price\":15.99},{\"id\":2,\"title\":\"The Old Man and the Sea\",\"author\":\"Ernest Hemmingway\",\"price\":10.25},{\"id\":3,\"title\":\"Guide to Java\",\"author\":\"Some guy\",\"price\":20.00}]}";

        //Create the mapper that will handle the json object parsing, creation and output
        ObjectMapper mapper = new ObjectMapper();
        JsonNode booksJsonAsJson = mapper.readTree(BOOKS_AS_JSON);
        JsonNode booksArrayNode = booksJsonAsJson.get("books");

        ArrayNode titleAndAuthorArray = mapper.createArrayNode();
        //iterate through array elements and if they have an author and title, make a new object with just those two
        // properties and add them them to the JsonArray to be returned.
        for (JsonNode arrayElement : booksArrayNode) {
            if(arrayElement.hasNonNull("title") && arrayElement.hasNonNull("author")) {
                ObjectNode filteredElement = mapper.createObjectNode();
                filteredElement.put("title", arrayElement.get("title").asText());
                filteredElement.put("author", arrayElement.get("author").asText());
                titleAndAuthorArray.add(filteredElement);
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.createArrayNode().addAll(titleAndAuthorArray));
    }

    @GetMapping(path ="/books/idandtitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public String idAndTitle() throws Exception{
        String bookJsonExampleString ="{\"books\":[{\"id\":1,\"title\":\"Hitcher's Guide to the Galaxy\",\"author\":\"Douglas Adams\",\"price\":15.99},{\"id\":2,\"title\":\"The Old Man and the Sea\",\"author\":\"Ernest Hemmingway\",\"price\":10.25},{\"id\":3,\"title\":\"Guide to Java\",\"author\":\"Some guy\",\"price\":20.00}]}";

        //Create the mapper that will handle the json object parsing, creation and output
        ObjectMapper mapper = new ObjectMapper();
        JsonNode booksJsonAsJson = mapper.readTree(BOOKS_AS_JSON);
        JsonNode booksArrayNode = booksJsonAsJson.get("books");

        //alternate method using retain() and remove() for quicker filtering

        ArrayNode idAndTitleArray = mapper.createArrayNode();
        // GOAL: filter the JSON object array to only the "id" and "title" properties, or to put it another way, remove the "price" and "author" attributes
        for (JsonNode arrayElement : booksArrayNode) {
            // essentially cast from JsonNode to ObjectNode to use the Object Node methods - seems to work - but not sure if it works all the time
            ObjectNode filteredElement = (ObjectNode) arrayElement;
            //for a more robust, but expensive way to do that you could make an actual copy of the JsonNode to an ObjectNode like this:
            // ObjectNode filteredElement = arrayElement.deepCopy()
            // and then you could use that below instead

            filteredElement = filteredElement.retain("id", "title");
            //line below is not needed, but just to show the remove method also exists and can be helpful - by using retain above, author and price should already be removed since they were nto retained.
            filteredElement = filteredElement.remove(Arrays.asList("author", "price"));
            idAndTitleArray.add(filteredElement);
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.createArrayNode().addAll(idAndTitleArray));
    }

}
