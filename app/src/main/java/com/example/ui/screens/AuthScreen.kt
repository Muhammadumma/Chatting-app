package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuthStep
import com.example.ui.theme.KickAmber
import com.example.ui.theme.KickBlue
import com.example.ui.theme.KickBorder
import com.example.ui.theme.KickCard
import com.example.ui.theme.KickCyan
import com.example.ui.theme.KickDeep
import com.example.ui.theme.KickObsidian
import com.example.ui.theme.KickTextMuted
import com.example.ui.theme.KickTextPrimary
import com.example.ui.theme.KickTextSecondary
import com.example.ui.theme.KickViolet
import kotlinx.coroutines.delay

data class CountryCode(val name: String, val dial: String, val flag: String)

val countryCodes = listOf(
  CountryCode("United States", "+1", "🇺🇸"),
  CountryCode("United Kingdom", "+44", "🇬🇧"),
  CountryCode("India", "+91", "🇮🇳"),
  CountryCode("Germany", "+49", "🇩🇪"),
  CountryCode("France", "+33", "🇫🇷"),
  CountryCode("Japan", "+81", "🇯🇵"),
  CountryCode("UAE", "+971", "🇦🇪"),
  CountryCode("Canada", "+1", "🇨🇦"),
  CountryCode("Australia", "+61", "🇦🇺"),
  CountryCode("Singapore", "+65", "🇸🇬")
)

