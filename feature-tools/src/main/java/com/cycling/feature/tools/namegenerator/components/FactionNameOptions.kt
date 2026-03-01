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
import com.cycling.domain.usecase.namegenerator.FactionType

@Composable
fun FactionNameOptions(
    factionType: FactionType,
    onFactionTypeSelected: (FactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "势力类型",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            FilterChip(
                selected = factionType == FactionType.SECT,
                onClick = { onFactionTypeSelected(FactionType.SECT) },
                label = { Text("宗门") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = factionType == FactionType.FAMILY,
                onClick = { onFactionTypeSelected(FactionType.FAMILY) },
                label = { Text("家族") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = factionType == FactionType.EMPIRE,
                onClick = { onFactionTypeSelected(FactionType.EMPIRE) },
                label = { Text("帝国") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            FilterChip(
                selected = factionType == FactionType.GUILD,
                onClick = { onFactionTypeSelected(FactionType.GUILD) },
                label = { Text("商会") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = factionType == FactionType.GANG,
                onClick = { onFactionTypeSelected(FactionType.GANG) },
                label = { Text("帮派") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = factionType == FactionType.RANDOM,
                onClick = { onFactionTypeSelected(FactionType.RANDOM) },
                label = { Text("随机") }
            )
        }
    }
}
