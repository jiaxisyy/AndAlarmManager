package com.example.hekd.kotlinapp.ai

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by hekd on 2017/12/19.
 */
class ImageUtil {
    companion object {
        /**
         * 加载gif图
         */
        fun setGif(imageView: ImageView, context: Context, resourceId: Int) {


            Glide.with(context).load(resourceId).into(imageView)
            /*      val options = RequestOptions()
                          .centerCrop()
                          .priority(Priority.HIGH)
                          .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                  Glide.with(context).asGif().load(resourceId).listener(object : RequestListener<Drawable> {
                      override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                          return false
                      }

                      override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                          return false
                      }
                  }).apply(options).into(imageView)*/


        }
    }


}