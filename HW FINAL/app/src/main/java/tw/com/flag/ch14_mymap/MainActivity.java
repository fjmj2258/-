package tw.com.flag.ch14_mymap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity
        implements LocationListener {

    static final int MIN_TIME = 5000;// 位置更新條件：5000 毫秒
    static final float MIN_DIST = 5; // 位置更新條件：5 公尺
    LocationManager mgr;        // 定位總管
    TextView txv;//座標資訊
    TextView txv2;//顯示餐廳名
    GoogleMap map;  //操控地圖的物件
    Spinner meals;//餐點選單
    LatLng Point;//餐廳座標
    int ret ;//判定選擇餐點
    int res;//抽選餐廳
    int res2=0;//餐廳標號

    static final String db_name="testDB";//資料庫
    static final String tb_name="test";//資料表
    SQLiteDatabase db;//資料庫db

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txv = (TextView) findViewById(R.id.txv);
        mgr = (LocationManager)getSystemService(LOCATION_SERVICE);

        setUpMapIfNeeded();
        txv2 = (TextView)findViewById(R.id.textView);
        meals = (Spinner)findViewById(R.id.spinner);
//__________________________________________________________________________________________________________________資料庫內容
        db =openOrCreateDatabase(db_name,Context.MODE_PRIVATE, null);
        String createTable="CREATE TABLE IF NOT EXISTS "+tb_name+"(name VARCHAR(32),"+"meal VARCHAR(16) )";
        db.execSQL(createTable);
        addData("麥當勞", "早餐午餐晚餐宵夜");
        addData("摩斯漢堡","早餐午餐晚餐宵夜");
        addData("銘記港式燒臘","午餐晚餐");
        addData("派克雞排","宵夜");
        addData("永玉小吃店","午餐晚餐");
        addData("非常烏龍","午餐晚餐");
        addData("呷尚寶","早餐");
        addData("加熱滷味","晚餐宵夜");
        addData("蘭陽派","宵夜");
        addData("火車飯包","午餐晚餐");
        addData("阿國炒羊肉","午餐晚餐宵夜");
        addData("雅加達早餐店","早餐");
        addData("鐵板便當","午餐晚餐");
        addData("台南意麵","午餐晚餐宵夜");
        addData("慈善便當","午餐晚餐");
        addData("小田園湯包","午餐晚餐");
        addData("晨華早餐店","早餐");
        addData("蜜汁燒烤","宵夜");
        addData("華美自助餐","午餐晚餐");
        addData("五路鍋聖","午餐晚餐");
        addData("8鍋臭臭鍋","午餐晚餐");
        addData("淡江炒飯","午餐晚餐");
        addData("宵夜快餐","午餐晚餐宵夜");
        addData("敦煌快餐","午餐晚餐");
        addData("池玖便當","午餐晚餐");
        addData("大陸麵店","午餐晚餐");
        addData("牛肉拌麵","午餐晚餐");
        addData("華榮早餐店","早餐");
        addData("媽媽樂早餐店","早餐");
        addData("山東餃子館","午餐晚餐");
        addData("大Q麵店","午餐晚餐");
        addData("大紅袍","午餐晚餐");
        addData("阿羅哈早餐店","早餐");
        addData("大家樂","早餐");
        addData("Dr. Chicky","宵夜");
        addData("石鍋拌飯","午餐晚餐");
        addData("台北城","午餐晚餐");
        addData("藥膳排骨","午餐晚餐");
        addData("魷魚羹","午餐晚餐");
        addData("老地方炒飯","午餐晚餐");
        addData("感恩麵店","午餐晚餐");
        addData("鹽水雞","晚餐宵夜");
        addData("輔大豬排", "午餐晚餐");
//__________________________________________________________________________________________________________________資料庫內容


    }
    public void Inquire(View v) {//顯示餐廳資訊
        String str="";//字串Str

        Cursor c=db.rawQuery("SELECT * FROM "+tb_name,null);//讀取資料庫
        c.moveToFirst();//至第一項資料
        for(int i=res2;i>1;i--)//讀資料
            c.moveToNext();
        str+= c.getString(0)+" "+ c.getString(1) ;//輸出資料

        txv2.setText(str);
    }
    private void addData (String name,String meal){//資料庫增加
        ContentValues cv=new ContentValues(2);
        cv.put("name", name);
        cv.put("meal",meal);

        db.insert(tb_name,null,cv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        // 取得最佳的定位提供者
        String best = mgr.getBestProvider(new Criteria(), true);
        if (best != null) { // 取得快取的最後位置,如果有的話
            txv.setText("取得定位資訊中...");
            mgr.requestLocationUpdates(best, MIN_TIME, MIN_DIST, this);	// 註冊位置事件監聽器
        }
        else { // 無提供者, 顯示提示訊息
            txv.setText("請確認已開啟定位功能!");
        }
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();                     //取得 Google Map 物件
            if (map != null) {
                map.setMyLocationEnabled(true);    // 顯示『我的位置』圖示按鈕
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);    // 設定地圖為普通街道模式
                map.moveCamera(CameraUpdateFactory.zoomTo(18));    // 將地圖縮放級數改為 18
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mgr.removeUpdates(this);	// 停止監聽位置事件
    }

    @Override
    public void onLocationChanged(Location location) { // 位置變更事件
        if(location != null) { // 如果可以取得座標
            txv.setText(String.format("緯度 %.4f, 經度 %.4f (%s 定位 )",
                    location.getLatitude(),  // 目前緯度
                    location.getLongitude(), // 目前經度
                    location.getProvider()));// 定位方式

            // 將地圖中心點移到目前的經緯度
            map.animateCamera(CameraUpdateFactory.newLatLng(
                    new LatLng(location.getLatitude(),
                            location.getLongitude())));
        }
        else { // 無法取得座標
            txv.setText("暫時無法取得定位資訊...");
        }
    }

    @Override
    public void onProviderDisabled(String provider) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }





    public void Navigation(View v){//標記
        map.clear();//地圖標示清空
//______________________________________________________________________________________________________________餐廳標號
        if(res2==1)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137758, 121.546636)).title("麥當勞"));
        if(res2==2)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137960, 121.546415)).title("摩斯漢堡"));
        if(res2==3)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137709, 121.546430)).title("銘記港式燒臘"));
        if(res2==4)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137602, 121.546456)).title("派克雞排"));
        if(res2==5)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137523, 121.546545)).title("永玉小吃店"));
        if(res2==6)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137367, 121.546527)).title("非常烏龍"));
        if(res2==7)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137304, 121.546499)).title("呷尚寶"));
        if(res2==8)
            map.addMarker(new MarkerOptions().position(new LatLng(25.136991, 121.546489)).title("加熱滷味"));
        if(res2==9)
            map.addMarker(new MarkerOptions().position(new LatLng(25.136763, 121.546453)).title("蘭陽派"));
        if(res2==10)
            map.addMarker(new MarkerOptions().position(new LatLng(25.136523, 121.546435)).title("火車飯包"));
        if(res2==11)
            map.addMarker(new MarkerOptions().position(new LatLng(25.135984, 121.546056)).title("阿國炒羊肉"));
        if(res2==12)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137897, 121.542306)).title("雅加達早餐店"));
        if(res2==13)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137861, 121.542158)).title("鐵板便當"));
        if(res2==14)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137758, 121.541762)).title("台南意麵"));
        if(res2==15)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137828, 121.541703)).title("慈善便當"));
        if(res2==16)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137735, 121.541649)).title("小田園湯包"));
        if(res2==17)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137822, 121.541637)).title("晨華早餐店"));
        if(res2==18)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137768, 121.541513)).title("蜜汁燒烤"));
        if(res2==19)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137667, 121.541436)).title("華美自助餐"));
        if(res2==20)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137681, 121.541246)).title("五路鍋聖"));
        if(res2==21)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137548, 121.541281)).title("8鍋臭臭鍋"));
        if(res2==22)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137747, 121.541201)).title("淡江炒飯"));
        if(res2==23)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137719, 121.541040)).title("宵夜快餐"));
        if(res2==24)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137748, 121.541139)).title("敦煌快餐"));
        if(res2==25)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137346, 121.541333)).title("池玖便當"));
        if(res2==26)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137322, 121.541344)).title("大陸麵店"));
        if(res2==27)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137299, 121.541351)).title("牛肉拌麵"));
        if(res2==28)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137104, 121.541446)).title("華榮早餐店"));
        if(res2==29)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137158, 121.541562)).title("媽媽樂早餐店"));
        if(res2==30)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137214, 121.541482)).title("山東餃子館"));
        if(res2==31)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137187, 121.541658)).title("大Q麵店"));
        if(res2==32)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137211, 121.541728)).title("大紅袍"));
        if(res2==33)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137351, 121.541691)).title("阿囉哈早餐店"));
        if(res2==34)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137230, 121.541808)).title("大家樂早餐店"));
        if(res2==35)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137289, 121.541894)).title("Dr Chicky"));
        if(res2==36)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137321, 121.542032)).title("石鍋拌飯"));
        if(res2==37)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137388, 121.542179)).title("台北城"));
        if(res2==38)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137399, 121.542205)).title("藥膳排骨"));
        if(res2==39)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137408, 121.542235)).title("魷魚羹"));
        if(res2==40)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137454, 121.542117)).title("老地方炒飯"));
        if(res2==41)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137529, 121.542032)).title("感恩麵店"));
        if(res2==42)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137638, 121.541825)).title("鹽水雞"));
        if(res2==43)
            map.addMarker(new MarkerOptions().position(new LatLng(25.137264, 121.542330)).title("輔大豬排"));
