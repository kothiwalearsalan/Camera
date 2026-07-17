package com.procinex.app.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import android.widget.TextView
import androidx.camera.view.PreviewView
import java.util.concurrent.ExecutionException

class CaptureFragment : Fragment() {
    private lateinit var previewView: PreviewView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        previewView = PreviewView(requireContext())
        val containerLayout = FrameLayout(requireContext())
        containerLayout.addView(previewView, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        val tv = TextView(requireContext())
        tv.text = "ProCineX — Capture (stub)"
        tv.setPadding(20,20,20,20)
        containerLayout.addView(tv)
        return containerLayout
    }

    override fun onResume() {
        super.onResume()
        if (hasCameraPermission()) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), CAMERA_PERMISSION_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != CAMERA_PERMISSION_REQUEST) return
        if (hasCameraPermission()) {
            startCamera()
        } else {
            Log.w(TAG, "Camera permission denied; cannot start preview")
            showError("Camera permission is required to use ProCineX.")
        }
    }

    private fun hasCameraPermission(): Boolean =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun startCamera() {
        val providerFuture = ProcessCameraProvider.getInstance(requireContext())
        providerFuture.addListener({
            val cameraProvider = try {
                providerFuture.get()
            } catch (e: ExecutionException) {
                Log.e(TAG, "Failed to obtain camera provider", e)
                showError("Unable to access the camera. Please try again.")
                return@addListener
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                Log.e(TAG, "Interrupted while obtaining camera provider", e)
                showError("Unable to access the camera. Please try again.")
                return@addListener
            }

            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val selector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(viewLifecycleOwner, selector, preview)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to bind camera use cases to lifecycle", e)
                showError("Unable to start the camera preview.")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun showError(message: String) {
        val ctx = context ?: return
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val TAG = "CaptureFragment"
        private const val CAMERA_PERMISSION_REQUEST = 101
    }
}
