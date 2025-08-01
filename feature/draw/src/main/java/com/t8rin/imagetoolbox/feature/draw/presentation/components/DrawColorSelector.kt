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

package com.t8rin.imagetoolbox.feature.draw.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.t8rin.imagetoolbox.core.resources.R
import com.t8rin.imagetoolbox.core.resources.icons.BorderColor
import com.t8rin.imagetoolbox.core.ui.widget.color_picker.ColorSelectionRowDefaults
import com.t8rin.imagetoolbox.core.ui.widget.controls.selection.ColorRowSelector
import com.t8rin.imagetoolbox.core.ui.widget.modifier.ShapeDefaults
import com.t8rin.imagetoolbox.core.ui.widget.modifier.container

@Composable
fun DrawColorSelector(
    modifier: Modifier = Modifier
        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    value: Color,
    onValueChange: (Color) -> Unit,
    color: Color = Color.Unspecified,
    titleText: String = stringResource(R.string.paint_color),
    defaultColors: List<Color> = ColorSelectionRowDefaults.colorList,
) {
    ColorRowSelector(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .container(
                shape = ShapeDefaults.extraLarge,
                color = color
            ),
        title = titleText,
        allowAlpha = false,
        icon = Icons.Outlined.BorderColor,
        defaultColors = defaultColors
    )
}