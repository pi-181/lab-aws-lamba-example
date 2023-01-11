package io.invokegs.aws_lambda_sample;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        response.setHeaders(headers);


        Boolean base64 = input.getIsBase64Encoded();

        String body = input.getBody();
        if (base64 != null && base64) {
            body = new String(Base64.getDecoder().decode(body), StandardCharsets.UTF_8);
        }

        try {
            final BucketPayload payload
                    = gson.fromJson(body, BucketPayload.class);
            uploadToBucket(payload);
        } catch (Exception e) {
            response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
            response.setBody(e.getMessage());
            return response;
        }

        response.setStatusCode(HttpStatusCode.ACCEPTED);
        response.setBody("Accepted");
        return response;
    }

    private void uploadToBucket(BucketPayload payload) throws AwsServiceException {
        try (S3Client s3 = S3Client.create()) {
            final PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(payload.getBucketName())
                    .key(payload.getKey())
                    .build();

            s3.putObject(request, RequestBody.fromString(payload.getPayload(), StandardCharsets.UTF_8));
        }
    }
}