@Composable
fun AuthScreen(
  onCompleteAuth: (name: String, handle: String, facet: String, bio: String, avatarColor: Color, phone: String) -> Unit,
  onBypass: () -> Unit = {}
) {
  var currentStep by remember { mutableStateOf(AuthStep.PHONE_ENTRY) }

  var selectedCountry by remember { mutableStateOf(countryCodes[0]) }
  var phoneNumber by remember { mutableStateOf("") }
  var otpDigits by remember { mutableStateOf("") }
  var otpError by remember { mutableStateOf<String?>(null) }
  var resendTimer by remember { mutableIntStateOf(30) }

  // Profile setup state
  var profileName by remember { mutableStateOf("Mu'in Architect") }
  var profileHandle by remember { mutableStateOf("@muin_kick") }
  var selectedFacet by remember { mutableStateOf("Distributed Architect & ZK") }
  var profileBio by remember { mutableStateOf("Crafting cryptographic trust-distance protocols and generative systems.") }
  var selectedAvatarColor by remember { mutableStateOf(KickCyan) }

  // Countdown timer for OTP
  LaunchedEffect(currentStep, resendTimer) {
    if (currentStep == AuthStep.OTP_VERIFICATION && resendTimer > 0) {
      delay(1000)
      resendTimer -= 1
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(KickObsidian)
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding()
      .testTag("auth_screen")
  ) {
    // Ambient background radial glow
    Box(
      modifier = Modifier
        .size(320.dp)
        .align(Alignment.TopCenter)
        .background(
          Brush.radialGradient(
            colors = listOf(KickCyan.copy(alpha = 0.12f), Color.Transparent)
          )
        )
    )

    AnimatedContent(
      targetState = currentStep,
      transitionSpec = {
        if (targetState.ordinal > initialState.ordinal) {
          slideInHorizontally { it } togetherWith slideOutHorizontally { -it / 2 }
        } else {
          slideInHorizontally { -it / 2 } togetherWith slideOutHorizontally { it }
        }
      },
      label = "auth_step_transition"
    ) { step ->
      when (step) {
        AuthStep.PHONE_ENTRY -> {
          PhoneEntryStep(
            selectedCountry = selectedCountry,
            onCountrySelected = { selectedCountry = it },
            phoneNumber = phoneNumber,
            onPhoneNumberChange = { phoneNumber = it },
            onContinue = {
              if (phoneNumber.length >= 6) {
                resendTimer = 30
                otpDigits = ""
                otpError = null
                currentStep = AuthStep.OTP_VERIFICATION
              }
            },
            onBypass = onBypass
          )
        }

        AuthStep.OTP_VERIFICATION -> {
          OtpVerificationStep(
            fullPhone = "${selectedCountry.dial} $phoneNumber",
            otpDigits = otpDigits,
            onOtpChange = {
              otpDigits = it
              if (otpError != null) otpError = null
            },
            otpError = otpError,
            resendTimer = resendTimer,
            onResend = { resendTimer = 30 },
            onBack = { currentStep = AuthStep.PHONE_ENTRY },
            onVerify = {
              if (otpDigits == "123456") {
                currentStep = AuthStep.PROFILE_SETUP
              } else {
                otpError = "Invalid verification code. Please enter 123456."
              }
            }
          )
        }

        AuthStep.PROFILE_SETUP -> {
          ProfileSetupStep(
            name = profileName,
            onNameChange = { profileName = it },
            handle = profileHandle,
            onHandleChange = { profileHandle = it },
            facet = selectedFacet,
            onFacetChange = { selectedFacet = it },
            bio = profileBio,
            onBioChange = { profileBio = it },
            avatarColor = selectedAvatarColor,
            onAvatarColorChange = { selectedAvatarColor = it },
            onBoom = {
              onCompleteAuth(
                profileName,
                profileHandle,
                selectedFacet,
                profileBio,
                selectedAvatarColor,
                "${selectedCountry.dial} $phoneNumber"
              )
            }
          )
        }

        AuthStep.AUTHENTICATED -> {
          // Handled externally
        }
      }
    }
  }
}

@Composable
fun PhoneEntryStep(
  selectedCountry: CountryCode,
  onCountrySelected: (CountryCode) -> Unit,
  phoneNumber: String,
  onPhoneNumberChange: (String) -> Unit,
  onContinue: () -> Unit,
  onBypass: () -> Unit
) {
  var countryMenuOpen by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp, vertical = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(20.dp))

    // Brand Monogram
    Box(
      modifier = Modifier
        .size(80.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(
          Brush.linearGradient(
            listOf(KickCyan.copy(alpha = 0.25f), Color(0xFF0F2220))
          )
        )
        .border(1.5.dp, KickCyan.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
        .shadow(elevation = 16.dp, shape = RoundedCornerShape(24.dp)),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "K",
        fontSize = 44.sp,
        fontWeight = FontWeight.Black,
        color = KickCyan,
        fontFamily = FontFamily.SansSerif
      )
    }

    Spacer(modifier = Modifier.height(18.dp))

    Text(
      text = "KICK",
      fontSize = 26.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 4.sp,
      color = KickTextPrimary
    )

    Text(
      text = "The Trust-Distance Network",
      fontSize = 13.sp,
      fontWeight = FontWeight.Medium,
      color = KickCyan,
      letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(36.dp))

    // Form Container
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, KickBorder, RoundedCornerShape(20.dp)),
      color = KickCard
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "PHONE NUMBER",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = KickTextSecondary
        )
        Text(
          text = "Authenticate your cryptographic client instance",
          fontSize = 12.sp,
          color = KickTextMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone input row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Country Selector Chip
          Box {
            Surface(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, KickBorder, RoundedCornerShape(12.dp))
                .clickable { countryMenuOpen = true }
                .testTag("country_code_selector"),
              color = KickObsidian
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(text = selectedCountry.flag, fontSize = 18.sp)
                Text(
                  text = selectedCountry.dial,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = KickTextPrimary
                )
              }
            }

            DropdownMenu(
              expanded = countryMenuOpen,
              onDismissRequest = { countryMenuOpen = false }
            ) {
              countryCodes.forEach { country ->
                DropdownMenuItem(
                  text = { Text("${country.flag} ${country.name} (${country.dial})") },
                  onClick = {
                    onCountrySelected(country)
                    countryMenuOpen = false
                  }
                )
              }
            }
          }

          // Number input field
          OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            modifier = Modifier
              .weight(1f)
              .testTag("phone_number_input"),
            placeholder = { Text(text = "555 019 2834", color = KickTextMuted) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Phone,
              imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onContinue() }),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = KickCyan,
              unfocusedBorderColor = KickBorder,
              focusedTextColor = KickTextPrimary,
              unfocusedTextColor = KickTextPrimary,
              focusedContainerColor = KickObsidian,
              unfocusedContainerColor = KickObsidian
            ),
            shape = RoundedCornerShape(12.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick fill test button
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(KickCyan.copy(alpha = 0.1f))
            .clickable { onPhoneNumberChange("5552345678") }
            .padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = null,
            tint = KickCyan,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "Quick Test: (555) 234-5678",
            fontSize = 11.sp,
            color = KickCyan,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Privacy seal note
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x1800E5B8))
            .padding(10.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = KickCyan,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Sealed Identity: Your phone number is hashed with client-side zero knowledge proofs. Contacts never see your raw digits.",
            fontSize = 11.sp,
            color = KickCyan.copy(alpha = 0.9f),
            lineHeight = 15.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Continue button
    Button(
      onClick = onContinue,
      enabled = phoneNumber.length >= 6,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("auth_continue_btn"),
      shape = RoundedCornerShape(14.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = KickCyan,
        disabledContainerColor = KickCard
      )
    ) {
      Text(
        text = "Send Verification Token",
        color = if (phoneNumber.length >= 6) Color(0xFF080D12) else KickTextMuted,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
      )
      Spacer(modifier = Modifier.width(8.dp))
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        tint = if (phoneNumber.length >= 6) Color(0xFF080D12) else KickTextMuted,
        modifier = Modifier.size(18.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Guest / Direct Preview Option
    Text(
      text = "Already calibrated? Explore as Verified Guest →",
      fontSize = 12.sp,
      color = KickTextMuted,
      modifier = Modifier
        .clickable { onBypass() }
        .padding(8.dp)
    )
  }
}

@Composable
fun OtpVerificationStep(
  fullPhone: String,
  otpDigits: String,
  onOtpChange: (String) -> Unit,
  otpError: String?,
  resendTimer: Int,
  onResend: () -> Unit,
  onBack: () -> Unit,
  onVerify: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp, vertical = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top bar with back
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = KickTextSecondary
        )
      }
      Spacer(modifier = Modifier.weight(1f))
      Text(
        text = "STEP 2 OF 3",
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = KickCyan
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Box(
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .background(KickCyan.copy(alpha = 0.15f))
        .border(1.dp, KickCyan.copy(alpha = 0.4f), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Key,
        contentDescription = null,
        tint = KickCyan,
        modifier = Modifier.size(30.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "VERIFY TOKEN",
      fontSize = 22.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 2.sp,
      color = KickTextPrimary
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "Token sent via encrypted carrier relay to",
      fontSize = 12.sp,
      color = KickTextSecondary
    )
    Text(
      text = fullPhone,
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold,
      color = KickCyan
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Mock OTP Banner (Crucial for user requirement: "mock OTP 123456")
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .border(1.dp, KickAmber.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
      color = KickAmber.copy(alpha = 0.1f)
    ) {
      Row(
        modifier = Modifier.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "MOCK OTP READY",
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = KickAmber
          )
          Text(
            text = "Use code: 123456",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = KickTextPrimary
          )
        }

        Button(
          onClick = {
            onOtpChange("123456")
          },
          colors = ButtonDefaults.buttonColors(containerColor = KickAmber),
          shape = RoundedCornerShape(10.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(
            text = "Quick Fill",
            color = Color(0xFF1E1300),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // 6-digit display boxes
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      for (i in 0 until 6) {
        val char = otpDigits.getOrNull(i)?.toString() ?: ""
        val isFocused = otpDigits.length == i

        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(KickObsidian)
            .border(
              width = if (isFocused) 2.dp else 1.dp,
              color = when {
                otpError != null -> Color(0xFFFF5252)
                isFocused -> KickCyan
                char.isNotEmpty() -> KickCyan.copy(alpha = 0.5f)
                else -> KickBorder
              },
              shape = RoundedCornerShape(12.dp)
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = char,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = KickTextPrimary
          )
        }
      }
    }

    // Hidden actual text field to drive the input
    OutlinedTextField(
      value = otpDigits,
      onValueChange = {
        if (it.length <= 6 && it.all { ch -> ch.isDigit() }) {
          onOtpChange(it)
          if (it.length == 6 && it == "123456") {
            onVerify()
          }
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)
        .testTag("otp_hidden_input"),
      placeholder = { Text("Type 6-digit code or tap Quick Fill", color = KickTextMuted, fontSize = 12.sp) },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.NumberPassword,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(onDone = { onVerify() }),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = KickCyan,
        unfocusedBorderColor = KickBorder,
        focusedTextColor = KickTextPrimary,
        unfocusedTextColor = KickTextPrimary,
        focusedContainerColor = KickObsidian,
        unfocusedContainerColor = KickObsidian
      ),
      shape = RoundedCornerShape(12.dp)
    )

    if (otpError != null) {
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = otpError,
        fontSize = 12.sp,
        color = Color(0xFFFF5252),
        fontWeight = FontWeight.Medium
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Resend text
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      if (resendTimer > 0) {
        Text(
          text = "Resend token in ${resendTimer}s",
          fontSize = 12.sp,
          color = KickTextMuted
        )
      } else {
        Text(
          text = "Resend Token",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = KickCyan,
          modifier = Modifier.clickable { onResend() }
        )
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    // Verify Button
    Button(
      onClick = onVerify,
      enabled = otpDigits.length == 6,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("verify_otp_btn"),
      shape = RoundedCornerShape(14.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = KickCyan,
        disabledContainerColor = KickCard
      )
    ) {
      Text(
        text = "Verify & Calibrate Profile",
        color = if (otpDigits.length == 6) Color(0xFF080D12) else KickTextMuted,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
      )
      Spacer(modifier = Modifier.width(8.dp))
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = null,
        tint = if (otpDigits.length == 6) Color(0xFF080D12) else KickTextMuted,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}

@Composable
fun ProfileSetupStep(
  name: String,
  onNameChange: (String) -> Unit,
  handle: String,
  onHandleChange: (String) -> Unit,
  facet: String,
  onFacetChange: (String) -> Unit,
  bio: String,
  onBioChange: (String) -> Unit,
  avatarColor: Color,
  onAvatarColorChange: (Color) -> Unit,
  onBoom: () -> Unit
) {
  val avatarColors = listOf(
    KickCyan,
    KickAmber,
    KickViolet,
    KickBlue,
    Color(0xFFFF5252),
    Color(0xFF20BF6B)
  )

  val facetOptions = listOf(
    "Distributed Architect & ZK",
    "Autonomous Robotics & Embedded",
    "Creative Coder & Modular Audio",
    "DeepTech Founder & Syndicate",
    "Quantum Computing & Physics",
    "Clean Energy Microgrids"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp, vertical = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "SET YOUR PROFILE",
        fontSize = 14.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.5.sp,
        color = KickCyan
      )
      Text(
        text = "FINAL CALIBRATION",
        fontSize = 10.sp,
        color = KickTextSecondary
      )
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Avatar preview
    Box(
      modifier = Modifier
        .size(92.dp)
        .clip(CircleShape)
        .background(avatarColor)
        .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape)
        .shadow(elevation = 20.dp, shape = CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = name.take(1).uppercase().ifBlank { "K" },
        fontSize = 38.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF080D12)
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Avatar Color Palette Picker
    Row(
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      avatarColors.forEach { col ->
        val isSelected = col == avatarColor
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(col)
            .border(
              if (isSelected) 2.5.dp else 1.dp,
              if (isSelected) Color.White else Color.Transparent,
              CircleShape
            )
            .clickable { onAvatarColorChange(col) }
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Form inputs
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, KickBorder, RoundedCornerShape(20.dp)),
      color = KickCard
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        // Name
        Text(
          text = "DISPLAY NAME",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = KickTextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = name,
          onValueChange = onNameChange,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_name_input"),
          placeholder = { Text("e.g. Alex Vance", color = KickTextMuted) },
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KickCyan,
            unfocusedBorderColor = KickBorder,
            focusedTextColor = KickTextPrimary,
            unfocusedTextColor = KickTextPrimary,
            focusedContainerColor = KickObsidian,
            unfocusedContainerColor = KickObsidian
          ),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Handle
        Text(
          text = "HANDLE (@USERNAME)",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = KickTextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = handle,
          onValueChange = {
            onHandleChange(if (it.startsWith("@")) it else "@$it")
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_handle_input"),
          placeholder = { Text("@username", color = KickTextMuted) },
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KickCyan,
            unfocusedBorderColor = KickBorder,
            focusedTextColor = KickTextPrimary,
            unfocusedTextColor = KickTextPrimary,
            focusedContainerColor = KickObsidian,
            unfocusedContainerColor = KickObsidian
          ),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Primary Facet selector
        Text(
          text = "PRIMARY FACET (ORBIT IDENTITY)",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = KickTextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(facetOptions) { option ->
            val isSelected = facet == option
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) KickCyan.copy(alpha = 0.2f) else KickObsidian)
                .border(
                  1.dp,
                  if (isSelected) KickCyan else KickBorder,
                  RoundedCornerShape(10.dp)
                )
                .clickable { onFacetChange(option) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Text(
                text = option,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) KickCyan else KickTextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bio
        Text(
          text = "INTENT STATEMENT / BIO",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = KickTextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = bio,
          onValueChange = onBioChange,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_bio_input"),
          placeholder = { Text("What are you building or researching?", color = KickTextMuted) },
          maxLines = 3,
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KickCyan,
            unfocusedBorderColor = KickBorder,
            focusedTextColor = KickTextPrimary,
            unfocusedTextColor = KickTextPrimary,
            focusedContainerColor = KickObsidian,
            unfocusedContainerColor = KickObsidian
          ),
          shape = RoundedCornerShape(12.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // THE BOOM BUTTON!
    Button(
      onClick = onBoom,
      enabled = name.isNotBlank() && handle.isNotBlank(),
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .testTag("auth_boom_btn")
        .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp)),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(containerColor = KickCyan)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.RocketLaunch,
          contentDescription = null,
          tint = Color(0xFF080D12),
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "BOOM! ENTER KICK",
          color = Color(0xFF080D12),
          fontWeight = FontWeight.Black,
          fontSize = 16.sp,
          letterSpacing = 1.sp
        )
      }
    }
  }
}
