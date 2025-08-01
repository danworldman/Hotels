package com.example.hotels.repository.sql;

import com.example.hotels.dto.HotelDetailDTO;
import com.example.hotels.dto.HotelSummaryDTO;
import com.example.hotels.mapper.HotelSqlMapper;
import com.example.hotels.model.sql.Amenity;
import com.example.hotels.model.sql.Hotel;
import com.example.hotels.repository.base.IAmenityBase;
import com.example.hotels.repository.base.IHotelBase;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile({"h2", "mysql", "postgresql"})
public class HotelSqlRepositoryImpl implements IHotelBase {

    private final IHotelJpaRepository IHotelJpaRepository;
    private final IAmenityBase amenityBase;
    private final IAmenityRepository IAmenityRepository;

    @Autowired
    public HotelSqlRepositoryImpl(IHotelJpaRepository IHotelJpaRepository,
                                  IAmenityBase amenityBase,
                                  IAmenityRepository IAmenityRepository) {
        this.IHotelJpaRepository = IHotelJpaRepository;
        this.amenityBase = amenityBase;
        this.IAmenityRepository = IAmenityRepository;
    }

    @Override
    public HotelDetailDTO save(HotelDetailDTO dto) {
        Hotel entity = HotelSqlMapper.toEntity(dto);

        if (entity.getId() == null) {
            Long maxId = IHotelJpaRepository.findMaxId();
            Long nextId = (maxId == null) ? 1L : maxId + 1;
            // Используйте nextId, если нужно явно задать ID, или доверяйте автоинкременту
        }

        entity = IHotelJpaRepository.save(entity);
        return HotelSqlMapper.toDetailDTO(entity);
    }

    @Override
    public Optional<HotelDetailDTO> findById(Long id) {
        return IHotelJpaRepository.findById(id).map(HotelSqlMapper::toDetailDTO);
    }

    @Override
    public List<HotelSummaryDTO> findAll() {
        List<Hotel> entities = IHotelJpaRepository.findAll();
        return entities.stream()
                .map(HotelSqlMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HotelSummaryDTO> search(String name, String brand, String city, String country, List<String> amenities) {
        Specification<Hotel> spec = Specification.where(null);

        if (name != null && !name.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (brand != null && !brand.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%"));
        }
        if (city != null && !city.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("address").get("city")), "%" + city.toLowerCase() + "%"));
        }
        if (country != null && !country.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("address").get("country")), "%" + country.toLowerCase() + "%"));
        }
        if (amenities != null && !amenities.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                Join<Hotel, Amenity> amenityJoin = root.join("amenities", JoinType.INNER);
                return amenityJoin.get("name").in(amenities);
            });
        }

        List<Hotel> hotels = IHotelJpaRepository.findAll(spec);
        return hotels.stream()
                .map(HotelSqlMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addAmenitiesToHotel(Long hotelId, List<String> amenities) {
        Optional<Hotel> optHotel = IHotelJpaRepository.findById(hotelId);
        if (optHotel.isEmpty()) {
            return false;
        }
        Hotel hotel = optHotel.get();

        Set<Amenity> amenitiesToAdd = new HashSet<>();

        for (String name : amenities) {
            Amenity amenity = IAmenityRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> {
                        Amenity newAmenity = new Amenity();
                        newAmenity.setName(name);
                        return IAmenityRepository.save(newAmenity); // сохранить и получить объект с ID
                    });
            amenitiesToAdd.add(amenity);
        }

        if (hotel.getAmenities() == null) {
            hotel.setAmenities(new HashSet<>());
        }

        Set<String> existingNames = hotel.getAmenities().stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet());

        boolean changed = false;
        for (Amenity amenity : amenitiesToAdd) {
            if (!existingNames.contains(amenity.getName())) {
                hotel.addAmenity(amenity); // обновляем bidirectional связь
                changed = true;
            }
        }

        if (changed) {
            IHotelJpaRepository.save(hotel);
        }

        return true;
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        switch (param.toLowerCase()) {
            case "city":
                return listToMap(IHotelJpaRepository.countGroupByCity(), 0, 1);
            case "country":
                return listToMap(IHotelJpaRepository.countGroupByCountry(), 0, 1);
            case "brand":
                return listToMap(IHotelJpaRepository.countGroupByBrand(), 0, 1);
            case "amenities":
                return listToMap(amenityBase.countGroupByAmenity(), 0, 1);
            default:
                throw new IllegalArgumentException("Unsupported histogram parameter: " + param);
        }
    }

    private Map<String, Long> listToMap(List<Object[]> data, int keyIndex, int valueIndex) {
        Map<String, Long> result = new HashMap<>();
        for (Object[] row : data) {
            String key = (String) row[keyIndex];
            Long value = row[valueIndex] == null ? 0L : ((Number) row[valueIndex]).longValue();
            result.put(key, value);
        }
        return result;
    }
}
