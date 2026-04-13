package com.vyn.player.ui.onboarding

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.onboarding.components.ProgressIndicator
import com.vyn.player.ui.onboarding.pages.DonePage
import com.vyn.player.ui.onboarding.pages.PermissionPage
import com.vyn.player.ui.onboarding.pages.WelcomePage

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onCompleted: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        Log.d("ONBOARDING_FLOW", "Permission result: granted=$granted")
        if (granted) {
            viewModel.onEvent(OnboardingEvent.PermissionGranted)
            viewModel.onEvent(OnboardingEvent.NextClicked)
        }
    }

    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) {
            onCompleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        ProgressIndicator(
            currentPage = state.currentPage,
            pageCount = PAGE_COUNT,
        )

        when (state.currentPage) {
            0 -> WelcomePage(
                stepLabel = "Step 1 of 3",
                onNextClick = { viewModel.onEvent(OnboardingEvent.NextClicked) },
            )

            1 -> PermissionPage(
                stepLabel = "Step 2 of 3",
                permissionName = permission,
                onRequestPermission = {
                    val isGranted = ContextCompat.checkSelfPermission(
                        context,
                        permission,
                    ) == PackageManager.PERMISSION_GRANTED

                    if (isGranted) {
                        viewModel.onEvent(OnboardingEvent.PermissionGranted)
                        viewModel.onEvent(OnboardingEvent.NextClicked)
                    } else {
                        permissionLauncher.launch(permission)
                    }
                },
            )

            else -> DonePage(
                stepLabel = "Step 3 of 3",
                onFinishClick = { viewModel.onEvent(OnboardingEvent.Finish) },
            )
        }
    }
}

private const val PAGE_COUNT = 3
