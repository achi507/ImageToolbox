/*
 * ImageToolbox is an image editor for android
 * Copyright (c) 2025 T8RIN (Malik Mukhametzyanov)
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

package ru.tech.imageresizershrinker.core.crash.presentation.components

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.exifinterface.media.ExifInterface
import com.t8rin.dynamic.theme.ColorTuple
import com.t8rin.dynamic.theme.extractPrimaryColor
import ru.tech.imageresizershrinker.core.crash.presentation.CrashActivity
import ru.tech.imageresizershrinker.core.domain.TELEGRAM_GROUP_LINK
import ru.tech.imageresizershrinker.core.domain.image.ImageGetter
import ru.tech.imageresizershrinker.core.resources.R
import ru.tech.imageresizershrinker.core.resources.emoji.Emoji
import ru.tech.imageresizershrinker.core.resources.icons.Github
import ru.tech.imageresizershrinker.core.resources.icons.ImageToolboxBroken
import ru.tech.imageresizershrinker.core.resources.icons.Telegram
import ru.tech.imageresizershrinker.core.settings.presentation.model.toUiState
import ru.tech.imageresizershrinker.core.ui.shapes.IconShapeDefaults
import ru.tech.imageresizershrinker.core.ui.theme.Black
import ru.tech.imageresizershrinker.core.ui.theme.Blue
import ru.tech.imageresizershrinker.core.ui.theme.ImageToolboxThemeSurface
import ru.tech.imageresizershrinker.core.ui.theme.White
import ru.tech.imageresizershrinker.core.ui.theme.outlineVariant
import ru.tech.imageresizershrinker.core.ui.utils.helper.AppActivityClass
import ru.tech.imageresizershrinker.core.ui.utils.provider.ImageToolboxCompositionLocals
import ru.tech.imageresizershrinker.core.ui.utils.provider.LocalScreenSize
import ru.tech.imageresizershrinker.core.ui.utils.provider.rememberLocalEssentials
import ru.tech.imageresizershrinker.core.ui.widget.enhanced.EnhancedButton
import ru.tech.imageresizershrinker.core.ui.widget.enhanced.EnhancedFloatingActionButton
import ru.tech.imageresizershrinker.core.ui.widget.other.ExpandableItem
import ru.tech.imageresizershrinker.core.ui.widget.other.ToastHost
import ru.tech.imageresizershrinker.core.ui.widget.text.AutoSizeText

@Composable
internal fun CrashActivity.CrashRootContent() {
    val crashInfo = remember { getCrashInfo() }

    ImageToolboxCompositionLocals(
        settingsState = getSettingsState().toUiState(
            allEmojis = Emoji.allIcons(),
            allIconShapes = IconShapeDefaults.shapes,
            onGetEmojiColorTuple = imageGetter::getColorTupleFromEmoji
        )
    ) {
        val essentials = rememberLocalEssentials()
        val copyCrashInfo: () -> Unit = {
            essentials.copyToClipboard(crashInfo.textToSend)
            essentials.showToast(
                icon = Icons.Rounded.ContentCopy,
                message = getString(R.string.copied),
            )
        }

        val linkHandler = LocalUriHandler.current

        ImageToolboxThemeSurface {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState())
                    .displayCutoutPadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Outlined.ImageToolboxBroken,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .statusBarsPadding()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.something_went_wrong_emphasis),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                val screenWidth = LocalScreenSize.current.width - 32.dp
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EnhancedButton(
                        onClick = {
                            copyCrashInfo()
                            linkHandler.openUri(TELEGRAM_GROUP_LINK)
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f)
                            .width(screenWidth / 2f)
                            .height(50.dp),
                        containerColor = Blue,
                        contentColor = White,
                        borderColor = MaterialTheme.colorScheme.outlineVariant(
                            onTopOf = Blue
                        ),
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Telegram,
                                contentDescription = stringResource(R.string.telegram)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            AutoSizeText(
                                text = stringResource(id = R.string.contact_me),
                                maxLines = 1
                            )
                        }
                    }
                    EnhancedButton(
                        onClick = {
                            copyCrashInfo()
                            linkHandler.openUri(crashInfo.githubLink)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .width(screenWidth / 2f)
                            .height(50.dp),
                        containerColor = Black,
                        contentColor = White,
                        borderColor = MaterialTheme.colorScheme.outlineVariant(
                            onTopOf = Black
                        ),
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Github,
                                contentDescription = stringResource(R.string.github)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            AutoSizeText(
                                text = stringResource(id = R.string.create_issue),
                                maxLines = 1
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                ExpandableItem(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding(),
                    visibleContent = {
                        Icon(
                            imageVector = Icons.Rounded.BugReport,
                            contentDescription = null,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 16.dp
                            )
                        )
                        AutoSizeText(
                            text = crashInfo.exceptionName,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f)
                        )
                    },
                    expandableContent = {
                        AnimatedVisibility(visible = it) {
                            SelectionContainer {
                                Text(
                                    text = crashInfo.stackTrace,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(80.dp))
            }
            Row(
                Modifier
                    .padding(8.dp)
                    .navigationBarsPadding()
                    .displayCutoutPadding()
                    .align(Alignment.BottomCenter)
            ) {
                EnhancedFloatingActionButton(
                    modifier = Modifier
                        .weight(1f, false),
                    onClick = {
                        startActivity(
                            Intent(
                                this@CrashRootContent,
                                AppActivityClass
                            )
                        )
                    },
                    content = {
                        Spacer(Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Rounded.RestartAlt,
                            contentDescription = stringResource(R.string.restart_app)
                        )
                        Spacer(Modifier.width(16.dp))
                        AutoSizeText(
                            text = stringResource(R.string.restart_app),
                            maxLines = 1
                        )
                        Spacer(Modifier.width(16.dp))
                    }
                )
                Spacer(Modifier.width(8.dp))
                EnhancedFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onClick = copyCrashInfo
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = stringResource(R.string.copy)
                    )
                }
            }

            ToastHost()
        }
    }
}

private suspend fun ImageGetter<Bitmap, ExifInterface>.getColorTupleFromEmoji(
    emojiUri: String
): ColorTuple? = getImage(data = emojiUri)
    ?.extractPrimaryColor()
    ?.let { ColorTuple(it) }