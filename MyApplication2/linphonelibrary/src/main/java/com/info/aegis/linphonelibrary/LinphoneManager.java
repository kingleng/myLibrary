package com.info.aegis.linphonelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.TextureView;
import android.widget.RelativeLayout;

import com.info.aegis.linphonelibrary.callback.CallState;
import com.info.aegis.linphonelibrary.callback.RegiserationState;
import com.info.aegis.linphonelibrary.callback.ServiceCallback;
import com.info.aegis.linphonelibrary.service.LinphoneService;

import org.linphone.core.AccountCreator;
import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.TransportType;
import org.linphone.core.VideoDefinition;
import org.linphone.core.tools.Log;

/**
 * Created by leng on 2020/6/17.
 */
public class LinphoneManager {

    private static LinphoneManager instance;

    public static LinphoneManager getInstance(){
        if(instance == null){
            synchronized (LinphoneManager.class){
                if(instance == null){
                    instance = new LinphoneManager();
                }
            }
        }

        return instance;
    }

    public LinphoneManager() {
        mHandler = new Handler();
    }

    private Handler mHandler;
    ServiceCallback callback;
    public void startService(Context mContext, ServiceCallback callback){
        this.callback = callback;
        if (LinphoneService.isReady()) {
            callback.serviceStarted();
        } else {
            // If it's not, let's start it
            mContext.startService(new Intent().setClass(mContext, LinphoneService.class));
            // And wait for it to be ready, so we can safely use it afterwards
            new ServiceWaitThread().start();
        }
    }

    private class ServiceWaitThread extends Thread {
        public void run() {
            while (!LinphoneService.isReady()) {
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException("waiting thread sleep() has been interrupted");
                }
            }
            mHandler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            callback.serviceStarted();
                        }
                    });
        }
    }

    public boolean getState(){

        ProxyConfig proxyConfig = LinphoneService.getCore().getDefaultProxyConfig();
        if (proxyConfig != null) {
            updateLed(proxyConfig.getState());
            return true;
        }else{
            return false;
        }
    }

    public void updateLed(RegistrationState state) {
        switch (state) {
            case Ok: // This state means you are connected, to can make and receive calls & messages
//                mLed.setImageResource(R.drawable.led_connected);
                break;
            case None: // This state is the default state
            case Cleared: // This state is when you disconnected
//                mLed.setImageResource(R.drawable.led_disconnected);
                break;
            case Failed: // This one means an error happened, for example a bad password
//                mLed.setImageResource(R.drawable.led_error);
                break;
            case Progress: // Connection is in progress, next state will be either Ok or Failed
//                mLed.setImageResource(R.drawable.led_inprogress);
                break;
        }
    }

    private AccountCreator mAccountCreator;
    public void configureAccount(String mUsername, String mPassword, String mDomain, String mDisplayName) {
        // At least the 3 below values are required

        mAccountCreator = LinphoneService.getCore().createAccountCreator(null);
        mAccountCreator.setUsername(mUsername);
        mAccountCreator.setDomain(mDomain);
        mAccountCreator.setPassword(mPassword);
        mAccountCreator.setDisplayName(mDisplayName);

        mAccountCreator.setTransport(TransportType.Udp);

        // This will automatically create the proxy config and auth info and add them to the Core
        ProxyConfig cfg = mAccountCreator.createProxyConfig();
        // Make sure the newly created one is the default
        LinphoneService.getCore().setDefaultProxyConfig(cfg);
    }

    private CoreListenerStub mCoreListener;
    public void addListener(final RegiserationState regiserationState, final CallState callState){
        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {

                if (state == Call.State.IncomingReceived) {
                    if(callState!=null){
                        callState.incomingCall(call);
                    }
                } else if (state == Call.State.Connected) {
                    if(callState!=null){
                        callState.connected();
                    }
                } else if (state == Call.State.OutgoingInit) {
                    if(callState!=null){
                        callState.outgoingInit();
                    }
                } else if (state == Call.State.End || state == Call.State.Released) {
                    if(callState!=null){
                        callState.end();
                    }
                }
            }

            @Override
            public void onRegistrationStateChanged(Core core, ProxyConfig cfg, RegistrationState state, String message) {

                if (state == RegistrationState.Ok) {
                    if(regiserationState!=null){
                        regiserationState.registrationOk();
                    }
                } else if (state == RegistrationState.Failed) {
                    if(regiserationState!=null){
                        regiserationState.registrationFailed();
                    }
                } else if (state == RegistrationState.Progress) {
                    if(regiserationState!=null){
                        regiserationState.registrationProgress();
                    }
                } else if (state == RegistrationState.None) {
                    if(regiserationState!=null){
                        regiserationState.registrationNone();
                    }
                }else if (state == RegistrationState.Cleared) {
                    if(regiserationState!=null){
                        regiserationState.registrationCleared();
                    }
                }
            }
        };

        LinphoneService.getCore().addListener(mCoreListener);
    }

    public void removeListener(){
        LinphoneService.getCore().removeListener(mCoreListener);
    }

    public void toCall(String userName, boolean videoEnabled){
        Core core = LinphoneService.getCore();
        Address addressToCall = core.interpretUrl(userName);
        CallParams params = core.createCallParams(null);
        params.enableVideo(videoEnabled);

        if (addressToCall != null) {
            core.inviteAddressWithParams(addressToCall, params);
        }
    }

    public void acceptCall(Call call){
        CallParams params = LinphoneService.getCore().createCallParams(call);
        params.enableVideo(true);
        call.acceptWithParams(params);
    }

    public void resizePreview(Activity context, TextureView mCaptureView) {
        Core core = LinphoneService.getCore();
        if (core.getCallsNb() > 0) {
            Call call = core.getCurrentCall();
            if (call == null) {
                call = core.getCalls()[0];
            }
            if (call == null) return;

            DisplayMetrics metrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenHeight = metrics.heightPixels;
            int maxHeight =
                    screenHeight / 4; // Let's take at most 1/4 of the screen for the camera preview

            VideoDefinition videoSize =
                    call.getCurrentParams()
                            .getSentVideoDefinition(); // It already takes care of rotation
            if (videoSize.getWidth() == 0 || videoSize.getHeight() == 0) {
                Log.w(
                        "[Video] Couldn't get sent video definition, using default video definition");
                videoSize = core.getPreferredVideoDefinition();
            }
            int width = videoSize.getWidth();
            int height = videoSize.getHeight();

            Log.d("[Video] Video height is " + height + ", width is " + width);
            width = width * maxHeight / height;
            height = maxHeight;

            if (mCaptureView == null) {
                Log.e("[Video] mCaptureView is null !");
                return;
            }

            RelativeLayout.LayoutParams newLp = new RelativeLayout.LayoutParams(width, height);
            newLp.addRule(
                    RelativeLayout.ALIGN_PARENT_BOTTOM,
                    1); // Clears the rule, as there is no removeRule until API 17.
            newLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
            mCaptureView.setLayoutParams(newLp);
            Log.d("[Video] Video preview size set to " + width + "x" + height);
        }
    }

    public void terminateCall(){
        Core core = LinphoneService.getCore();
        if (core.getCallsNb() > 0) {
            Call call = core.getCurrentCall();
            if (call == null) {
                // Current call can be null if paused for example
                call = core.getCalls()[0];
            }
            call.terminate();
        }
    }

}
