package vn.uef.g2.foodlocation.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import vn.uef.g2.foodlocation.domain.dto.RestaurantDto;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.repository.IRestaurantRepository;
import vn.uef.g2.foodlocation.service.imp.IMapService;
import vn.uef.g2.foodlocation.utility.TitleToSlug;
import vn.uef.g2.foodlocation.utility.UploadFile;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    private final IRestaurantRepository restaurantRepository;
    private final IMapService mapService;

    public void add(@Valid RestaurantDto r) throws FileUploadException {

        Optional<Restaurant> existingRestaurant = restaurantRepository.findByRestaurantName(r.getRestaurantName());
        if (existingRestaurant.isPresent()) {
            // Nếu tồn tại, bạn có thể thực hiện xử lý khi có lỗi, ví dụ:
            throw new DuplicateRestaurantNameException("Nhà hàng với tên đã tồn tại");
        }

        // Nếu không có lỗi, tiếp tục thêm nhà hàng mới
        Restaurant res = new Restaurant();
        res.setPlaceId(r.getPlaceId());
        res.setRestaurantName(r.getRestaurantName());
        res.setTotalRating(r.getTotalRating());
        res.setAddress(r.getAddress());
        res.setLatitude(r.getLatitude());
        res.setLongitude(r.getLongitude());
        res.setDescription(r.getDescription());
        res.setSlug(createSlug(r.getRestaurantName()));
        res.setOpenTime(LocalTime.parse(r.getOpenTime()));
        res.setCloseTime(LocalTime.parse(r.getCloseTime()));

        restaurantRepository.save(res);
    }


    private String createSlug(String restaurantName) {
        while (true) {
            String slug = TitleToSlug.toSlug(restaurantName);
            if (restaurantRepository.findBySlug(slug)== null) {
                return slug;
            }
            restaurantName = restaurantName + "1";
        }
    }

    public void update(Long restaurantId, RestaurantDto updatedRestaurantForm) throws FileUploadException {
        // Kiểm tra xem nhà hàng có tồn tại hay không
        Optional<Restaurant> existingRestaurantOptional = restaurantRepository.findById(restaurantId);
        if (existingRestaurantOptional.isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId);
        }

        Restaurant existingRestaurant = existingRestaurantOptional.get();


        // Cập nhật thông tin của nhà hàng
        existingRestaurant.setRestaurantName(updatedRestaurantForm.getRestaurantName());
        existingRestaurant.setAddress(updatedRestaurantForm.getAddress());
        existingRestaurant.setLongitude(updatedRestaurantForm.getLongitude());
        existingRestaurant.setLatitude(updatedRestaurantForm.getLatitude());
        existingRestaurant.setDescription(updatedRestaurantForm.getDescription());
        existingRestaurant.setSlug(createSlug(updatedRestaurantForm.getRestaurantName()));
        existingRestaurant.setOpenTime(LocalTime.parse(updatedRestaurantForm.getOpenTime()));
        existingRestaurant.setCloseTime(LocalTime.parse(updatedRestaurantForm.getCloseTime()));

        // Lưu những thay đổi vào cơ sở dữ liệu
        restaurantRepository.save(existingRestaurant);
    }

    public List<Restaurant> findRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> findOne(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    public static class DuplicateRestaurantNameException extends RuntimeException {
        public DuplicateRestaurantNameException(String message) {
            super(message);
        }
    }

    public static class RestaurantNotFoundException extends RuntimeException {
        public RestaurantNotFoundException(String message) {
            super(message);
        }
    }
    public Optional<Restaurant> findBySlug (String slug) {
        return Optional.ofNullable(restaurantRepository.findBySlug(slug));
    }

    public void deleteRestaurantById(Long id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurantRepository.delete(restaurant);
        }
    }

    public List<Restaurant> findMatchingRestaurant(String name) throws RestaurantNotFoundException{
        return restaurantRepository.findByRestaurantNameContaining(name);
    }
    // get all restaurants by food name
    public Set<Restaurant> findAllRestaurantsWithFoodName(String foodName) {
        return restaurantRepository.FindAllWithFoodNameQuery(foodName);
    }

    public String findSlugById(Long restaurantId) {
        return restaurantRepository.findSlugById(restaurantId);
    }

    public List<Restaurant> restaurantsWithinRadius(double x, double y,int r) {

        // Conversion from Google's zoom level to meters
        // it's annoying i know
        Map<Integer, Double> valueMap = new HashMap<>();
        valueMap.put(12, 10.0);
        valueMap.put(14, 5.0);
        valueMap.put(15, 2.5);
        valueMap.put(16, 1.2);
        valueMap.put(17, 0.6);
        valueMap.put(18, 0.3);

        List<Restaurant> nearbyRestarants;

        // Hahaha I learned LinQ when LeetCoding
        // and look what I found similar >:)
        // From now on I'm going to abuse this
        nearbyRestarants = restaurantRepository
                .findAll()
                .stream()
                .filter(res ->
                    mapService.getDistance(x,y,
                        Double.valueOf(res.getLatitude()),
                        Double.valueOf(res.getLongitude())
                    ) < valueMap.get(r))
                .collect(toList());

        return nearbyRestarants;
    }

    public List<Restaurant> restaurantsInRegion(String region) {
        List<Restaurant> regionalRestaurants;

        regionalRestaurants = restaurantRepository.findAll().stream()
                .filter( r ->getDistrict(r.getAddress()).equalsIgnoreCase(region))
                .collect(toList());

        return regionalRestaurants;
    }

    public String getDistrict(String address) {
        List<String> districtNames = Arrays.asList(
                "Quận 1", "Quận 3", "Quận 4", "Quận 5", "Quận 6", "Quận 7",
                "Quận 8", "Quận 10", "Quận 11", "Quận 12", "Tân Bình",
                "Bình Tân", "Bình Thạnh", "Tân Phú", "Gò Vấp",
                "Phú Nhuận"
        );

        String lowerCaseAddress = address.toLowerCase();
        for (String districtName : districtNames) {
            if (lowerCaseAddress.contains(districtName.toLowerCase())) {
                return districtName;
            }
        }
        return null;
    }

    public void addToFavorite(Long id){

    }
}