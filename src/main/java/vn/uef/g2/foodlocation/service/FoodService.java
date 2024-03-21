package vn.uef.g2.foodlocation.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import vn.uef.g2.foodlocation.domain.dto.FoodDto;
import vn.uef.g2.foodlocation.domain.entity.Food;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.repository.IFoodRepository;
import vn.uef.g2.foodlocation.repository.IRestaurantRepository;
import vn.uef.g2.foodlocation.utility.TitleToSlug;
import vn.uef.g2.foodlocation.utility.UploadFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class FoodService {
    private final IFoodRepository iFoodRepository;
    private final IRestaurantRepository iRestaurantRepository;
    public void add(@Valid FoodDto foodDto) throws FileUploadException {
        Food food = new Food();
        food.setFoodName(foodDto.getFoodName());
        food.setDescription(foodDto.getDescription());
        food.setPrice(foodDto.getPrice());
        food.setTimeWait(foodDto.getTimeWait());
        food.setImage(UploadFile.uploadFile(foodDto.getImageFile()));
        food.setSlug(createSlug(foodDto.getFoodName()));

        // Lấy thông tin nhà hàng từ repository
        Restaurant restaurant = iRestaurantRepository.findById(foodDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + foodDto.getRestaurantId()));
        food.setRestaurant(restaurant);

        food.setCreatedAt(LocalDateTime.now());

        iFoodRepository.save(food);
    }

    private String createSlug(String foodName) {
        while (true) {
            String slug = TitleToSlug.toSlug(foodName);
            if (iFoodRepository.findBySlug(slug)== null) {
                return slug;
            }
            foodName = foodName + "1";
        }
    }

    public void delete(Long foodId) {
        iFoodRepository.deleteById(foodId);
    }

    public void update(Long foodId, FoodDto foodDto) throws FileUploadException {
        Optional<Food> optionalFood = iFoodRepository.findById(foodId);
        if (optionalFood.isPresent()) {
            Food food = optionalFood.get();
            food.setFoodName(foodDto.getFoodName());
            food.setDescription(foodDto.getDescription());
            food.setPrice(foodDto.getPrice());
            food.setTimeWait(foodDto.getTimeWait());
            food.setSlug(createSlug(foodDto.getFoodName()));

            // Nếu có hình ảnh mới được cung cấp, cập nhật hình ảnh
            if (foodDto.getImageFile() != null && !foodDto.getImageFile().isEmpty()) {
                food.setImage(UploadFile.uploadFile(foodDto.getImageFile()));
            }

            // Lấy thông tin nhà hàng từ repository
            Restaurant restaurant = iRestaurantRepository.findById(foodDto.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + foodDto.getRestaurantId()));
            food.setRestaurant(restaurant);

            iFoodRepository.save(food);
        } else {
            throw new RuntimeException("Food not found with id: " + foodId);
        }
    }

    public List<Food> findAll() {
        return iFoodRepository.findAll();
    }

    public Optional<Food> findOne(Long foodId) {
        return iFoodRepository.findById(foodId);
    }

    public Restaurant findRestaurantFromFood(Long foodId) {
        Optional<Food> optionalFood = iFoodRepository.findById(foodId);
        if (optionalFood.isPresent()) {
            Food food = optionalFood.get();
            return food.getRestaurant();
        } else {
            throw new RuntimeException("Food not found with id: " + foodId);
        }
    }

    public void deleteFoodById(Long id) {
        Optional<Food> foodOptional = iFoodRepository.findById(id);
        if (foodOptional.isPresent()) {
            Food food = foodOptional.get();
            iFoodRepository.delete(food);
        }
    }

}
