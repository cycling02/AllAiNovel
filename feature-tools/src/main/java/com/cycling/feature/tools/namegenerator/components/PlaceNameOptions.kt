package com.cycling.feature.tools.namegenerator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cycling.domain.usecase.namegenerator.PlaceType

@Composable
fun PlaceNameOptions(
    placeType: PlaceType,
    onPlaceTypeSelected: (PlaceType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "地名类型",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            FilterChip(
                selected = placeType == PlaceType.CITY,
                onClick = { onPlaceTypeSelected(PlaceType.CITY) },
                label = { Text("城市") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = placeType == PlaceType.MOUNTAIN,
                onClick = { onPlaceTypeSelected(PlaceType.MOUNTAIN) },
                label = { Text("山脉") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = placeType == PlaceType.RIVER,
                onClick = { onPlaceTypeSelected(PlaceType.RIVER) },
                label = { Text("河流") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            FilterChip(
                selected = placeType == PlaceType.FOREST,
                onClick = { onPlaceTypeSelected(PlaceType.FOREST) },
                label = { Text("森林") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = placeType == PlaceType.LAKE,
                onClick = { onPlaceTypeSelected(PlaceType.LAKE) },
                label = { Text("湖泊") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = placeType == PlaceType.RANDOM,
                onClick = { onPlaceTypeSelected(PlaceType.RANDOM) },
                label = { Text("随机") }
            )
        }
    }
}
