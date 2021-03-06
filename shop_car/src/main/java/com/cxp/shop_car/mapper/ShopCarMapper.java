package com.cxp.shop_car.mapper;

import com.cxp.shop_api.dto.AddShopCar;
import com.cxp.shop_api.entity.ShopCar;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCarMapper {

    //查用户购物车
    @Select("select shop_car_id, commodity_id,choose_number from shop_car where user_id=#{userId} order by shop_car_id desc")
    public List<ShopCar> selShopCarByUserId(@Param("userId") int userId);

    //查询用户购物车数量
    @Select("select sum(choose_number) from shop_car where user_id=#{userId} ")
    public Integer selShopCarNumberByUserId(@Param("userId") int userId);

    //更改该条购物车的购买数量 根据购物车id
    @Update("update shop_car set choose_number=#{chooseNumber} where shop_car_id=#{shopCarID}")
    public int addShopCarNumberByShopCarId(AddShopCar addShopCar);

    //更改该条购物车的购买数量 根据 用户id 和 商品id
    @Update("update shop_car set choose_number=choose_number+#{chooseNumber} where user_id=#{userId} and commodity_id=#{commodityId}")
    public int updShopCarNumberByCommodityId_UserId(AddShopCar addShopCar);

    //增加一条购物车
    @Insert("insert shop_car(user_id,commodity_id,choose_number) values(#{userId},#{commodityId},#{chooseNumber})")
    public int insShop_Car(AddShopCar addShopCar);

    //删除一条购物车 根据这条购物车id  userId
    public int delShopCarByShopCar(@Param("userId") int userId,@Param("list") List<ShopCar> shopCarList);

    //删除一条购物车 根据这条购物车id, userId
    public int delShopCarByShopCarId(@Param("userId") int userId,@Param("list") List<Integer> shopCarIdList);

}
