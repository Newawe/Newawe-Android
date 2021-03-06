package com.android.volley;

import android.os.Process;
import com.android.volley.Cache.Entry;
import java.util.concurrent.BlockingQueue;

public class CacheDispatcher extends Thread {
    private static final boolean DEBUG;
    private final Cache mCache;
    private final BlockingQueue<Request<?>> mCacheQueue;
    private final ResponseDelivery mDelivery;
    private final BlockingQueue<Request<?>> mNetworkQueue;
    private volatile boolean mQuit;

    /* renamed from: com.android.volley.CacheDispatcher.1 */
    class C02921 implements Runnable {
        final /* synthetic */ Request val$finalRequest;

        C02921(Request request) {
            this.val$finalRequest = request;
        }

        public void run() {
            try {
                CacheDispatcher.this.mNetworkQueue.put(this.val$finalRequest);
            } catch (InterruptedException e) {
            }
        }
    }

    static {
        DEBUG = VolleyLog.DEBUG;
    }

    public CacheDispatcher(BlockingQueue<Request<?>> cacheQueue, BlockingQueue<Request<?>> networkQueue, Cache cache, ResponseDelivery delivery) {
        this.mQuit = false;
        this.mCacheQueue = cacheQueue;
        this.mNetworkQueue = networkQueue;
        this.mCache = cache;
        this.mDelivery = delivery;
    }

    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    public void run() {
        if (DEBUG) {
            VolleyLog.m12v("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(10);
        this.mCache.initialize();
        while (true) {
            try {
                Request<?> request = (Request) this.mCacheQueue.take();
                try {
                    request.addMarker("cache-queue-take");
                    if (request.isCanceled()) {
                        request.finish("cache-discard-canceled");
                    } else {
                        Entry entry = this.mCache.get(request.getCacheKey());
                        if (entry == null) {
                            request.addMarker("cache-miss");
                            this.mNetworkQueue.put(request);
                        } else if (entry.isExpired()) {
                            request.addMarker("cache-hit-expired");
                            request.setCacheEntry(entry);
                            this.mNetworkQueue.put(request);
                        } else {
                            request.addMarker("cache-hit");
                            Response<?> response = request.parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
                            request.addMarker("cache-hit-parsed");
                            if (entry.refreshNeeded()) {
                                request.addMarker("cache-hit-refresh-needed");
                                request.setCacheEntry(entry);
                                response.intermediate = true;
                                this.mDelivery.postResponse(request, response, new C02921(request));
                            } else {
                                this.mDelivery.postResponse(request, response);
                            }
                        }
                    }
                } catch (Exception e) {
                    VolleyLog.m11e(e, "Unhandled exception %s", e.toString());
                }
            } catch (InterruptedException e2) {
                if (this.mQuit) {
                    return;
                }
            }
        }
    }
}
