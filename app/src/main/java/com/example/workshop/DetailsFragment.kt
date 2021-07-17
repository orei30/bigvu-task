package com.example.workshop

import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.setAlpha
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.squareup.picasso.Picasso


class DetailsFragment : Fragment() {

    private val TAG = "DetailsFragment"
    private lateinit var cardView: CardView
    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var videoView: VideoView
    private lateinit var workshop: Workshop

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // back click will return to workshop list
                activity?.supportFragmentManager?.popBackStack()
            }
        })
    }

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        val transition: Transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_card)
        sharedElementEnterTransition = transition

        // get all views
        cardView = view.findViewById(R.id.cardView)
        nameTextView = view.findViewById(R.id.textViewName)
        descriptionTextView = view.findViewById(R.id.textViewDescription)
        imageView = view.findViewById(R.id.imageView)
        textView = view.findViewById(R.id.textView)
        videoView = view.findViewById(R.id.videoView)
        workshop = viewModel.selectedWorkshop.value!!

        // set transition name to the card view
        ViewCompat.setTransitionName(cardView, "card")

        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String?>,
                    sharedElements: MutableMap<String?, View?>
                ) {
                    sharedElements[names[0]] = cardView
                }
            })

        // start transition
        startPostponedEnterTransition()

        setViews()

        return view
    }

    fun setViews() {
        // set workshop name
        nameTextView?.text = workshop.name

        // set workshop description
        descriptionTextView?.text = workshop.description

        // set workshop image
        Picasso.get()
            .load(workshop.imageUrl)
            .resize(250, 250)
            .transform(ImageRoundCorners())
            .centerCrop()
            .noFade()
            .into(imageView)

        // set workshop text
        textView.text = workshop.text

        // set workshop video view
        videoView.alpha = 0F
        videoView.setOnPreparedListener { mp ->
            mp.setOnInfoListener(MediaPlayer.OnInfoListener { mp, what, _ ->
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    videoView.alpha = 1F
                    return@OnInfoListener true
                }
                false
            })
        }
        // set video path
        videoView.setVideoPath(workshop.videoUrl)
        // start video
        videoView.start();
    }
}