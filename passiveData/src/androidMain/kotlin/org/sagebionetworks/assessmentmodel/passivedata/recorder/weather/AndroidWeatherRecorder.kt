package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking


class AndroidWeatherRecorder(
    override val configuration: WeatherConfiguration,
    val context: Context
) :
    WeatherRecorder(configuration) {
    private val tag = "AndroidWeatherRecorder"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val cancellationToken = CancellationTokenSource().token

    @RequiresPermission(
        anyOf =
        ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    override fun start() {
        //TODO: Add way to request permissions at runtime - liujoshua 2021-09-21
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(tag, "No permissions for location")
            return
        }

        runBlocking {
            Napier.i("Launching services")
            launchWeatherServices(
                getLocation()
            )
        }
    }

    @RequiresPermission(
        anyOf =
        ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    override suspend fun getLocation(): Location? {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        cancellationToken.onCanceledRequested {
            cancel()
        }
        val currentLocation =
            fusedLocationClient.getCurrentLocation(
                PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationToken
            )

        val result = CompletableDeferred<Location?>()
        currentLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result.complete(with(currentLocation.result) {
                    Location(longitude = longitude, latitude = latitude)
                })
            } else if (task.exception != null) {
                result.completeExceptionally(task.exception!!)
            } else {
                result.complete(null)
            }
        }

        return result.await()
    }
}