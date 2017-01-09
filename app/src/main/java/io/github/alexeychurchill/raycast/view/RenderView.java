package io.github.alexeychurchill.raycast.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Render view
 * View that can use renderer to render game state
 */

public class RenderView extends SurfaceView implements SurfaceHolder.Callback {
    private RenderThread renderThread;
    private RaycastRenderer renderer;

    public RenderView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public RenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public RenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    public void setRenderer(RaycastRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        renderThread = new RenderThread(holder);
        renderThread.setRunning(true);
        renderThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        renderThread.setRunning(false);
        while (retry) {
            try {
                renderThread.join();
                retry = false;
            } catch (InterruptedException ignored) {
            }
        }
    }

    class RenderThread extends Thread {
        private boolean running = false;
        private SurfaceHolder surfaceHolder;

        public RenderThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (running) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null) {
                        continue;
                    }
                    if (renderer == null) {
                        continue;
                    }
                    renderer.render(canvas, canvas.getWidth(), canvas.getHeight());
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
