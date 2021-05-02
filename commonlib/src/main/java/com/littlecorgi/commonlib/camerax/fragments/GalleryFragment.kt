package com.littlecorgi.commonlib.camerax.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.littlecorgi.commonlib.R
import com.littlecorgi.commonlib.camerax.utils.padWithDisplayCutout

/**
 * 相册页面
 *
 * @author littlecorgi 2021/5/1
 */
/** Fragment used to present the user with a gallery of photos taken */
class GalleryFragment internal constructor() : Fragment() {

    /** AndroidX navigation arguments */
    private val args: GalleryFragmentArgs by navArgs()

    private lateinit var picUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mark this as a retain fragment, so the lifecycle does not get restarted on config change
        retainInstance = true

        picUri = args.picFileUri
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gallery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Make sure that the cutout "safe area" avoids the screen notch if any
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Use extension method to pad "inside" view containing UI using display cutout's bounds
            view.findViewById<ConstraintLayout>(R.id.cutout_safe_area).padWithDisplayCutout()
        }

        Glide.with(requireContext()).load(picUri)
            .into(view.findViewById<AppCompatImageView>(R.id.photo_view))

        // Handle back button press
        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            deletePicAndBackToCameraFragment()
        }

        // Handle share button press
        view.findViewById<ImageButton>(R.id.ok_button).setOnClickListener {
            requireActivity().setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("uri", picUri)
                putExtra("position", requireActivity().intent.getIntExtra("position", -1))
            })
            requireActivity().finish()
        }

        // Handle delete button press
        view.findViewById<ImageButton>(R.id.delete_button).setOnClickListener {
            deletePicAndBackToCameraFragment()
        }
    }

    private fun deletePicAndBackToCameraFragment() {
        picUri.toFile().delete()
        Log.d(javaClass.simpleName, "onViewCreated: 删除图片")
        Toast.makeText(requireContext(), "照片被删除", Toast.LENGTH_SHORT).show()
        Navigation.findNavController(requireActivity(), R.id.fragment_container).navigateUp()
    }
}
