package vn.uef.g2.foodlocation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.uef.g2.foodlocation.domain.dto.RatingFoodDto;
import vn.uef.g2.foodlocation.domain.entity.Food;
import vn.uef.g2.foodlocation.domain.entity.RatingFood;
import vn.uef.g2.foodlocation.domain.entity.User;
import vn.uef.g2.foodlocation.repository.IFoodRepository;
import vn.uef.g2.foodlocation.repository.IRatingFoodRepository;
import vn.uef.g2.foodlocation.service.imp.IUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingFoodService {
    private final IRatingFoodRepository iRatingFoodRepository;
    private final IUserService userService;
    private final IFoodRepository iFoodRepository;

    @Transactional
    public void rateFood(String content, int rateStar, long foodId) {
        // Retrieve Entities
        String username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            username = "anonymousUser";
        }
        else
            username = authentication.getName();
        User user = userService.findByEmail(username);
        Food food = iFoodRepository.findFoodById(foodId);

        // Create Rating
        RatingFood rating = new RatingFood();
        rating.setContent(content);
        rating.setRateStar(rateStar);
        rating.setCreatedAt(LocalDateTime.now());
        rating.setUser(user);
        rating.setFood(food);

        // Save Rating
        iRatingFoodRepository.save(rating);
    }

    public List<RatingFoodDto> findAll() {
        List<RatingFood> ratingFoodList = iRatingFoodRepository.findAll();
        return getRatingResponseList(ratingFoodList);
    }

    public List<RatingFoodDto> getRatingsByFoodId(Long foodId) {
        List<RatingFood> ratingFoodList = iRatingFoodRepository.findByFoodId(foodId);
        return getRatingResponseList(ratingFoodList);
    }

    private List<RatingFoodDto> getRatingResponseList(List<RatingFood> ratingFoodList) {
        List<RatingFoodDto> ratingResponseList = new ArrayList<>();
        for (RatingFood ratingFood : ratingFoodList) {
            RatingFoodDto ratingFoodDto = new RatingFoodDto();

            ratingFoodDto.setId(ratingFood.getId());
            ratingFoodDto.setContent(ratingFood.getContent());
            ratingFoodDto.setRateStar(ratingFood.getRateStar());
            ratingFoodDto.setCreatedAt(ratingFood.getCreatedAt());
            ratingFoodDto.setFoodName(ratingFood.getFood().getFoodName());
            ratingFoodDto.setUserFullName(ratingFood.getUser().getFullName());
            ratingFoodDto.setUserAvatar(ratingFood.getUser().getAvatar());
            ratingResponseList.add(ratingFoodDto);
        }
        return ratingResponseList;
    }

    public Double calculateAverageRatingByFoodId(Long foodId) {
        return iRatingFoodRepository.calculateAverageRatingByFoodId(foodId);
    }

    public long countRating(Long foodId) {
        return iRatingFoodRepository.countByFoodId(foodId);
    }

    public void deleteRating(Long ratingId) {
        iRatingFoodRepository.deleteById(ratingId);
    }

}
