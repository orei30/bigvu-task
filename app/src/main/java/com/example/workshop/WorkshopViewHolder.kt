package com.example.workshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class WorkshopViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private var context: Context,
    private val onItemClick: ((Workshop, CardView) -> Unit)
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.workshop_list_item, parent, false)) {
    private var nameTextView: TextView = itemView.findViewById(R.id.textViewName)
    private var descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
    private var imageView: ImageView = itemView.findViewById(R.id.imageView)
    private var cardView: CardView = itemView.findViewById(R.id.cardView)

    fun bind(workshop: Workshop) {
        nameTextView?.text = workshop.name
        descriptionTextView?.text = workshop.description

        Picasso.get()
            .load(workshop.imageUrl)
            .resize(250, 250)
            .transform(ImageRoundCorners())
            .centerCrop()
            .noFade()
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    imageView.visibility = View.VISIBLE
                    nameTextView.x = 50F
                    nameTextView.animate().setDuration(300).x(258F).start();
                    descriptionTextView.x = 50F
                    descriptionTextView.animate().setDuration(300).x(258F).start();
                    imageView.alpha = 0F
                    imageView.x = -200F
                    imageView.animate().setDuration(300).x(-50F).rotation(10F).alpha(1F).start();
                }

                override fun onError(e: Exception?) {
                }
            })

        itemView.setOnClickListener {
            onItemClick.invoke(workshop, cardView)
        }
    }

}