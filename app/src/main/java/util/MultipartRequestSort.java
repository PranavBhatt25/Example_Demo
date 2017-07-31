package util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by wpa5 on 12/1/17.
 */

public class MultipartRequestSort extends Request<JSONObject> {

// private MultipartEntity entity = new MultipartEntity();

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpentity;
    private static final String FILE_PART_NAME = "upload_file";
    private static final String FILE_PART_NAME_PHOTO = "photo";

    private final Response.Listener<JSONObject> mListener;
    private final File mFilePartVideo, mFilePartPhoto, mFilePartVideoThumbnail;
    private final Map<String, String> mStringPart;

    public MultipartRequestSort(String url, Response.ErrorListener errorListener,
                                Response.Listener<JSONObject> listener, File fileVideoPath, File fileVideoThumbnail, File filePhoto,
                                Map<String, String> mStringPart) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mFilePartVideo = fileVideoPath;
        mFilePartVideoThumbnail = fileVideoThumbnail;
        mFilePartPhoto = filePhoto;
        this.mStringPart = mStringPart;
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        buildMultipartEntity();
    }

    public void addStringBody(String param, String value) {
        mStringPart.put(param, value);
    }

    private void buildMultipartEntity() {
        try {
            if (mFilePartVideo != null && mFilePartVideo.exists()) {
                entity.addPart(Constant.JSON_KEY.VIDEO, new FileBody(mFilePartVideo));
            }
            if (mFilePartPhoto != null && mFilePartPhoto.exists()) {
                entity.addPart(Constant.JSON_KEY.IMAGE, new FileBody(mFilePartPhoto));
            }
            if (mFilePartVideoThumbnail != null && mFilePartVideoThumbnail.exists()) {
                entity.addPart(Constant.JSON_KEY.VIDEO_THUMB, new FileBody(mFilePartVideoThumbnail));
            }
            for (Map.Entry<String, String> entry : mStringPart.entrySet()) {
                // entity.addBinaryBody(entry.getKey(), entry.getValue().getBytes(Charset.forName("UTF-8")));
                // entity.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.TEXT_PLAIN));

                StringBody body = null;  //Charset.forName("UTF-8")
                body = new StringBody(entry.getValue(), "text/plain", Charset.forName("UTF-8"));
                entity.addPart(entry.getKey(), body);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBodyContentType() {
        return httpentity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpentity = entity.build();
            httpentity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public Request<?> setRequestQueue(RequestQueue requestQueue) {
        return super.setRequestQueue(requestQueue);
    }


//    @Override
//    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//        return Response.success("Uploaded", getCacheEntry());
//    }


    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}