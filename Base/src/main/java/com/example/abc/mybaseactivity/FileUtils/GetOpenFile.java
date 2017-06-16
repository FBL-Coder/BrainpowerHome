package com.example.abc.mybaseactivity.FileUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 开开系统工具获取文件（相册，照相机，音频，视频，文件）
 * Author：FBL  Time： 2017/5/10.
 */

public class GetOpenFile {

    /**
     * 获取相册文件
     * @param activity 调用Activity对象
     * @param Tag   requestCode返回识别码
     */
    public static void getSystemImage(Activity activity, int Tag) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        activity.startActivityForResult(intent, Tag);
    }

    /**
     * 获取音频
     * @param activity 调用Activity对象
     * @param Tag   requestCode返回识别码
     */
    public static void getSystemAudio(Activity activity, int Tag) {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, Tag);
    }

    /**
     * 获取视频
     * @param activity 调用Activity对象
     * @param Tag   requestCode返回识别码
     */
    public static void getSystemVideo(Activity activity, int Tag) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(intent, Tag);
    }

    /**
     * 获取文件
     * @param activity 调用Activity对象
     * @param Tag   requestCode返回识别码
     */
    public static void getSystgemFile(Activity activity, int Tag) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, Tag);
    }


    /**
     * 打开照相机
     * @param activity  调用Activity对象
     * @param Tag  requestCode返回识别码
     * @return  拍照保存图片路径
     */
    private static String getCameraPhoto(Activity activity, int Tag) {
        File mCameraImageFile;
        try {
            File PHOTO_DIR = new File(FileUtils.getStorageDirectory());
            if (!PHOTO_DIR.exists())
                PHOTO_DIR.mkdirs();// 创建照片的存储目录

            mCameraImageFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraImageFile));
            activity.startActivityForResult(intent, Tag);
        } catch (ActivityNotFoundException e) {
            return null;
        }
        return mCameraImageFile.getAbsolutePath();
    }

    /**
     * getCameraPhoto（）中调用
     * 用当前时间给取得的图片命名
     */
    private static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy_MM_dd_HH_mm_ss");
        return dateFormat.format(date) + ".jpg";
    }


    /**
     * 打开录音，获取录音
     * @param activity 调用Activity对象
     * @param Tag  requestCode返回识别码
     */
    private void getRecord(Activity activity, int Tag) {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, Tag);
    }


    /**
     * 获取视频缩略图
     * @param videoPath 视频路径
     * @return 视频缩略图对象
     */
    public Bitmap getVideoThumbnail(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(videoPath);
            bitmap = retriever.getFrameAtTime();
        } catch (Exception e) {
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                return null;
            }
        }
        return bitmap;
    }


    /**
     * @param audioPath 文件路径，like XXX/XXX/XX.mp3
     * @return 专辑封面bitmap
     * @Description 获取音频专辑封面的方法
     */
    public Bitmap getAudioImage(String audioPath) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(audioPath); //设置数据源
            byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
            if (embedPic != null) {
                bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
                return bitmap;
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
                return null;
            }
        }
    }
}
