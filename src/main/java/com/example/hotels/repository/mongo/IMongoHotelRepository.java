package com.example.hotels.repository.mongo;

import com.example.hotels.model.mongo.HotelDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMongoHotelRepository extends MongoRepository<HotelDocument, String> {
    Optional<HotelDocument> findByLongId(Long longId);
}