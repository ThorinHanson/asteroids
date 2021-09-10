package com.udacity.asteroidradar

import android.media.Image
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.api.AsteroidSelection
import com.udacity.asteroidradar.main.AsteroidAdapter
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.getToday
import java.lang.Exception


@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("asteroidStatusDescription")
fun bindTextOfDay(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.contentDescription = "This asteroid is potentially hazardous"
    } else {
        imageView.contentDescription = "This asteroid should be safe"
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("pictureOfDay")
fun bindPictureOfDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if (pictureOfDay.mediaType != "image") {
            return@let
        }
        val context = imageView.context
        val picasso = Picasso.Builder(context).build()
        picasso.load(pictureOfDay.url).into(imageView)
    }
}

@BindingAdapter("textOfDay")
fun bindTextOfDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if (pictureOfDay.mediaType != "image") {
            return@let
        }
        imageView.contentDescription = pictureOfDay.title
    }
}

@BindingAdapter(value= ["listData", "filter"])
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?, selection: AsteroidSelection) {
    data?.let {
        val adapter = recyclerView.adapter as AsteroidAdapter


        adapter.submitList(data.filter {
            when (selection) {
                AsteroidSelection.NEXT_WEEK -> {
                    return@filter getNextSevenDaysFormattedDates().contains(it.closeApproachDate)
                }
                AsteroidSelection.TODAY -> {
                    return@filter it.closeApproachDate == getToday()
                }
                AsteroidSelection.ALL -> {
                    return@filter true
                }
                else -> throw Exception("Unknown filter")
            }
        })
    }
}