//______________________________________________________________________________________________________________餐廳標號
    }



    public void Lottery(View v){
        map.moveCamera(CameraUpdateFactory.zoomTo(18));//地圖大小18
        ret =(meals).getSelectedItemPosition();//讀取選擇
//______________________________________________________________________________________________________________早餐
        if(ret==0){ //選擇早餐
            res= (int)(Math.random()*9+1);//抽選
            if(res==1) {
                txv2.setText("麥當勞");
                Point = new LatLng(25.137758, 121.546636);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=1;
            }
            if(res==2) {
                txv2.setText("摩斯漢堡");
                Point = new LatLng(25.137960, 121.546415);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=2;
            }
            if(res==3) {
                txv2.setText("呷尚寶");
                Point = new LatLng(25.137304, 121.546499);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=7;
            }
            if(res==4) {
                txv2.setText("雅加達早餐店");
                Point = new LatLng(25.137897, 121.542306);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=12;
            }
            if(res==5) {
                txv2.setText("晨華早餐店");
                Point = new LatLng(25.137822, 121.541637);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=17;
            }
            if(res==6) {
                txv2.setText("華榮早餐店");
                Point = new LatLng(25.137104, 121.541446);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=28;
            }
            if(res==7) {
                txv2.setText("媽媽樂早餐店");
                Point = new LatLng(25.137158, 121.541562);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=29;
            }
            if(res==8) {
                txv2.setText("阿囉哈早餐店");
                Point = new LatLng(25.137351, 121.541691);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=33;
            }
            if(res==9) {
                txv2.setText("大家樂早餐店");
                Point = new LatLng(25.137230, 121.541808);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=34;
            }

        }
//______________________________________________________________________________________________________________早餐

//______________________________________________________________________________________________________________午餐
        if(ret==1){//選擇午餐

            res= (int)(Math.random()*31+1);//抽選
            if(res==1) {
                txv2.setText("麥當勞");
                Point = new LatLng(25.137758, 121.546636);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=1;
            }
            if(res==2) {
                txv2.setText("摩斯漢堡");
                Point = new LatLng(25.137960, 121.546415);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=2;
            }
            if(res==3) {
                txv2.setText("銘記港式燒臘");
                Point = new LatLng(25.137709, 121.546430);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=3;
            }
            if(res==4) {
                txv2.setText("永玉小吃店");
                Point = new LatLng(25.137523, 121.546545);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=5;
            }
            if(res==5) {
                txv2.setText("非常烏龍");
                Point = new LatLng(25.137367, 121.546527);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=6;
            }
            if(res==6) {
                txv2.setText("加熱滷味");
                Point = new LatLng(25.136991, 121.546481);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=8;
            }
            if(res==7) {
                txv2.setText("火車飯包");
                Point = new LatLng(25.136523, 121.546435);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=10;
            }
            if(res==8) {
                txv2.setText("阿國炒羊肉");
                Point = new LatLng(25.135995, 121.546061);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=11;
            }
            if(res==9) {
                txv2.setText("鐵板便當");
                Point = new LatLng(25.137861, 121.542158);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=13;
            }
            if(res==10) {
                txv2.setText("台南意麵");
                Point = new LatLng(25.137764, 121.541763);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=14;
            }
            if(res==11) {
                txv2.setText("慈善便當");
                Point = new LatLng(25.137828, 121.541703);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=15;
            }
            if(res==12) {
                txv2.setText("小田園湯包");
                Point = new LatLng(25.137735, 121.541649);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=16;
            }
            if(res==13) {
                txv2.setText("華美自助餐");
                Point = new LatLng(25.137667, 121.541436);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=19;
            }
            if(res==14) {
                txv2.setText("五路鍋聖");
                Point = new LatLng(25.137681, 121.541246);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=20;
            }
            if(res==15) {
                txv2.setText("8鍋臭臭鍋");
                Point = new LatLng(25.137548, 121.541281);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=21;
            }
            if(res==16) {
                txv2.setText("淡江炒飯");
                Point = new LatLng(25.137747, 121.541201);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=22;
            }
            if(res==17) {
                txv2.setText("宵夜快餐");
                Point = new LatLng(25.137718, 121.541035);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=23;
            }
            if(res==18) {
                txv2.setText("敦煌快餐");
                Point = new LatLng(25.137748, 121.541139);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=24;
            }
            if(res==19) {
                txv2.setText("池玖便當");
                Point = new LatLng(25.137346, 121.541333);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=25;
            }
            if(res==20) {
                txv2.setText("大陸麵店");
                Point = new LatLng(25.137322, 121.541344);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=26;
            }
            if(res==21) {
                txv2.setText("牛肉拌麵");
                Point = new LatLng(25.137299, 121.541351);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=27;
            }
            if(res==22) {
                txv2.setText("山東餃子館");
                Point = new LatLng(25.137214, 121.541482);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=30;
            }
            if(res==23) {
                txv2.setText("大Q麵店");
                Point = new LatLng(25.137187, 121.541658);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=31;
            }
            if(res==24) {
                txv2.setText("大紅袍");
                Point = new LatLng(25.137211, 121.541728);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=32;
            }
            if(res==25) {
                txv2.setText("石鍋拌飯");
                Point = new LatLng(25.137321, 121.542032);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=36;
            }
            if(res==26) {
                txv2.setText("台北城");
                Point = new LatLng(25.137388, 121.542179);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=37;
            }
            if(res==27) {
                txv2.setText("藥膳排骨");
                Point = new LatLng(25.137399, 121.542205);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=38;
            }
            if(res==28) {
                txv2.setText("魷魚羹");
                Point = new LatLng(25.137408, 121.542235);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=39;
            }
            if(res==29) {
                txv2.setText("老地方炒飯");
                Point = new LatLng(25.137454, 121.542117);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=40;
            }
            if(res==30) {
                txv2.setText("感恩麵店");
                Point = new LatLng(25.137529, 121.542032);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=41;
            }
            if(res==31) {
                txv2.setText("輔大豬排");
                Point = new LatLng(25.137264, 121.542330);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=42;
            }
        }
//______________________________________________________________________________________________________________午餐

//______________________________________________________________________________________________________________晚餐
        if(ret==2){//選擇晚餐

            res= (int)(Math.random()*31+1);//抽選
            if(res==1) {
                txv2.setText("麥當勞");
                Point = new LatLng(25.137758, 121.546636);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=1;
            }
            if(res==2) {
                txv2.setText("摩斯漢堡");
                Point = new LatLng(25.137960, 121.546415);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=2;
            }
            if(res==3) {
                txv2.setText("銘記港式燒臘");
                Point = new LatLng(25.137709, 121.546430);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=3;
            }
            if(res==4) {
                txv2.setText("永玉小吃店");
                Point = new LatLng(25.137523, 121.546545);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=5;
            }
            if(res==5) {
                txv2.setText("非常烏龍");
                Point = new LatLng(25.137367, 121.546527);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=6;
            }
            if(res==6) {
                txv2.setText("加熱滷味");
                Point = new LatLng(25.136991, 121.546481);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=8;
            }
            if(res==7) {
                txv2.setText("火車飯包");
                Point = new LatLng(25.136523, 121.546435);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=10;
            }
            if(res==8) {
                txv2.setText("阿國炒羊肉");
                Point = new LatLng(25.135995, 121.546061);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=11;
            }
            if(res==9) {
                txv2.setText("鐵板便當");
                Point = new LatLng(25.137861, 121.542158);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=13;
            }
            if(res==10) {
                txv2.setText("台南意麵");
                Point = new LatLng(25.137764, 121.541763);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=14;
            }
            if(res==11) {
                txv2.setText("慈善便當");
                Point = new LatLng(25.137828, 121.541703);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=15;
            }
            if(res==12) {
                txv2.setText("小田園湯包");
                Point = new LatLng(25.137735, 121.541649);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=16;
            }
            if(res==13) {
                txv2.setText("華美自助餐");
                Point = new LatLng(25.137667, 121.541436);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=19;
            }
            if(res==14) {
                txv2.setText("五路鍋聖");
                Point = new LatLng(25.137681, 121.541246);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=20;
            }
            if(res==15) {
                txv2.setText("8鍋臭臭鍋");
                Point = new LatLng(25.137548, 121.541281);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=21;
            }
            if(res==16) {
                txv2.setText("淡江炒飯");
                Point = new LatLng(25.137747, 121.541201);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=22;
            }
            if(res==17) {
                txv2.setText("宵夜快餐");
                Point = new LatLng(25.137718, 121.541035);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=23;
            }
            if(res==18) {
                txv2.setText("敦煌快餐");
                Point = new LatLng(25.137748, 121.541139);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=24;
            }
            if(res==19) {
                txv2.setText("池玖便當");
                Point = new LatLng(25.137346, 121.541333);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=25;
            }
            if(res==20) {
                txv2.setText("大陸麵店");
                Point = new LatLng(25.137322, 121.541344);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=26;
            }
            if(res==21) {
                txv2.setText("牛肉拌麵");
                Point = new LatLng(25.137299, 121.541351);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=27;
            }
            if(res==22) {
                txv2.setText("山東餃子館");
                Point = new LatLng(25.137214, 121.541482);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=30;
            }
            if(res==23) {
                txv2.setText("大Q麵店");
                Point = new LatLng(25.137187, 121.541658);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=31;
            }
            if(res==24) {
                txv2.setText("大紅袍");
                Point = new LatLng(25.137211, 121.541728);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=32;
            }
            if(res==25) {
                txv2.setText("石鍋拌飯");
                Point = new LatLng(25.137321, 121.542032);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=36;
            }
            if(res==26) {
                txv2.setText("台北城");
                Point = new LatLng(25.137388, 121.542179);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=37;
            }
            if(res==27) {
                txv2.setText("藥膳排骨");
                Point = new LatLng(25.137399, 121.542205);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=38;
            }
            if(res==28) {
                txv2.setText("魷魚羹");
                Point = new LatLng(25.137408, 121.542235);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=39;
            }
            if(res==29) {
                txv2.setText("老地方炒飯");
                Point = new LatLng(25.137454, 121.542117);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=40;
            }
            if(res==30) {
                txv2.setText("感恩麵店");
                Point = new LatLng(25.137529, 121.542032);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=41;
            }
            if(res==31) {
                txv2.setText("輔大豬排");
                Point = new LatLng(25.137264, 121.542330);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=42;
            }
        }
//______________________________________________________________________________________________________________晚餐

//______________________________________________________________________________________________________________宵夜
        if(ret==3){//選擇宵夜
            res= (int)(Math.random()*11+1);//抽選
            if(res==1) {
                txv2.setText("麥當勞");
                Point = new LatLng(25.137758, 121.546636);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=1;
            }
            if(res==2) {
                txv2.setText("摩斯漢堡");
                Point = new LatLng(25.137960, 121.546415);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=2;
            }
            if(res==3) {
                txv2.setText("派克雞排");
                Point = new LatLng(25.137602, 121.546456);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=4;
            }
            if(res==4) {
                txv2.setText("加熱滷味");
                Point = new LatLng(25.136991, 121.546489);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=8;
            }
            if(res==5) {
                txv2.setText("蘭陽派");
                Point = new LatLng(25.136763, 121.546453);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=9;
            }
            if(res==6) {
                txv2.setText("阿國炒羊肉");
                Point = new LatLng(25.135984, 121.546056);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=11;
            }
            if(res==7) {
                txv2.setText("台南意麵");
                Point = new LatLng(25.137758, 121.541762);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=14;
            }
            if(res==8) {
                txv2.setText("蜜汁燒烤");
                Point = new LatLng(25.137768, 121.541513);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=18;
            }
            if(res==9) {
                txv2.setText("宵夜快餐");
                Point = new LatLng(25.137719, 121.541040);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=23;
            }
            if(res==10) {
                txv2.setText("Dr. Chicky");
                Point = new LatLng(25.137289, 121.541894);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=35;
            }
            if(res==11) {
                txv2.setText("鹽水雞");
                Point = new LatLng(25.137638, 121.541825);
                map.animateCamera(CameraUpdateFactory.newLatLng(Point));
                res2=42;
            }

        }
//______________________________________________________________________________________________________________宵夜
    }
}