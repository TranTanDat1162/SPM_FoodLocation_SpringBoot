package vn.uef.g2.foodlocation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.repository.IRestaurantRepository;
import vn.uef.g2.foodlocation.service.imp.IMapService;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
@Transactional
@Service
@RequiredArgsConstructor
public class MapService implements IMapService{

    @Override
    public double getDistance(double x, double y, double xx, double yy) {
        double lat1Rad = Math.toRadians(x);
        double lat2Rad = Math.toRadians(xx);
        double lon1Rad = Math.toRadians(y);
        double lon2Rad = Math.toRadians(yy);

        double X = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double Y = (lat2Rad - lat1Rad);

        var temp = Math.sqrt(X * X + Y * Y) * 6371;
        return temp;
    }
}
