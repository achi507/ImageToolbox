/*
 * ImageToolbox is an image editor for android
 * Copyright (c) 2024 T8RIN (Malik Mukhametzyanov)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */

package ru.tech.imageresizershrinker.noise_generation.presentation

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.tech.imageresizershrinker.core.domain.image.model.ImageInfo
import ru.tech.imageresizershrinker.core.resources.R
import ru.tech.imageresizershrinker.core.ui.utils.animation.animate
import ru.tech.imageresizershrinker.core.ui.utils.confetti.LocalConfettiHostState
import ru.tech.imageresizershrinker.core.ui.utils.helper.asClip
import ru.tech.imageresizershrinker.core.ui.utils.helper.isPortraitOrientationAsState
import ru.tech.imageresizershrinker.core.ui.utils.helper.parseSaveResult
import ru.tech.imageresizershrinker.core.ui.utils.navigation.Screen
import ru.tech.imageresizershrinker.core.ui.utils.state.derivedValueOf
import ru.tech.imageresizershrinker.core.ui.widget.AdaptiveLayoutScreen
import ru.tech.imageresizershrinker.core.ui.widget.buttons.BottomButtonsBlock
import ru.tech.imageresizershrinker.core.ui.widget.buttons.ShareButton
import ru.tech.imageresizershrinker.core.ui.widget.controls.ResizeImageField
import ru.tech.imageresizershrinker.core.ui.widget.controls.selection.ImageFormatSelector
import ru.tech.imageresizershrinker.core.ui.widget.controls.selection.QualitySelector
import ru.tech.imageresizershrinker.core.ui.widget.dialogs.OneTimeSaveLocationSelectionDialog
import ru.tech.imageresizershrinker.core.ui.widget.image.Picture
import ru.tech.imageresizershrinker.core.ui.widget.modifier.container
import ru.tech.imageresizershrinker.core.ui.widget.other.Loading
import ru.tech.imageresizershrinker.core.ui.widget.other.LoadingDialog
import ru.tech.imageresizershrinker.core.ui.widget.other.LocalToastHostState
import ru.tech.imageresizershrinker.core.ui.widget.other.TopAppBarEmoji
import ru.tech.imageresizershrinker.core.ui.widget.sheets.ProcessImagesPreferenceSheet
import ru.tech.imageresizershrinker.core.ui.widget.text.marquee
import ru.tech.imageresizershrinker.noise_generation.presentation.components.NoiseParamsSelection
import ru.tech.imageresizershrinker.noise_generation.presentation.viewModel.NoiseGenerationViewModel

@Composable
fun NoiseGenerationContent(
    onGoBack: () -> Unit,
    onNavigate: (Screen) -> Unit,
    viewModel: NoiseGenerationViewModel = hiltViewModel()
) {
    val toastHostState = LocalToastHostState.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val confettiHostState = LocalConfettiHostState.current

    val showConfetti: () -> Unit = {
        scope.launch {
            confettiHostState.showConfetti()
        }
    }

    val isPortrait by isPortraitOrientationAsState()

    val saveBitmap: (oneTimeSaveLocationUri: String?) -> Unit = {
        viewModel.saveNoise(it) { saveResult ->
            context.parseSaveResult(
                saveResult = saveResult,
                onSuccess = showConfetti,
                toastHostState = toastHostState,
                scope = scope
            )
        }
    }

    val shareButton: @Composable () -> Unit = {
        var editSheetData by remember {
            mutableStateOf(listOf<Uri>())
        }
        ShareButton(
            onShare = {
                viewModel.shareNoise(showConfetti)
            },
            onCopy = { manager ->
                viewModel.cacheCurrentNoise { uri ->
                    manager.setClip(uri.asClip(context))
                    showConfetti()
                }
            },
            onEdit = {
                viewModel.cacheCurrentNoise {
                    editSheetData = listOf(it)
                }
            }
        )
        ProcessImagesPreferenceSheet(
            uris = editSheetData,
            visible = editSheetData.isNotEmpty(),
            onDismiss = {
                if (!it) {
                    editSheetData = emptyList()
                }
            },
            onNavigate = { screen ->
                scope.launch {
                    editSheetData = emptyList()
                    delay(200)
                    onNavigate(screen)
                }
            }
        )
    }

    AdaptiveLayoutScreen(
        title = {
            Text(
                text = stringResource(R.string.noise_generation),
                textAlign = TextAlign.Center,
                modifier = Modifier.marquee()
            )
        },
        onGoBack = onGoBack,
        actions = {},
        topAppBarPersistentActions = {
            TopAppBarEmoji()
        },
        imagePreview = {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Picture(
                    model = viewModel.previewBitmap,
                    modifier = Modifier
                        .container(MaterialTheme.shapes.medium)
                        .aspectRatio(viewModel.noiseSize.safeAspectRatio.animate()),
                    shape = MaterialTheme.shapes.medium,
                    contentScale = ContentScale.FillBounds
                )
                if (viewModel.isImageLoading) Loading()
            }
        },
        controls = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResizeImageField(
                    imageInfo = derivedValueOf(viewModel.noiseSize) {
                        ImageInfo(viewModel.noiseSize.width, viewModel.noiseSize.height)
                    },
                    originalSize = null,
                    onWidthChange = viewModel::setNoiseWidth,
                    onHeightChange = viewModel::setNoiseHeight
                )
                NoiseParamsSelection(
                    value = viewModel.noiseParams,
                    onValueChange = viewModel::updateParams
                )
                Spacer(Modifier.height(4.dp))
                ImageFormatSelector(
                    value = viewModel.imageFormat,
                    onValueChange = viewModel::setImageFormat,
                    forceEnabled = true
                )
                QualitySelector(
                    quality = viewModel.quality,
                    imageFormat = viewModel.imageFormat,
                    onQualityChange = viewModel::setQuality
                )
            }
        },
        buttons = {
            var showFolderSelectionDialog by rememberSaveable {
                mutableStateOf(false)
            }
            BottomButtonsBlock(
                targetState = false to isPortrait,
                isSecondaryButtonVisible = false,
                onSecondaryButtonClick = {},
                onPrimaryButtonClick = {
                    saveBitmap(null)
                },
                onPrimaryButtonLongClick = {
                    showFolderSelectionDialog = true
                },
                actions = {
                    shareButton()
                }
            )
            if (showFolderSelectionDialog) {
                OneTimeSaveLocationSelectionDialog(
                    onDismiss = { showFolderSelectionDialog = false },
                    onSaveRequest = saveBitmap,
                    formatForFilenameSelection = viewModel.getFormatForFilenameSelection()
                )
            }
        },
        canShowScreenData = true,
        isPortrait = isPortrait
    )

    if (viewModel.isSaving) {
        LoadingDialog(onCancelLoading = viewModel::cancelSaving)
    }
}