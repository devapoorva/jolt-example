package org.example;

import java.io.*;
import java.util.List;
import java.util.Map;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.bazaarvoice.jolt.Transform;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class App {

    public static void main(String[] args) {
        // Define paths for input, spec, and output JSON files
        String inputJsonFilePath = "src/main/resources/input.json";
        String specJsonFilePath = "src/main/resources/spec.json";
        String outputJsonFilePath = "src/main/resources/output.json";

        try {
            // Load the input JSON from file
            Map<String, Object> inputJson = loadJson(inputJsonFilePath);

            // Load the Jolt spec JSON from file
            List<Map<String, Object>> specJson = loadJsonAsList(specJsonFilePath);

            // Create the Chainr transformer from the spec
            Transform transform = Chainr.fromSpec(specJson);

            // Apply the transformation
            Object transformedJson = transform.transform(inputJson);

            // Write the transformed JSON to the output file
            writeJsonToFile(outputJsonFilePath, transformedJson);

            // Optionally, print the transformed JSON to the console
            printTransformedJson(transformedJson);

            System.out.println("Transformation completed. Output saved to: " + outputJsonFilePath);

        } catch (Exception e) {
            System.err.println("Error during transformation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to load JSON from a file
    private static Map<String, Object> loadJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), Map.class);
    }

    // Helper method to load JSON from a file using Jackson (returns List of Maps)
    private static List<Map<String, Object>> loadJsonAsList(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), List.class);
    }

    // Helper method to write JSON to a file
    private static void writeJsonToFile(String filePath, Object json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty print

        // Write the JSON to file
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            objectMapper.writeValue(fileWriter, json);
        }
    }

    // Helper method to pretty-print the transformed JSON to the console
    private static void printTransformedJson(Object transformedJson) {
        String transformedJsonPretty = JsonUtils.toJsonString(transformedJson);
        System.out.println("Transformed JSON: ");
        System.out.println(transformedJsonPretty);
    }
}

