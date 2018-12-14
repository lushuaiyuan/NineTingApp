package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.CarLocation;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 车辆位置地图
 */

public class MapActivity extends BaseActivity {
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_last_updateTime)
    TextView tvUpdateTime;
    @BindView(R.id.bmapView)
    MapView mMapView;
    BaiduMap mBaiduMap;

    @Override
    public int getContentViewId() {
        return R.layout.activity_map;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        String plateNumber = UIUtils.getStr4Intent(this, "plateNumber");
        tvCarNumber.setText(plateNumber);
        tvProjectAddress.setText(UIUtils.getStr4Intent(this, "project"));
        String status = UIUtils.getStr4Intent(this, "status");
        if (TextUtils.equals(status, "0")) {
            tvState.setTextColor(App.get().getResources().getColor(R.color.color_6bcfd6));
            tvState.setText("存放中");
        } else if (TextUtils.equals(status, "1")) {
            tvState.setTextColor(App.get().getResources().getColor(R.color.color_ffb947));
            tvState.setText("工作中");
        } else if (TextUtils.equals(status, "2")) {
            tvState.setTextColor(App.get().getResources().getColor(R.color.color_fe81b3));
            tvState.setText("维修中");
        }
        showDia();
        getLocation(plateNumber);
    }

    private void getLocation(String plateNumber) {
        HashMap<String, String> params = new HashMap<>();
        params.put("plateNumber", plateNumber);
        OkHttpManager.postFormBody(Urls.POST_GETCARLOCATION, params, tvCarNumber, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData().toString());
                        if (jsonArray.length() > 0) {
                            CarLocation carLocation = ParseUtils.parseJson(jsonArray.optString(0), CarLocation.class);
                            tvUpdateTime.setText(carLocation.getLastTime().replace("T", " "));
                            Double latitude = Double.parseDouble(carLocation.getLocation().split(",")[0]);//纬度
                            Double longitude = Double.parseDouble(carLocation.getLocation().split(",")[1]);//经度
                            //移动到指定位置
                            //定义Maker坐标点
                            LatLng point = new LatLng(latitude, longitude);
                            //构建Marker图标
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.icon_marka);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap);
                            //在地图上添加Marker，并显示
                            mBaiduMap.addOverlay(option);

                            MapStatus.Builder builder = new MapStatus.Builder();
                            builder.target(point);
                            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                            //设置缩放级别 ， 缩放级别为4~21，21为最近，4为最远
                            MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
                            mBaiduMap.setMapStatus(msu);
                        } else {
                            mMapView.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }

            }
        });
    }

    private void initView() {
        setTitle("车辆位置");
        mBaiduMap = mMapView.getMap();//获取地图实例
        // 开启定位图层，一定不要少了这句，否则对在地图的设置、绘制定位点将无效
        mBaiduMap.setMyLocationEnabled(true);
        // 隐藏百度的LOGO
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        // 不显示地图上比例尺
        mMapView.showScaleControl(false);
        // 不显示地图缩放控件（按钮控制栏）
        mMapView.showZoomControls(false);

    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
