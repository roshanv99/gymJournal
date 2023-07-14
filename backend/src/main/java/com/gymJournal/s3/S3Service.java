package com.gymJournal.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class S3Service {
    private final S3Service s3Client;

    public S3Service(S3Client s3Client){
        this.s3Client = s3Client;
    }


}
