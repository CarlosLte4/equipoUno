package com.example.picobotella.ui.home

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella.R
import com.example.picobotella.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    // Estado del audio de fondo
    private var isAudioOn = true
    private var backgroundPlayer: MediaPlayer? = null

    // Estado del giro de la botella
    private var bottleRotation = 0f
    private var countdownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackPressHandler()
        setupAudio()
        setupButtonAnimations()
        setupToolbarButtons()
        setupPresioname()
        startBlinkAnimation()
    }

    // Criterio 5 de HU 1.0: al presionar atrás en el home, salir de la app (no volver al splash)
    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        )
    }

    // HU 2.0 Criterio 7: sonido de fondo al iniciar
    private fun setupAudio() {

        try {
            // TODO: Agregar background_music.mp3 en res/raw/ cuando se tenga el audio backgroundPlayer = MediaPlayer.create(requireContext(), R.raw.background_music)
            backgroundPlayer?.isLooping = true
            backgroundPlayer?.start()
            isAudioOn = true
        } catch (e: Exception) {
            // Si no existe el recurso de audio aún, simplemente no reproduce
            isAudioOn = false
            binding.btnAudio.setImageResource(R.drawable.ic_volume_off)
        }
    }//

    // HU 3.0 Criterio 7: animación sutil de touch en cada botón de la toolbar
    private fun setupButtonAnimations() {
        val buttons = listOf(
            binding.btnRate,
            binding.btnAudio,
            binding.btnInstructions,
            binding.btnChallenges,
            binding.btnShare
        )
        buttons.forEach { btn ->
            btn.setOnTouchListener { v, _ ->
                v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(100)
                    .withEndAction { v.animate().scaleX(1f).scaleY(1f).setDuration(100).start() }
                    .start()
                false
            }
        }
    }

    private fun setupToolbarButtons() {

        // HU 3.0 Criterio 2: estrella → calificar (HU 4.0)
        binding.btnRate.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es")
            )
            startActivity(intent)
        }

        // HU 3.0 Criterio 3: toggle audio
        binding.btnAudio.setOnClickListener {
            isAudioOn = !isAudioOn
            if (isAudioOn) {
                backgroundPlayer?.start()
                binding.btnAudio.setImageResource(R.drawable.ic_volume_on)
            } else {
                backgroundPlayer?.pause()
                binding.btnAudio.setImageResource(R.drawable.ic_volume_off)
            }
        }

        // HU 3.0 Criterio 4: instrucciones (HU 5.0)
        binding.btnInstructions.setOnClickListener {
            pauseAudioIfOn()
            findNavController().navigate(R.id.action_homeFragment_to_instructionsFragment)
        }

        // HU 3.0 Criterio 5: retos (HU 6.0)
        binding.btnChallenges.setOnClickListener {
            pauseAudioIfOn()
            findNavController().navigate(R.id.action_homeFragment_to_challengesFragment)
        }

        // HU 3.0 Criterio 6: compartir (HU 10)
        binding.btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,
                    "App pico botella.\nSolo los valientes lo juegan !!\n" +
                    "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es")
            }
            startActivity(Intent.createChooser(shareIntent, "Compartir"))
        }
    }

    // HU 2.0 Criterio 6 + HU 11: botón parpadeante que gira la botella
    private fun setupPresioname() {
        binding.btnPresioname.setOnClickListener {
            spinBottle()
        }
    }

    // Animación de parpadeo en el botón "Presióname"
    private fun startBlinkAnimation() {
        val blink = AlphaAnimation(0.3f, 1.0f).apply {
            duration = 600
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        binding.btnPresioname.startAnimation(blink)
    }

    // HU 11: girar la botella aleatoriamente
    private fun spinBottle() {
        // Ocultar botón mientras gira (HU 11 Criterio 7)
        binding.btnPresioname.clearAnimation()
        binding.btnPresioname.visibility = View.INVISIBLE
        binding.tvCountdown.visibility = View.GONE

        // Pausar audio de fondo mientras gira (HU 11 Criterio 8)
        if (isAudioOn) backgroundPlayer?.pause()

        // Giro aleatorio: entre 3 y 5 vueltas completas + dirección aleatoria
        val extraDegrees = (3 * 360 + (0..360).random()).toFloat()
        val targetRotation = bottleRotation + extraDegrees
        val spinDuration = (3000..5000).random().toLong()

        binding.bottleImage.animate()
            .rotation(targetRotation)
            .setDuration(spinDuration)
            .withEndAction {
                // Guardar la rotación final para el próximo giro (Criterio 4)
                bottleRotation = targetRotation % 360
                startCountdown()
            }
            .start()
    }

    // HU 11 Criterio 5 y 6: cuenta regresiva 3→0 al detenerse la botella
    private fun startCountdown() {
        binding.tvCountdown.visibility = View.VISIBLE
        var count = 3

        countdownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCountdown.text = count.toString()
                count--
            }

            override fun onFinish() {
                binding.tvCountdown.text = "0"
                binding.tvCountdown.postDelayed({
                    binding.tvCountdown.visibility = View.GONE
                    // Mostrar botón nuevamente (Criterio 7)
                    binding.btnPresioname.visibility = View.VISIBLE
                    startBlinkAnimation()
                    // TODO: navegar a HU 12 (mostrar reto aleatorio)
                    // findNavController().navigate(R.id.action_homeFragment_to_challengeDialogFragment)
                }, 500)
            }
        }.start()
    }

    // Utilitario: pausar audio solo si estaba encendido
    fun pauseAudioIfOn() {
        if (isAudioOn) backgroundPlayer?.pause()
    }

    // Utilitario: reanudar audio si estaba encendido
    fun resumeAudioIfOn() {
        if (isAudioOn) backgroundPlayer?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        backgroundPlayer?.release()
        backgroundPlayer = null
    }
}
