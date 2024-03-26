package vn.uef.g2.foodlocation.service.imp;

import vn.uef.g2.foodlocation.domain.entity.Restaurant;

import java.util.List;

public interface IMapService {
    double getDistance(double x,double y, double xx, double yy);
}
