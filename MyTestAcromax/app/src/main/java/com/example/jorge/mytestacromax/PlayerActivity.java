package com.example.jorge.mytestacromax;

import android.animation.Animator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.jorge.mytestacromax.utilite.Information.PUT_EXTRA_FILE;
import static com.example.jorge.mytestacromax.utilite.Information.PUT_EXTRA_HEIGHT;
import static com.example.jorge.mytestacromax.utilite.Information.PUT_EXTRA_NAME;
import static com.example.jorge.mytestacromax.utilite.Information.PUT_EXTRA_WIDTH;
import static com.example.jorge.mytestacromax.utilite.Information.TAG;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_BANDWIDTH_FRACTION;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MAX_INITIAL_BITRATE;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS;

/** Activty the Detail with player*/
public class PlayerActivity extends AppCompatActivity implements ExoPlayer.EventListener {

    private TextView tv_name;

    private ProgressBar progressBar;

    public int publicDeltaX;
    public int publicDeltaY;

    public float publicDownX = 0;
    public float publicDownY = 0;
    public float publicUpX = 0;
    public float publicUpY = 0;

    private String mHeight;
    private String mWidth;

    String mName;
    String mFile;
    String mType;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private LinearLayout mLinearLayoutPlayPause;
    private LinearLayout mLinearLayoutLineTime;
    private RelativeLayout mRelativeLayout;
    private android.widget.RelativeLayout.LayoutParams layoutParams;

    private CircleImageView exo_play;
    private CircleImageView exo_pause;


