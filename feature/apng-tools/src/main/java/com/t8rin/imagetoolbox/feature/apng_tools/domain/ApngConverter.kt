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

package com.t8rin.imagetoolbox.feature.apng_tools.domain

import com.t8rin.imagetoolbox.core.domain.image.model.ImageFormat
import com.t8rin.imagetoolbox.core.domain.image.model.Quality
import kotlinx.coroutines.flow.Flow

interface ApngConverter {

    fun extractFramesFromApng(
        apngUri: String,
        imageFormat: ImageFormat,
        quality: Quality
    ): Flow<String>

    suspend fun createApngFromImageUris(
        imageUris: List<String>,
        params: ApngParams,
        onFailure: (Throwable) -> Unit,
        onProgress: () -> Unit
    ): String?

    suspend fun convertApngToJxl(
        apngUris: List<String>,
        quality: Quality.Jxl,
        onProgress: suspend (String, ByteArray) -> Unit
    )

}