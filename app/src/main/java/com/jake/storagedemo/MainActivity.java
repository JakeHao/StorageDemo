package com.jake.storagedemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllStoragePath(getApplicationContext());
    }

    /**
     * 获取所有存储空间
     *
     * @param mContext
     */
    public void getAllStoragePath(Context mContext)
    {
        StorageManager mStorageManager = (StorageManager)mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try
        {
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++)
            {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String)getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean)isRemovable.invoke(storageVolumeElement);
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取内置存储空间
     *
     * @return
     */
    public String getInternalStorage()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取外置存储空间
     *
     * @param context
     * @return
     */
    public String getExternalStorage(Context context)
    {
        StorageManager mStorageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try
        {
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Method mState = storageVolumeClazz.getMethod("getState");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++)
            {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String)getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean)isRemovable.invoke(storageVolumeElement);
                String state = (String)mState.invoke(storageVolumeElement);
                //mRemovable=false：为内置存储  =true：为外置存储
                //mPath 为存储设置路径
                //mState 为存储设置挂载状态
                if (removable && Environment.MEDIA_MOUNTED.equals(state))
                {
                    return path;
                }
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
