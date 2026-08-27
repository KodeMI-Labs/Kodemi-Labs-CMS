package com.kodemi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.PublicAccessBlockConfiguration;
import com.amazonaws.services.s3.model.SetPublicAccessBlockRequest;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3BucketInitializer {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    @PostConstruct
    public void initBucket() {
        try {
            boolean exists = bucketExistsInThisAccount();

            if (!exists) {
                log.info("Creating S3 bucket '{}' in region '{}'", bucketName, region);
                CreateBucketRequest req = new CreateBucketRequest(bucketName, region);
                amazonS3.createBucket(req);
                log.info("S3 bucket created: {}", bucketName);
                // Give AWS a moment to propagate
                Thread.sleep(2000);
            } else {
                log.info("S3 bucket already exists: {}", bucketName);
            }

            disableBlockPublicAccess();
            applyPublicReadPolicy();

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.warn("S3 bucket init error for '{}': {}", bucketName, e.getMessage());
        }
    }

    /**
     * Uses getBucketLocation (throws 403 if bucket belongs to another account,
     * throws NoSuchBucket if it doesn't exist) to confirm ownership.
     */
    private boolean bucketExistsInThisAccount() {
        try {
            amazonS3.getBucketLocation(bucketName);
            return true; // bucket exists AND belongs to this account
        } catch (AmazonS3Exception e) {
            if ("NoSuchBucket".equals(e.getErrorCode())) {
                return false;
            }
            if ("AllAccessDisabled".equals(e.getErrorCode())
                    || e.getStatusCode() == 403) {
                // Bucket exists but belongs to another account — use a different name
                throw new IllegalStateException(
                        "S3 bucket '" + bucketName + "' is owned by a different AWS account. " +
                        "Change aws.s3.bucket to a unique name.");
            }
            throw e;
        }
    }

    private void disableBlockPublicAccess() {
        try {
            SetPublicAccessBlockRequest req = new SetPublicAccessBlockRequest()
                    .withBucketName(bucketName)
                    .withPublicAccessBlockConfiguration(
                            new PublicAccessBlockConfiguration()
                                    .withBlockPublicAcls(false)
                                    .withIgnorePublicAcls(false)
                                    .withBlockPublicPolicy(false)
                                    .withRestrictPublicBuckets(false)
                    );
            amazonS3.setPublicAccessBlock(req);
            log.info("S3 Block Public Access disabled for: {}", bucketName);
        } catch (Exception e) {
            log.warn("Could not disable Block Public Access for '{}': {}", bucketName, e.getMessage());
        }
    }

    private void applyPublicReadPolicy() {
        try {
            String policy =
                    "{\"Version\":\"2012-10-17\"," +
                    "\"Statement\":[{" +
                    "\"Sid\":\"PublicReadGetObject\"," +
                    "\"Effect\":\"Allow\"," +
                    "\"Principal\":\"*\"," +
                    "\"Action\":\"s3:GetObject\"," +
                    "\"Resource\":\"arn:aws:s3:::" + bucketName + "/*\"" +
                    "}]}";
            amazonS3.setBucketPolicy(bucketName, policy);
            log.info("S3 public-read policy applied to: {}", bucketName);
        } catch (Exception e) {
            log.warn("Could not set bucket policy for '{}': {}", bucketName, e.getMessage());
        }
    }
}
