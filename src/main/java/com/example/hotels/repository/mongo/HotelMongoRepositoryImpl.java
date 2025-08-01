package com.example.hotels.repository.mongo;

import com.example.hotels.dto.HotelDetailDTO;
import com.example.hotels.dto.HotelSummaryDTO;
import com.example.hotels.mapper.HotelMongoMapper;
import com.example.hotels.model.mongo.HotelDocument;
import com.example.hotels.repository.base.IHotelBase;
import com.example.hotels.repository.base.IAmenityBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import org.bson.Document;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("mongodb")
public class HotelMongoRepositoryImpl implements IHotelBase {

    private final IMongoHotelRepository IMongoHotelRepository;
    private final MongoTemplate mongoTemplate;
    private final IAmenityBase amenityBase;
    private final MongoOperations mongoOperations;

    @Autowired
    public HotelMongoRepositoryImpl(IMongoHotelRepository IMongoHotelRepository,
                                    MongoTemplate mongoTemplate,
                                    IAmenityBase amenityBase) {
        this.IMongoHotelRepository = IMongoHotelRepository;
        this.mongoTemplate = mongoTemplate;
        this.amenityBase = amenityBase;
        this.mongoOperations = mongoTemplate;
    }

    private long getNextSequence(String seqName) {
        Document sequence = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Document.class,
                "counters"
        );

        if (sequence == null) {
            return 1L;
        }
        Object seqObj = sequence.get("seq");
        if (seqObj instanceof Number) {
            return ((Number) seqObj).longValue();
        } else {
            throw new IllegalStateException("Expected 'seq' to be a Number but was " + (seqObj == null ? "null" : seqObj.getClass()));
        }
    }

    @Override
    public HotelDetailDTO save(HotelDetailDTO dto) {
        HotelDocument doc = HotelMongoMapper.dtoToDocument(dto);

        if (dto.getId() == null) {
            long generatedId = getNextSequence("hotelId");
            doc.setLongId(generatedId);
        } else {
            doc.setLongId(dto.getId());
        }

        doc.setId(null);

        HotelDocument saved = IMongoHotelRepository.save(doc);
        return HotelMongoMapper.documentToDetailDTO(saved);
    }

    @Override
    public Optional<HotelDetailDTO> findById(Long id) {
        if (id == null) return Optional.empty();
        Optional<HotelDocument> docOpt = IMongoHotelRepository.findByLongId(id);
        return docOpt.map(HotelMongoMapper::documentToDetailDTO);
    }

    public Optional<HotelDetailDTO> findById(String id) {
        return IMongoHotelRepository.findById(id)
                .map(HotelMongoMapper::documentToDetailDTO);
    }

    @Override
    public List<HotelSummaryDTO> findAll() {
        List<HotelDocument> docs = IMongoHotelRepository.findAll();
        return docs.stream()
                .map(HotelMongoMapper::documentToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HotelSummaryDTO> search(String name, String brand, String city, String country, List<String> amenities) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            criteriaList.add(Criteria.where("name").regex(name, "i"));
        }
        if (brand != null && !brand.isBlank()) {
            criteriaList.add(Criteria.where("brand").regex(brand, "i"));
        }
        if (city != null && !city.isBlank()) {
            criteriaList.add(Criteria.where("address.city").regex(city, "i"));
        }
        if (country != null && !country.isBlank()) {
            criteriaList.add(Criteria.where("address.country").regex(country, "i"));
        }
        if (amenities != null && !amenities.isEmpty()) {
            criteriaList.add(Criteria.where("amenities").all(amenities));
        }

        MatchOperation match = criteriaList.isEmpty() ?
                Aggregation.match(new Criteria())
                : Aggregation.match(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));

        Aggregation aggregation = Aggregation.newAggregation(match);
        List<HotelDocument> docs = mongoTemplate.aggregate(aggregation, "hotels", HotelDocument.class).getMappedResults();

        return docs.stream()
                .map(HotelMongoMapper::documentToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addAmenitiesToHotel(Long hotelId, List<String> amenities) {
        Optional<HotelDocument> optDoc = IMongoHotelRepository.findByLongId(hotelId);
        if (optDoc.isEmpty()) return false;

        amenityBase.addAmenities(amenities);

        HotelDocument doc = optDoc.get();
        Set<String> currentAmenities = new HashSet<>(doc.getAmenities() != null ? doc.getAmenities() : new ArrayList<>());
        boolean added = false;
        for (String amenity : amenities) {
            if (!currentAmenities.contains(amenity)) {
                currentAmenities.add(amenity);
                added = true;
            }
        }
        if (added) {
            doc.setAmenities(new ArrayList<>(currentAmenities));
            IMongoHotelRepository.save(doc);
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        switch (param.toLowerCase()) {
            case "city":
                return countGroupByField("address.city");
            case "country":
                return countGroupByField("address.country");
            case "brand":
                return countGroupByBrand();
            case "amenities":
                return countGroupByAmenity();
            default:
                throw new IllegalArgumentException("Unsupported histogram parameter: " + param);
        }
    }

    private Map<String, Long> countGroupByField(String fieldName) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.unwind("$" + fieldName),
                Aggregation.group("$" + fieldName).count().as("count"),
                Aggregation.project("count").and("_id").as("key")
        );

        AggregationResults<DocumentCount> results = mongoTemplate.aggregate(agg, "hotels", DocumentCount.class);
        return results.getMappedResults()
                .stream()
                .collect(Collectors.toMap(DocumentCount::getKey, DocumentCount::getCount));
    }

    private Map<String, Long> countGroupByBrand() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group("brand").count().as("count"),
                Aggregation.project("count").and("_id").as("key")
        );
        AggregationResults<DocumentCount> results = mongoTemplate.aggregate(agg, "hotels", DocumentCount.class);
        return results.getMappedResults()
                .stream()
                .collect(Collectors.toMap(DocumentCount::getKey, DocumentCount::getCount));
    }

    private Map<String, Long> countGroupByAmenity() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.unwind("$amenities"),
                Aggregation.group("$amenities").count().as("count"),
                Aggregation.project("count").and("_id").as("key")
        );

        AggregationResults<DocumentCount> results = mongoTemplate.aggregate(agg, "hotels", DocumentCount.class);

        return results.getMappedResults()
                .stream()
                .collect(Collectors.toMap(DocumentCount::getKey, DocumentCount::getCount));
    }

    private static class DocumentCount {
        private String key;
        private long count;

        public String getKey() { return key; }
        public long getCount() { return count; }
    }
}