    /** Get event for pass Parent*/
    private View.OnTouchListener mParentListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            getOnTouch(view,motionEvent);
            return true;
        }
    };

    /** Get event for pass Parent*/
    private View.OnDragListener mParentListenerDrag = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            getOnDrag(view,dragEvent);
            return false;
        }
    };

    @Override
    protected void onStart() {
        putCenterPlayer();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        /** Returns Put extra*/
        Bundle extras = getIntent().getExtras();
        mName = extras.getString(PUT_EXTRA_NAME);
        mFile = extras.getString(PUT_EXTRA_FILE);
        mType = extras.getString(PUT_EXTRA_FILE);
        mHeight = extras.getString(PUT_EXTRA_HEIGHT);
        mWidth = extras.getString(PUT_EXTRA_WIDTH);

        /** Title Video*/
        tv_name  = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(mName);

        /** View Detail with event*/
        mLinearLayoutLineTime = (LinearLayout) findViewById(R.id.ll_line_time);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_player);
        mLinearLayoutPlayPause = (LinearLayout) findViewById(R.id.ll_play_pause);
        mLinearLayoutPlayPause.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(mLinearLayoutPlayPause);
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });
        mLinearLayoutPlayPause.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return getOnDrag(v,event);
            }
        });
        mLinearLayoutPlayPause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               return getOnTouch(v, event);
            }
        });


        /** Control Player with event*/
        exo_play  = (CircleImageView) findViewById(R.id.exo_play);
        exo_pause = (CircleImageView) findViewById(R.id.exo_pause);
        exo_play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mParentListener.onTouch(mLinearLayoutPlayPause, event);
                return false;
            }
        });

        exo_pause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mParentListener.onTouch(mLinearLayoutPlayPause, event);
                return false;
            }
        });

        exo_play.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                mParentListenerDrag.onDrag(mLinearLayoutPlayPause, dragEvent);
                return false;
            }
       });

        exo_pause.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                mParentListenerDrag.onDrag(mLinearLayoutPlayPause, dragEvent);
                return false;
            }
        });


        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        /*** 2. Put the best QUALITY*/
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter,DEFAULT_MAX_INITIAL_BITRATE, DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS, DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS, DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS,DEFAULT_BANDWIDTH_FRACTION);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 3. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 4. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Exo2"), defaultBandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.

        if (mType.equals("SOUND")){
            MediaSource mediaSource = null;
            mediaSource = createPlayerSound(dataSourceFactory, extractorsFactory);
            player.prepare(mediaSource);
        }else{
            HlsMediaSource hlsMediaSource = null;
            hlsMediaSource = createPlayerVideo(dataSourceFactory, mainHandler);
            player.prepare(hlsMediaSource);
        }

        player.addListener(this);
        simpleExoPlayerView.requestFocus();
        player.setPlayWhenReady(true);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    /*** function return HlsMediaSource of the Video*/
    private HlsMediaSource createPlayerVideo(DataSource.Factory dataSourceFactory, Handler mainHandler) {
        HlsMediaSource hlsMediaSource = new HlsMediaSource(Uri.parse(mFile), dataSourceFactory, mainHandler, new AdaptiveMediaSourceEventListener() {
            @Override
            public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {
                Log.i(TAG, "onLoadStarted: ");
            }

            @Override
            public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
                Log.i(TAG, "onLoadCompleted: ");
            }

            @Override
            public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
                Log.i(TAG, "onLoadCanceled: ");
            }

            @Override
            public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
                Log.i(TAG, "onLoadError: ");
            }

            @Override
            public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
                Log.i(TAG, "onUpstreamDiscarded: ");
            }

            @Override
            public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {
                Log.i(TAG, "onDownstreamFormatChanged: ");
            }
        });
        return  hlsMediaSource;

    }

    /*** function return HlsMediaSource of the Sound*/
    private MediaSource createPlayerSound(DataSource.Factory dataSourceFactory, ExtractorsFactory extractorsFactory)  {
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mFile),
                dataSourceFactory,
                extractorsFactory,
                null,
                null);
        return  mediaSource;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }


    /*** Listening Touch finger user */
    public void onLeftToRightSwipe(View v){

        Log.i(TAG, "left to right");

        v.animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(1000)
                .x(mRelativeLayout.getMeasuredWidth() - v.getMeasuredWidth()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mRelativeLayout.invalidate();
    }

    /*** Listening Touch finger user */
    public void onRightToLeftSwipe(View v) {

        Log.i(TAG, "right to left");

        v.animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(1000)
                .x(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mRelativeLayout.invalidate();
    }

    /*** Listening Touch finger user */
    public void onTopToBottomSwipe(View v) {

        Log.i(TAG, "top to bottom");

        v.animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(1000)
        .y(mRelativeLayout.getMeasuredHeight() - v.getMeasuredHeight() - mLinearLayoutLineTime.getHeight()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mRelativeLayout.invalidate();
    }

    /*** Listening Touch finger user */
    public void onBottomToTopSwipe(View v) {

        Log.i(TAG, "bottom to top ");

        v.animate().setInterpolator(new DecelerateInterpolator())
            .setDuration(1000)
                .y(0 + v.getPaddingTop()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mRelativeLayout.invalidate();
    }

    /*** Listening Pause user */
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false); //to pause a video because now our video player is not in focus
        }
    }


    /*** Dextroy player */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    /*** Change Progreess an Control Player dependent of the Status */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                //You can use progress dialog to show user that video is preparing or buffering so please wait
                Log.i(TAG, "STATE_BUFFERING: ");
                progressBar.setVisibility(View.VISIBLE);
                exo_play.setCircleBackgroundColorResource(R.color.colorAccentBuffer);
                exo_pause.setCircleBackgroundColorResource(R.color.colorAccentBuffer);
                break;
            case ExoPlayer.STATE_IDLE:
                Log.i(TAG, "STATE_IDLE: ");
                //idle state
                break;
            case ExoPlayer.STATE_READY:
                // dismiss your dialog here because our video is ready to play now
                Log.i(TAG, "STATE_READY: ");
                progressBar.setVisibility(View.GONE);
                exo_play.setCircleBackgroundColorResource(R.color.colorPrimary);
                exo_pause.setCircleBackgroundColorResource(R.color.colorPrimary);

                break;
            case ExoPlayer.STATE_ENDED:
                Log.i(TAG, "STATE_ENDED: ");
                // do your processing after ending of video
                break;
        }
    }

    /*** Listening Touch finger user */
   public boolean getOnTouch(View v, MotionEvent event){
       int min_distance = 200;
       final int X = (int) event.getRawX();
       final int Y = (int) event.getRawY();

       switch (event.getAction() & MotionEvent.ACTION_MASK) {
           case MotionEvent.ACTION_DOWN:
               publicDownX = X;
               publicDownY = Y;
               Log.i(TAG,"ACTION_DOWN" + "x"+ Integer.toString(X) + "y" + Integer.toString(Y));
               RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
               publicDeltaX = X - lParams.leftMargin;
               publicDeltaY = Y - lParams.topMargin;
               break;
           case MotionEvent.ACTION_UP:
               publicUpX = X;
               publicUpY = Y;
               Log.i(TAG,"ACTION_UP" + "x"+ Integer.toString(X) + "y" + Integer.toString(Y));
               float deltaX = publicDownX - publicUpX;
               float deltaY = publicDownY - publicUpY;
               //HORIZONTAL SCROLL
               if (Math.abs(deltaX) > Math.abs(deltaY)) {
                   if (Math.abs(deltaX) > min_distance) {
                       // left or right
                       if (deltaX < 0) {
                           onLeftToRightSwipe(v);
                           return true;
                       }
                       if (deltaX > 0) {
                           onRightToLeftSwipe(v);
                           return true;
                       }
                   } else {
                       //not long enough swipe...
                       return false;
                   }
               }
               //VERTICAL SCROLL
               else {
                   if (Math.abs(deltaY) > min_distance) {
                       // top or down
                       if (deltaY < 0) {
                           onTopToBottomSwipe(v);
                           return true;
                       }
                       if (deltaY > 0) {
                           onBottomToTopSwipe(v);
                           return true;
                       }
                   } else {
                       //not long enough swipe...
                       return false;
                   }
              }
               return true;

           case MotionEvent.ACTION_BUTTON_PRESS:
               Log.i("TAG","ACTION_BUTTON_PRESS" + "x"+ Integer.toString(X) + "y" + Integer.toString(Y));
               break;
           case MotionEvent.ACTION_POINTER_UP:
               Log.i("TAG","ACTION_POINTER_UP" + "x"+ Integer.toString(X) + "y" + Integer.toString(Y));
               break;
           case MotionEvent.ACTION_MOVE:
               Log.i("TAG","ACTION_MOVE" + "x"+ Integer.toString(X) + "y" + Integer.toString(Y));
               RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
               layoutParams.leftMargin = X - publicDeltaX;
               layoutParams.topMargin = Y - publicDeltaY;
               v.setLayoutParams(layoutParams);
               break;
       }
       mRelativeLayout.invalidate();
       return true;
   }

    /*** Funtion of the event getOnDrag */
   public boolean getOnDrag(View view, DragEvent dragEvent){
       switch(dragEvent.getAction()) {
           case DragEvent.ACTION_DRAG_STARTED:
               layoutParams = (RelativeLayout.LayoutParams)view.getLayoutParams();
               Log.d(TAG, "Action is DragEvent.ACTION_DRAG_STARTED");
               break;
           case DragEvent.ACTION_DRAG_ENTERED:
               Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENTERED");
               break;
           case DragEvent.ACTION_DRAG_EXITED :
               Log.d(TAG, "Action is DragEvent.ACTION_DRAG_EXITED");
               view.setLayoutParams(layoutParams);
               break;
           case DragEvent.ACTION_DRAG_LOCATION  :
               Log.d(TAG, "Action is DragEvent.ACTION_DRAG_LOCATION");
               break;
           case DragEvent.ACTION_DRAG_ENDED   :
               Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENDED");
               break;
           case DragEvent.ACTION_DROP:
               Log.d(TAG, "ACTION_DROP event");
               break;
           default: break;
       }
       return true;
   }

    /*** Put Cicle Player in of the middle */
   public void putCenterPlayer(){
       Log.i(TAG, "putCenterPlayer" + mRelativeLayout.getScaleX() + " " + mRelativeLayout.getMeasuredHeight() );

       mLinearLayoutPlayPause.animate().setInterpolator(new DecelerateInterpolator())
               .setDuration(0)
               .x(Float.parseFloat(mWidth))
               .y(Float.parseFloat(mHeight)).setListener(new Animator.AnimatorListener() {
           @Override
           public void onAnimationStart(Animator animator) {

           }

           @Override
           public void onAnimationEnd(Animator animator) {

           }

           @Override
           public void onAnimationCancel(Animator animator) {

           }

           @Override
           public void onAnimationRepeat(Animator animator) {

           }
       });
       mRelativeLayout.invalidate();
   }

}
