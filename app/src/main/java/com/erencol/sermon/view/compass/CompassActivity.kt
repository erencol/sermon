package com.erencol.sermon.view.compass

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.erencol.sermon.R
import com.erencol.sermon.databinding.ActivityCompassBinding
import com.google.android.gms.location.LocationServices
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class CompassActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false

    private var currentAzimuth = 0f
    private var qiblaDirection = 0.0

    lateinit var binding: ActivityCompassBinding

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_compass)
        setToolbar()

        initializeSensors()
        requestLocationPermission()
    }

    private fun initializeSensors() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (accelerometer == null || magnetometer == null) {
            Toast.makeText(this, "Bu cihazda gerekli sensörler mevcut değil!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLocationAndCalculateQibla()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationAndCalculateQibla()
                } else {
                    Toast.makeText(this, "Konum izni gerekli!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getLocationAndCalculateQibla() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        fusedLocation.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                qiblaDirection = getQiblaDirection(location.latitude, location.longitude)
                Toast.makeText(this, "Konum alındı, kıble yönü hesaplandı", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Konum alınamadı", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Konum alma hatası: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                accelerometerReading = event.values.clone()
                lastAccelerometerSet = true
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                magnetometerReading = event.values.clone()
                lastMagnetometerSet = true
            }
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            updateCompass()
        }
    }

    private fun updateCompass() {
        val rotationMatrix = FloatArray(9)
        val orientationAngles = FloatArray(3)

        val success = SensorManager.getRotationMatrix(
            rotationMatrix, null,
            accelerometerReading, magnetometerReading
        )

        if (success) {
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            // Telefon manyetik kuzey yönünü alıyor (radyan cinsinden)
            val azimuthInRadians = orientationAngles[0]
            var azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble())

            // Negatif değerleri pozitif yap (0-360 derece arası)
            if (azimuthInDegrees < 0) {
                azimuthInDegrees += 360
            }

            currentAzimuth = azimuthInDegrees.toFloat()

            // Kıble yönü ile telefon yönü arasındaki farkı hesapla
            var bearing = qiblaDirection - currentAzimuth

            // Kısa yolu bul (-180 ile +180 arasına getir)
            if (bearing < 0) {
                bearing += 360
            }
            if (bearing > 180) {
                bearing -= 360
            }

            // Pusula okunu kıble yönüne döndür
            rotateCompassArrow(bearing.toFloat())
        }
    }

    private fun rotateCompassArrow(bearingToQibla: Float) {
        // Mevcut rotation değerini al
        val currentRotation = binding.qiblaArrow.rotation

        // Hedef rotation değeri
        val targetRotation = bearingToQibla

        // Rotation farkını hesapla (en kısa yolu bul)
        var rotationDiff = targetRotation - currentRotation

        // 180 dereceden fazla farkları optimize et
        while (rotationDiff > 180) rotationDiff -= 360
        while (rotationDiff < -180) rotationDiff += 360

        // Smooth animasyon için RotateAnimation kullan
        val rotateAnimation = RotateAnimation(
            0f, rotationDiff,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 300 // Biraz daha yavaş animasyon
            fillAfter = true
        }

        // Animasyonu uygula
        binding.qiblaArrow.startAnimation(rotateAnimation)
        binding.qiblaArrow.rotation = targetRotation

        // Debug için log (opsiyonel)
        // Log.d("Compass", "Phone azimuth: $currentAzimuth, Qibla: $qiblaDirection, Bearing: $bearingToQibla")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Düşük doğruluk durumunda kullanıcıyı bilgilendir
        if (accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            Toast.makeText(this, "Sensör doğruluğu düşük", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar as Toolbar?)
        supportActionBar?.title = resources.getString(R.string.qibla)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getQiblaDirection(lat: Double, lon: Double): Double {
        val kaabaLat = Math.toRadians(21.4225)
        val kaabaLon = Math.toRadians(39.8262)

        val userLat = Math.toRadians(lat)
        val userLon = Math.toRadians(lon)

        val dLon = kaabaLon - userLon

        val y = sin(dLon) * cos(kaabaLat)
        val x = cos(userLat) * sin(kaabaLat) - sin(userLat) * cos(kaabaLat) * cos(dLon)

        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}