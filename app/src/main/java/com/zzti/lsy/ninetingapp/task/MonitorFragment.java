package com.zzti.lsy.ninetingapp.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.CarLocation;
import com.zzti.lsy.ninetingapp.entity.InfoWindowHolder;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.device.DeviceDetailActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/3 16:30
 * 监控
 */
public class MonitorFragment extends BaseFragment implements BaiduMap.OnMarkerClickListener {
    @BindView(R.id.mMapView)
    TextureMapView mMapView;
    @BindView(R.id.tv_hint)
    TextView tvHint;

    private boolean require;

    private BaiduMap mBaiduMap;
    private InfoWindow mInfoWindow;
    private LinearLayout baidumap_infowindow;
    private MarkerOnInfoWindowClickListener markerListener;
    private View v_temp;


    public static MonitorFragment newInstance() {
        MonitorFragment monitorFragment = new MonitorFragment();
        return monitorFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_monitor;
    }


    @Override
    protected void initView() {
        tvToolbarTitle.setText("监控");
        ivToolbarBack.setVisibility(View.GONE);
        // 1机械师理  2项目经理 3配件管理员 4设备管理员 5统计员 0总经理
        int opType = SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1);
        if (opType == 2 || opType == 4 || opType == 0) {
            require = true;
            mMapView.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
            mBaiduMap = mMapView.getMap();//获取地图实例
            // 开启定位图层，一定不要少了这句，否则对在地图的设置、绘制定位点将无效
            mBaiduMap.setMyLocationEnabled(true);
            mBaiduMap.setOnMarkerClickListener(this);
            //点击marker的pop的监听
            markerListener = new MarkerOnInfoWindowClickListener();
            //popwindowView
            baidumap_infowindow = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.baidumap_infowindow, null);
            //自定义Marker
            v_temp = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.text_up_img_down, null);
        } else {
            require = false;
            mMapView.setVisibility(View.GONE);
            tvHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        if (require) {
            showDia();
            getCarList();
        }
    }

    private void getCarList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("plateNumber", "");
        OkHttpManager.postFormBody(Urls.POST_GETCARLOCATION, params, tvHint, new OkHttpManager.OnResponse<String>() {
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
                            List<LatLng> points = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CarLocation carLocation = ParseUtils.parseJson(jsonArray.optString(i), CarLocation.class);
                                Double latitude = Double.parseDouble(carLocation.getLocation().split(",")[0]);//纬度
                                Double longitude = Double.parseDouble(carLocation.getLocation().split(",")[1]);//经度
                                //定义Maker坐标点
                                LatLng point = new LatLng(latitude, longitude);
                                points.add(point);
                                //构建Marker图标
                                TextView textView = v_temp.findViewById(R.id.baidumap_custom_text);
                                ImageView imageView = v_temp.findViewById(R.id.baidumap_custom_img);
                                textView.setText(carLocation.getPlatenumber());
                                imageView.setImageResource(R.mipmap.icon_car);
                                BitmapDescriptor bitmap = BitmapDescriptorFactory
                                        .fromView(v_temp);
                                OverlayOptions marker = new MarkerOptions()
                                        .icon(bitmap)
                                        .position(point);
                                Marker markOverlay = (Marker) mBaiduMap.addOverlay(marker);
                                Bundle bundle = new Bundle();
                                bundle.putString("plateNumber", carLocation.getPlatenumber());
                                bundle.putString("lastTime", carLocation.getLastTime().replace("T", " "));
                                markOverlay.setExtraInfo(bundle);
                            }
                            //移动到指定的位置
                            MapStatus.Builder builder = new MapStatus.Builder();
                            builder.target(points.get(0));
                            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                            //设置缩放级别 ， 缩放级别为4~21，21为最近，4为最远
                            MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(8.0f);
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

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //获得marker中的数据
        String plateNumber = (String) marker.getExtraInfo().get("plateNumber");
        String lastTime = (String) marker.getExtraInfo().get("lastTime");
        if (!StringUtil.isNullOrEmpty(plateNumber) && !StringUtil.isNullOrEmpty(lastTime)) {
            createInfoWindow(baidumap_infowindow, plateNumber, lastTime);
            //将marker所在的经纬度的信息转化成屏幕上的坐标
            final LatLng ll = marker.getPosition();
            mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(baidumap_infowindow), ll, -47, markerListener);
            //显示InfoWindow
            mBaiduMap.showInfoWindow(mInfoWindow);
        }
        return false;
    }


    private final class MarkerOnInfoWindowClickListener implements InfoWindow.OnInfoWindowClickListener {

        @Override
        public void onInfoWindowClick() {
            //隐藏InfoWindow
            mBaiduMap.hideInfoWindow();
        }

    }

    private void createInfoWindow(LinearLayout baidumap_infowindow, String plateNumber, String lastTime) {

        InfoWindowHolder holder;
        if (baidumap_infowindow.getTag() == null) {
            holder = new InfoWindowHolder();
            holder.tv_carNumber = baidumap_infowindow.findViewById(R.id.tv_carNumber);
            holder.tv_lastTime = baidumap_infowindow.findViewById(R.id.tv_lastTime);
            baidumap_infowindow.setTag(holder);
        }
        holder = (InfoWindowHolder) baidumap_infowindow.getTag();
        holder.tv_carNumber.setText(plateNumber);
        holder.tv_lastTime.setText(lastTime);
    }

}
