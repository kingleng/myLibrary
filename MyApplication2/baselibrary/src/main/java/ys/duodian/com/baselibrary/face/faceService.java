//package ys.duodian.com.baselibrary.face;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import com.megvii.facepp.sdk.Facepp;
//import com.megvii.licensemanager.sdk.LicenseManager;
//
///**
// * Created by leng on 2019/5/13.
// */
//public class faceService extends Service {
//
//    private Context mContext;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mContext = this;
//
//        LicenseManager licenseManager = new LicenseManager(this);
//
//        String uuid = ConUtil.getUUIDString(this);
//        long apiName = Facepp.getApiName();
//
//        licenseManager.setAuthTimeBufferMillis(0);
//
//        licenseManager.takeLicenseFromNetwork(
//                Util.CN_LICENSE_URL,
//                uuid,
//                Util.API_KEY,
//                Util.API_SECRET,
//                apiName,
//                "1",
//                new LicenseManager.TakeLicenseCallback() {
//                    @Override
//                    public void onSuccess() {
//                        authState(true,0,"");
//                    }
//                    @Override
//                    public void onFailed(int i, byte[] bytes) {
//                        if(TextUtils.isEmpty(Util.API_KEY)||TextUtils.isEmpty(Util.API_SECRET)) {
//                            if (!ConUtil.isReadKey(mContext)) {
//                                authState(false,1001,"");
//                            }else{
//                                authState(false,1001,"");
//                            }
//                        }else{
//                            String msg="";
//                            if (bytes!=null && bytes.length>0){
//                                msg=  new String(bytes);
//                            }
//                            authState(false,i,msg);
//                        }
//                    }
//                });
//    }
//
//    private void authState(boolean isSuccess,int code,String msg) {
//        if (isSuccess) {
//
//            Toast.makeText(mContext,"授权成功",Toast.LENGTH_SHORT).show();
////            Intent intent = new Intent();
////            intent.setClass(this, FaceppActionActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity,all of the other activities on top of it will be closed and this Intent will be delivered to the (now on top) old activity as a new Intent.
////            startActivity(intent);
////
////            finish();
//        } else {
//            Toast.makeText(mContext,"授权失败",Toast.LENGTH_SHORT).show();
////            WarrantyBar.setVisibility(View.GONE);
////            againWarrantyBtn.setVisibility(View.VISIBLE);
////            if (code==1001){
////                WarrantyText.setText(Html.fromHtml("<u>"+getResources().getString(R.string.key_secret)+"</u>"));
////                WarrantyText.setOnClickListener(onlineClick);
////            }else {
////                WarrantyText.setText(Html.fromHtml("<u>"+"code="+code+"，msg="+msg+"</u>"));
////                WarrantyText.setOnClickListener(onlineClick);
////            }
//        }
//    }
//}
