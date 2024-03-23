package vn.uef.g2.foodlocation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.uef.g2.foodlocation.domain.dto.RatingRestaurantDto;
import vn.uef.g2.foodlocation.domain.entity.RatingRestaurant;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.domain.entity.User;
import vn.uef.g2.foodlocation.repository.IRatingRestaurantRepository;
import vn.uef.g2.foodlocation.repository.IRestaurantRepository;
import vn.uef.g2.foodlocation.service.imp.IUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingRestaurantService {
    private final IRatingRestaurantRepository iRatingRestaurantRepository;
    private final IRestaurantRepository iRestaurantRepository;
    private final IUserService iUserService;

    public void rateRestaurant(String content, Long restaurantId) {
        // Retrieve Entities
        String username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            username = "anonymousUser";
        }
        else
            username = authentication.getName();

        User user = iUserService.findByEmail(username);
        Restaurant restaurant = iRestaurantRepository.findRestaurantById(restaurantId);

        // Create Rating
        RatingRestaurant rating = new RatingRestaurant();
        rating.setContent(content);
        rating.setCreatedAt(LocalDateTime.now());
        rating.setUser(user);
        rating.setRestaurant(restaurant);

        // Save Rating
        iRatingRestaurantRepository.save(rating);
    }

    public List<RatingRestaurantDto> findAll() {
        List<RatingRestaurant> ratingRestaurantList = iRatingRestaurantRepository.findAll();
        return getRatingResponseList(ratingRestaurantList);
    }

    public List<RatingRestaurantDto> getRatingsByRestaurantId(Long restaurantId) {
        List<RatingRestaurant> ratingRestaurantList = iRatingRestaurantRepository.findByRestaurantId(restaurantId);
        return getRatingResponseList(ratingRestaurantList);
    }

    /*
     * transform rating response DTO
     */
    private List<RatingRestaurantDto> getRatingResponseList(List<RatingRestaurant> ratingRestaurantList) {
        List<RatingRestaurantDto> ratingResponseList = new ArrayList<>();

        //    transform data DTO
        for (RatingRestaurant ratingRestaurant : ratingRestaurantList) {
            RatingRestaurantDto ratingRestaurantDto = new RatingRestaurantDto();

            ratingRestaurantDto.setId(ratingRestaurant.getId());
            ratingRestaurantDto.setContent(ratingRestaurant.getContent());
            ratingRestaurantDto.setCreatedAt(ratingRestaurant.getCreatedAt());
            ratingRestaurantDto.setRestaurantName(ratingRestaurant.getRestaurant().getRestaurantName());
            ratingRestaurantDto.setUserFullName(ratingRestaurant.getUser().getFullName());
            ratingRestaurantDto.setUserAvatar(ratingRestaurant.getUser().getAvatar());

            ratingResponseList.add(ratingRestaurantDto);
        }
        return ratingResponseList;
    }

    public void deleteRating(Long ratingId) {
        iRatingRestaurantRepository.deleteById(ratingId);
    }
}
