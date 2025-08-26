package com.sayem.trackfit.aiservice.mapper;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sayem.trackfit.aiservice.entity.Recommendation;

public class RecommendationMapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Maps the full analytics JSON into your Recommendation entity
     * without modifying the entity class. Uses all fields:
     * - Stores summary + analysis + predictions as a compact JSON string in `recommendations`
     * - improvements -> entity.improvements
     * - suggestions -> fitnessAdvice + nutritionAdvice + top-level suggestions + nextBestActivity
     * - safety -> recoveryTips
     */
    public static Recommendation mapToEntity(String analyticsJson,
            								 String activityId,
            								 String userId,
            								 String activityType) {
        try {
            JsonNode root = MAPPER.readTree(analyticsJson);

            // ---- Extract core sections ----
            String summary = getText(root, "summary");

            JsonNode analysis = root.path("analysis");          // effortLevel, consistency, calorieEfficiency, keyObservations
            JsonNode recs     = root.path("recommendations");   // fitnessAdvice, nutritionAdvice, recoveryTips
            JsonNode preds    = root.path("predictions");       // trend, nextBestActivity

            // Build the JSON we’ll store in the String field `recommendations` so we don't lose structured data
            ObjectNode toStore = MAPPER.createObjectNode();
            toStore.put("summary", summary);
            toStore.set("analysis", analysis.isMissingNode() ? MAPPER.createObjectNode() : analysis.deepCopy());
            toStore.set("predictions", preds.isMissingNode() ? MAPPER.createObjectNode() : preds.deepCopy());
            toStore.put("activityType", activityType);

            String recommendationsString = MAPPER.writeValueAsString(toStore);

            // ---- Lists for entity fields ----
            List<String> improvements = toList(root.path("improvements"));

            // suggestions := fitnessAdvice + nutritionAdvice + top-level suggestions + "next best activity"
            List<String> fitnessAdvice = toList(recs.path("fitnessAdvice"));
            List<String> nutritionAdvice = toList(recs.path("nutritionAdvice"));
            List<String> topSuggestions = toList(root.path("suggestions"));
            List<String> suggestionsMerged = new ArrayList<>();

            suggestionsMerged.addAll(fitnessAdvice);
            suggestionsMerged.addAll(nutritionAdvice);
            suggestionsMerged.addAll(topSuggestions);

            // Append nextBestActivity as a suggestion so it isn't lost
            String nextBest = getText(preds, "nextBestActivity");
            if (nextBest != null && !nextBest.isBlank()) {
                suggestionsMerged.add("Next best activity: " + nextBest);
            }

            // De-duplicate while preserving order
            List<String> suggestions = suggestionsMerged.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());

            // safety := recoveryTips
            List<String> safety = toList(recs.path("recoveryTips"));

            // ---- Build entity ----
            return Recommendation.builder()
                    .activityId(activityId)
                    .userId(userId)
                    .activityType(activityType)
                    .recommendations(recommendationsString)  // JSON string preserving summary+analysis+predictions
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .cratedAt(LocalDateTime.now())           // will be overridden by @CreatedDate if auditing is on
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to map analytics JSON to Recommendation", e);
        }
    }

    // --- helpers ---
    private static List<String> toList(JsonNode node) {
        if (node == null || !node.isArray()) return Collections.emptyList();
        List<String> out = new ArrayList<>();
        node.forEach(n -> {
            if (n != null && n.isTextual()) out.add(n.asText());
        });
        return out;
    }

    private static String getText(JsonNode parent, String field) {
        if (parent == null) return null;
        JsonNode n = parent.get(field);
        return (n != null && n.isTextual()) ? n.asText() : null;
    }
}

