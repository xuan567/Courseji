package com.littlecorgi.middle.logic.dao;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.littlecorgi.middle.R;

/**
 * 百度地图Service
 */
public class BaiDuMapService {

    private BaiduMap mBaiDuMap = null;

    /**
     * 构造方法
     *
     * @param baiduMap map对象
     */
    public BaiDuMapService(BaiduMap baiduMap) {
        if (mBaiDuMap == null) {
            mBaiDuMap = baiduMap;
            setLocationEnabled();
        }
    }

    public BaiduMap getBaiDuMap() {
        return mBaiDuMap;
    }

    public void setLocationEnabled() {
        mBaiDuMap.setMyLocationEnabled(true);
    }

    public void setUNLocationEnabled() {
        mBaiDuMap.setMyLocationEnabled(false);
    }

    /**
     * 设置中心点
     */
    public void setMapStatus(double lat, double ing) {
        LatLng cenpt = new LatLng(lat, ing); // 设定中心点坐标
        MapStatus mapStatus =
                new MapStatus.Builder() // 定义地图状态
                        .target(cenpt)
                        .zoom(19)
                        .build(); // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiDuMap.setMapStatus(mapStatusUpdate); // 改变地图状态
    }

    /**
     * 添加Marker图标
     *
     * @param latLng 定位点
     */
    public void addMarker(LatLng latLng) {
        // 构建Marker图标
        mBaiDuMap.clear();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap);
        // 在地图上添加Marker，并显示
        mBaiDuMap.addOverlay(option);
    }

    /**
     * 设置定位数据
     *
     * @param location 定位信息
     */
    public void setLocationData(BDLocation location) {
        MyLocationData locData =
                new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
        mBaiDuMap.setMyLocationData(locData);
    }

    /**
     * 设置Circle
     *
     * @param center 圆形中心点
     */
    public void setCircle(LatLng center, int radius) {
        // 构造CircleOptions对象
        CircleOptions circleOptions =
                new CircleOptions()
                        .center(center)
                        .radius(radius)
                        .fillColor(0xAA0000FF) // 填充颜色
                        .stroke(new Stroke(5, 0xAA00ff00)); // 边框宽和边框颜色
        mBaiDuMap.addOverlay(circleOptions);
    }
}
